package com.example.animeverse.presentation.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.animeverse.R
import com.example.animeverse.databinding.FragmentDetailBinding
import com.example.animeverse.domain.AnimeDetails
import com.example.animeverse.presentation.home.HomeViewModel
import com.google.android.material.chip.Chip
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null

    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    private val args: DetailFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater , container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animeId = args.animeId

        // Trigger ViewModel to fetch data for this ID
        viewModel.getAnimeDetails(animeId)

        observeViewModel()
    }

    fun setUpUI(animeDetails: AnimeDetails) {
        Log.d("details", animeDetails.toString())
        binding.apply {
            tvDetailTitle.text = animeDetails.title
            tvDetailRating.text ="⭐ ${animeDetails.rating}"
            tvDetailEpisodes.text = "${animeDetails.episodes} Episodes"
            tvSynopsis.text = animeDetails.plot
            tvCast.text = animeDetails.mainCast

            cgGenres.removeAllViews()
            animeDetails.genres.forEach { genre->
                val chip = Chip(requireContext())
                chip.text = genre
                cgGenres.addView(chip)
            }

        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.animeDetails.collect { animeDetails ->
                    animeDetails?.let { details ->
                        binding.progressBar.visibility = View.GONE
                        binding.groupSynopsisCast.visibility = View.VISIBLE
                        setUpUI(details)
                        setUpYoutubePlayer(details)
                    }
                    if (animeDetails == null) {
                        binding.groupSynopsisCast.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun extractYoutubeId(url: String?): String? {
        if (url.isNullOrEmpty()) return null
        return try {

            // Handles https://www.youtube.com/embed/VIDEO_ID?params
            if (url.contains("embed/")) {
                url.substringAfter("embed/").substringBefore("?")
            } else {
                // Fallback for standard watch URLs
                val pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%2Fvideos%2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"
                val compiledPattern = java.util.regex.Pattern.compile(pattern)
                val matcher = compiledPattern.matcher(url)
                if (matcher.find()) matcher.group() else null
            }
        } catch (e: Exception) {
            Log.e("youtube_debug", "Extraction failed: ${e.message}")
            null
        }
    }

    private fun setUpYoutubePlayer(animeDetails: AnimeDetails) {
        val videoId = extractYoutubeId(animeDetails.trailerEmbedUrl)

        binding.apply {
            Glide.with(ivDetailPoster.context)
                .load(animeDetails.imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(ivDetailPoster)

            if (!videoId.isNullOrEmpty()) {
                ivPlayButton.visibility = View.VISIBLE

                val clickListener = View.OnClickListener {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=$videoId")
                    )
                    startActivity(intent)
                }

                ivDetailPoster.setOnClickListener(clickListener)
                ivPlayButton.setOnClickListener(clickListener)

            } else {
                ivPlayButton.visibility = View.GONE
                ivDetailPoster.setOnClickListener(null)
            }
        }
    }

    }
