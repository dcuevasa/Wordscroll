# Wordscroll

A light, elegant, distraction-free app for reading poetry in a vertical-scroll, full-screen format.

Each poet is treated like an "account": browse poets, open a profile to see their poems, and read them one at a time in an immersive full-screen pager — swipe up for the next poem, just like a short-form video feed, but for verse.

Poems are fetched live from [PoetryDB](https://poetrydb.org), a free, public-domain poetry API — no API key required.

## Features

- **Feed** — an infinite vertical pager of random poems pulled live from PoetryDB.
- **Explore** — browse poets and open their profile to see all of their poems.
- **Saved** — bookmark poems locally (on-device only, via DataStore) and revisit them anytime, offline.
- **Share** — send a poem's title, author, and full text as plain text to any app (WhatsApp, Messages, etc.) via the native Android share sheet.

No social features beyond sharing: no friends, no comments, no following, no likes counters.

<br/>

## Tech stack
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material 3](https://m3.material.io/)
- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) / Flow
- [Hilt](https://dagger.dev/hilt/)
- [Retrofit](https://square.github.io/retrofit/) + [OkHttp](https://square.github.io/okhttp/) — talks to PoetryDB
- [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore) — local bookmarks
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- Multi-module, Kotlin DSL Gradle setup

<br/>

## Architecture
- Clean Architecture
- MVVM
- UDF (Unidirectional Data Flow)

<br/>

## Modularization
- `app` — entry point, navigation host, bottom bar
- `feature:home` — feed, explore, and saved screens
- `feature:creatorprofile` — poet profile and full-screen poem reader
- `data` — PoetryDB API client, bookmark repository, models
- `domain` — use cases
- `common:composable` — the shared vertical poem pager and other UI components
- `common:theme` — Material 3 theme, colors, typography
- `core` — shared base classes, navigation routes, utilities

<br/>

## Roadmap
- A second poetry source for non-English content (PoetryDB is English-only, public-domain classics).

<br/>

## License
[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Originally based on [TikTok-Compose](https://github.com/puskal-khadka/TikTok-Compose) by Puskal Khadka, licensed under Apache 2.0.
