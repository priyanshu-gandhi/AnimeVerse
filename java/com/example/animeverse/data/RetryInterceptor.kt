package com.example.animeverse.data

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class RetryInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response = chain.proceed(request)
        var tryCount = 0
        val maxLimit = 3 // Max retries
        var delay = 1000L // Start with 1 second

        while (!response.isSuccessful && response.code == 429 && tryCount < maxLimit) {
            Log.d("RetryInterceptor", "Rate limit hit (429). Retrying in ${delay}ms... (Attempt ${tryCount + 1})")

            // Wait
            Thread.sleep(delay)

            // Exponential backoff: increase delay for next time (1s, 2s, 4s...)
            delay *= 2
            tryCount++

            response.close()
            response = chain.proceed(request)
        }

        return response
    }

}