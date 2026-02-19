package com.example.animeverse.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.animeverse.databinding.ItemAnimeBinding
import com.example.animeverse.domain.Anime
import com.example.animeverse.util.AppConfig

class AnimeAdapter(
    private val onItemClick: (Anime) -> Unit
) : ListAdapter<Anime, AnimeAdapter.AnimeViewHolder>(DiffUtilCallBack()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnimeAdapter.AnimeViewHolder {
        val binding = ItemAnimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnimeViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: AnimeAdapter.AnimeViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

     inner class AnimeViewHolder(private val binding: ItemAnimeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(anime : Anime){
            binding.apply {
                tvAnimeTitle.text = anime.title
                tvEpisodes.text = if(anime.episodes > 1) {
                    "${anime.episodes} Episodes" }
                else {"${anime.episodes} Episode" }
                tvRating.text = "⭐ ${anime.rating}"

                root.setOnClickListener { onItemClick(anime) }
            }
            if (AppConfig.SHOW_IMAGES){
                Glide.with(binding.ivAnimePoster.context)
                    .load(anime.imageUrl)
                    .placeholder(android.R.color.darker_gray)
                    .into(binding.ivAnimePoster)
            } else{
                binding.ivAnimePoster.setImageResource(android.R.color.darker_gray)
            }
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Anime>(){
        override fun areItemsTheSame(
            oldItem: Anime,
            newItem: Anime
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Anime,
            newItem: Anime
        ): Boolean {
           return oldItem == newItem
        }

    }
}