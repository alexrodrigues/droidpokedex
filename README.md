# DroidPokedex

[![CI](https://github.com/alexrodrigues/droidpokedex/actions/workflows/ci.yml/badge.svg)](https://github.com/alexrodrigues/droidpokedex/actions/workflows/ci.yml)

A Pokédex app for browsing and searching Pokémon using the public [PokéAPI](https://pokeapi.co/). It is built with **Kotlin**, **Jetpack Compose** (Material 3), **Navigation Compose**, and **Hilt** for dependency injection. The project follows a **multi-module, layered structure** (domain / data per feature, shared networking) inspired by Clean Architecture.

![Pokedex home and list experience](poke.png)

## Features

- Paginated Pokémon list with types and artwork (images via **Coil**)
- Search by name
- Detail screen with stats and metadata
- **Remote-first with session cache**: list, search, and detail requests load from [PokéAPI](https://pokeapi.co/) over HTTPS. Repositories keep a **small in-memory cache** (per app process); on **network or HTTP failures** they can return the last successful payload and the UI shows an **offline banner** ([`PokeHomeRepositoryImpl`](home/data/src/main/java/com/rodriguesalex/data/repository/PokeHomeRepositoryImpl.kt), [`PokeDetailsRepositoryImpl`](details/data/src/main/java/com/rodriguesalex/details/data/repository/PokeDetailsRepositoryImpl.kt)). There is **no Room/DataStore disk cache**—data is not persisted across process death.
- **Clearer error states**: failures are classified (no connection, API HTTP errors with status, invalid Pokémon id, generic) and shown with title, message, optional detail, and retry ([`UserErrorMapper`](app/src/main/java/com/rodriguesalex/droidpokedex/util/UserErrorMapper.kt), [`strings.xml`](app/src/main/res/values/strings.xml)).
- **Debug-only HTTP logging**: OkHttp **body** logging runs only when **`BuildConfig.DEBUG`** is true in the `:network` module ([`PokeNetworkModule`](network/src/main/java/com/rodriguesalex/droidpokedex/network/PokeNetworkModule.kt)); release builds do not attach the logging interceptor.

## Requirements

| Item | Version |
|------|---------|
| JDK | 17 |
| `minSdk` | 24 |
| `compileSdk` / `targetSdk` | 36 |

Use a recent **Android Studio** version compatible with the Android Gradle Plugin declared in [`gradle/libs.versions.toml`](gradle/libs.versions.toml) (currently **8.13.0**).

## Getting started

Clone the repository and open the project in Android Studio, or build from the command line:

```sh
git clone https://github.com/alexrodrigues/droidpokedex.git
cd droidpokedex
./gradlew :app:assembleDebug
```

Run unit tests (includes Paparazzi snapshot tests under `app/src/test`, which compare against checked-in golden images):

```sh
./gradlew test
```

After intentional UI changes, record new Paparazzi baselines:

```sh
./gradlew recordPaparazziDebug
```

## Continuous integration

[GitHub Actions](https://docs.github.com/en/actions) runs on every **push** and **pull request** targeting **`main`**. The workflow is defined in [`.github/workflows/ci.yml`](.github/workflows/ci.yml): it sets up **Temurin JDK 17**, the **Android SDK** (`android-actions/setup-android`), Gradle caching (`gradle/actions/setup-gradle`), then runs **`assembleDebug`**, **`ktlintCheck`**, **`detekt`**, and **`test`** (Gradle may reorder tasks according to the task graph):

```sh
./gradlew assembleDebug ktlintCheck detekt test --stacktrace -Dorg.gradle.java.home="$JAVA_HOME"
```

The `-Dorg.gradle.java.home=…` flag matches CI’s JDK with the value from `actions/setup-java` and overrides the optional machine-specific `org.gradle.java.home` entry in [`gradle.properties`](gradle.properties) (for example a local Homebrew path). Instrumented tests on an emulator are not part of this workflow; run those locally when needed.

## Architecture

Modules are declared in [`settings.gradle.kts`](settings.gradle.kts). **Domain** layers expose use cases and repository interfaces; **data** layers implement repositories and Retrofit service interfaces. The **`:network`** module provides a single configured `Retrofit` instance (with `OkHttp` and Gson). Feature data modules depend on **`:feature:domain`** and obtain `Retrofit` through **Hilt** bindings (they do not need a direct Gradle dependency on `:network` for the Retrofit type).

Compose **screens, navigation, and ViewModels** live in **`:app`** under packages such as `home` and `details`. The **`:home`** and **`:details`** Gradle modules are included for a consistent feature layout; the heavy feature code today sits in **`home:domain` / `home:data`** and **`details:domain` / `details:data`**. A natural evolution is to move presentation into those feature modules or toward optional dynamic delivery.

```mermaid
flowchart TB
  app["app"]
  network["network"]
  homeDomain["home:domain"]
  homeData["home:data"]
  detailsDomain["details:domain"]
  detailsData["details:data"]
  app --> network
  app --> homeDomain
  app --> homeData
  app --> detailsDomain
  app --> detailsData
  homeData --> homeDomain
  detailsData --> detailsDomain
```

### Dependency injection (Hilt)

- **`@HiltAndroidApp`** on [`DroidApp`](app/src/main/java/com/rodriguesalex/droidpokedex/DroidApp.kt) bootstraps the application component.
- **`@AndroidEntryPoint`** on [`MainActivity`](app/src/main/java/com/rodriguesalex/droidpokedex/MainActivity.kt) enables field injection in Android components.
- **`@Module` / `@InstallIn(SingletonComponent::class)`** classes (for example [`PokeNetworkModule`](network/src/main/java/com/rodriguesalex/droidpokedex/network/PokeNetworkModule.kt) and feature data modules such as [`PokeHomeDataModule`](home/data/src/main/java/com/rodriguesalex/data/di/PokeHomeDataModule.kt)) bind network and repository implementations at **singleton** scope where appropriate, which keeps a single `Retrofit`/`OkHttp` stack and makes collaborators easy to replace in tests.

### Modules

| Module | Responsibility |
|--------|----------------|
| `:app` | Application entry, Compose UI, navigation, ViewModels, theme, design-system components |
| `:network` | Shared `OkHttp`, Gson, `Retrofit`, and base URL for PokéAPI |
| `:home:domain` | Home feature: models, repository contracts, use cases |
| `:home:data` | Home feature: Retrofit service, repository implementation, Hilt module |
| `:home` | Feature shell (reserved for future UI extraction) |
| `:details:domain` | Details feature: models, contracts, use cases |
| `:details:data` | Details feature: API integration, repository implementation, Hilt module |
| `:details` | Feature shell (reserved for future UI extraction) |

### Design system

Reusable Compose primitives and **@Preview**-backed components live under [`app/.../designsystem`](app/src/main/java/com/rodriguesalex/droidpokedex/designsystem/components/). Additional previews exist on screens such as [`MainActivity`](app/src/main/java/com/rodriguesalex/droidpokedex/MainActivity.kt) and the details flow.

## Testing strategy

| Layer | Approach | Examples |
|-------|----------|----------|
| Domain | JUnit, MockK, coroutines test | [`SearchPokemonUseCaseTest`](home/domain/src/test/java/com/rodriguesalex/domain/SearchPokemonUseCaseTest.kt), [`GetPokeHomeUseCaseTest`](home/domain/src/test/java/com/rodriguesalex/domain/GetPokeHomeUseCaseTest.kt) |
| Data | JUnit, MockK, coroutines test | [`PokeHomeRepositoryImplTest`](home/data/src/test/java/com/rodriguesalex/data/PokeHomeRepositoryImplTest.kt) |
| Presentation | JUnit, MockK, `StandardTestDispatcher` | [`DroidHomeViewModelTest`](app/src/test/java/com/rodriguesalex/droidpokedex/home/viewmodel/DroidHomeViewModelTest.kt) |
| UI snapshots | Paparazzi | [`DroidHomeCellPaparazziTest`](app/src/test/java/com/rodriguesalex/droidpokedex/DroidHomeCellPaparazziTest.kt), [`SimplePaparazziTest`](app/src/test/java/com/rodriguesalex/droidpokedex/SimplePaparazziTest.kt) |
| On-device UI | Compose UI test dependencies on `:app` | `androidTest` with `ui-test-junit4` (run on emulator/device) |

## Code quality

Static analysis and formatting are applied from the root [`build.gradle.kts`](build.gradle.kts) using **Detekt** ([`config/detekt/detekt.yml`](config/detekt/detekt.yml)) and **ktlint**.

```sh
./gradlew detekt
./gradlew ktlintCheck
```

To run the default verification pipeline for all modules (unit tests, Android Lint, Detekt, and ktlint):

```sh
./gradlew check
```

## Limitations and possible extensions

- **No durable offline store**: in-memory cache helps during a single run when the API fails; **persisting** lists or details (Room, DataStore, or similar) would improve true offline use and startup after process death. Coil still caches images for URLs on disk where configured.
- **CI** on `main` runs **`assembleDebug`**, **`ktlintCheck`**, **`detekt`**, and **`test`** (see [Continuous integration](#continuous-integration)). The full **`./gradlew check`** graph (including Android Lint) is not run in CI unless you add it.
- **Accessibility**: Compose previews help iteration, but a serious pass would add content descriptions, contrast checks, and TalkBack testing—not claimed here.

## Tech stack (summary)

- Kotlin, Jetpack Compose, Material 3, Navigation Compose  
- Hilt, Coroutines, Flow  
- Retrofit, OkHttp, Gson  
- Coil  
- JUnit, MockK, kotlinx-coroutines-test, Paparazzi, Compose UI tests  
- GitHub Actions (build, ktlint, Detekt, unit tests)  

## API

- Base URL: `https://pokeapi.co/api/v2/` (configured in [`PokeNetworkModule`](network/src/main/java/com/rodriguesalex/droidpokedex/network/PokeNetworkModule.kt))
