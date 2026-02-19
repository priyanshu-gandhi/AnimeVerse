AnimeVerse 🎥
AnimeVerse is a robust, offline-first Android application that tracks top-rated anime using the Jikan API. Built with modern Android development practices, it ensures a seamless user experience even without an internet connection.

🚀 Features Implemented
1. Core Functionality
Top Anime Listing: Displays a paginated list of top-rated anime with infinite scroll support.

Detailed View: Comprehensive details for each anime, including synopsis, ratings, episode counts, and a playable trailer.

Advanced Search: Real-time search functionality allowing users to find specific anime.

2. Offline-First Architecture (Single Source of Truth)
Room Persistence: All data fetched from the API is cached locally in a Room database.

Reactive UI: The UI observes Room via Kotlin Flows, ensuring instant updates whenever the database changes.

Network Resilience: If the API call fails, the app serves cached data immediately, providing a zero-latency experience.

3. Error Handling & UX
Live Network Observation: Monitors connectivity changes to automatically trigger data syncs when the device returns online.

Graceful State Management: Includes custom views for "Loading," "Empty Results," and "No Internet" states.

Smart Fallbacks: Handles restricted YouTube trailers by providing an "Open in YouTube" external intent.

🛠 Tech Stack
Language: Kotlin

Architecture: MVVM (Model-View-ViewModel) + Clean Architecture principles.

Dependency Injection: Hilt

Networking: Retrofit + OkHttp

Database: Room (with TypeConverters for complex lists)

UI Components: ViewBinding, ConstraintLayout, RecyclerView, Material Design 3.

Image Loading: Glide

📝 Assumptions Made
Content Safety: Assumed a general audience; implemented sfw=true in API queries to ensure search results are safe for work.

Data Freshness: Assumed that Top Anime rankings do not change every minute. Therefore, the app prioritizes showing cached data and performs an "Upsert" (Update or Insert) in the background to refresh details without flickering the UI.

Single Database: Assumed that merging List and Detail entities into a single table is more efficient for data integrity, preventing synchronization issues between two separate tables.

⚠️ Known Limitations
Jikan API Rate Limiting: The free tier of Jikan API has a rate limit. Rapid scrolling or frequent searching might occasionally trigger a 429 Too Many Requests error.

YouTube Embedding: Certain anime trailers are restricted by publishers (e.g., Kadokawa) for embedded playback. While the app provides a fallback, these cannot be played directly within the custom player.

Memory vs. Offline Depth: Currently, the app caches all viewed anime. While efficient for the current scope, a production-level app would require a "Cache Cleanup" strategy to delete older records after the database exceeds a certain size (e.g., 1000+ entries).

🏗 Installation
Clone the repository.

Build the project in Android Studio (Giraffe or newer).

Ensure you have an active internet connection for the initial data sync.

com.example.animeverse
├── data
│   ├── local
│   │   ├── dao             # Room DAOs for DB operations
│   │   ├── entities        # Database entities (Room models)
│   │   └── database        # AppDatabase & TypeConverters
│   ├── mapper              # DTO to Entity to Domain mappers
│   ├── remote
│   │   ├── api             # Retrofit Interface (Jikan API)
│   │   └── dto             # Network Data Transfer Objects
│   └── repository          # Repository Implementations (Sync logic)
├── domain
│   ├── model               # Pure Kotlin Domain models
│   └── repository          # Repository Interfaces (Abstractions)
├── di                      # Hilt Modules (Network/Database/Repo)
├── presentation
│   ├── home                # HomeFragment, ViewModel, Adapter
│   ├── detail              # DetailFragment, ViewModel
│   └── common              # Base classes & View Utils
└── util                    # Extension functions & Network Observer
