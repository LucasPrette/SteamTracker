# SteamTracker
SteamTracker is a Spring Boot application designed to centralize game libraries, achievement progress, wishlist items, playtime statistics, and pricing information into a single dashboard.

The project aims to provide a unified gaming hub for tracking game libraries, achievements, wishlists, playtime statistics, and pricing information across multiple gaming platforms and storefronts.

## Features

* Synchronize owned games
* Track achievement progress and completion tiers
* Monitor recently played games
* Synchronize wishlist items
* Track game prices and discounts
* Display information through a Google Sheets dashboard
* Scheduled automatic synchronization

## Architecture

The application follows a layered architecture:

```text
Scheduler
    ↓
Services
    ↓
Providers
    ↓
Clients
    ↓
External APIs
```

### Providers

* LibraryProvider
* AchievementProvider
* WishlistProvider
* PriceProvider

This architecture allows future support for multiple gaming platforms without major changes to the service layer.

## Technologies

* Java 17
* Spring Boot
* Steam Web API
* Google Sheets API
* Maven
* Jackson
* SLF4J

## Current Integrations

### Steam

* Owned Games
* Recently Played Games
* Achievement Tracking
* Wishlist Tracking
* Price Monitoring

### Google Sheets

* Dashboard Visualization
* Temporary Data Persistence Layer

## Gaming Hub Features

SteamTracker goes beyond simple data synchronization by providing game classification and tracking features designed to help organize large game libraries.

### Automatic Status Engine

Owned games are automatically categorized based on playtime, achievement progress, and recent activity.

Available statuses:

* BACKLOG — Owned but not meaningfully started
* PLAYING — Recently active
* COMPLETED — Considered completed based on achievement and activity analysis
* ABANDONED — Started but no longer actively played
* WISHLIST — Games not yet owned

### Completion Tiers

SteamTracker evaluates achievement progress and assigns a completion tier to each game.

| Achievement Progress | Tier          |
| -------------------- | ------------- |
| 0%                   | UNSTARTED     |
| 1% - 39%             | IN_PROGRESS   |
| 40% - 74%            | STORY_CLEARED |
| 75% - 99%            | MASTERED      |
| 100%                 | PERFECTED     |

### Manual Status Overrides

Automatic classification can be overridden by the user.

This allows users to manually correct classifications for games where achievement data does not accurately reflect actual completion progress.


## Motivation

SteamTracker started as a personal project to consolidate gaming information from different platforms into a single location.

The long-term goal is to provide a unified gaming hub capable of tracking libraries, achievements, wishlist items, playtime statistics, and pricing information across multiple gaming ecosystems.


## Dashboard Preview

### Recently Played Games

![Recently Played Games](docs/playingTitlesDashBoard.png)

### Owned Games

![Owned Games](docs/ownedTitlesDashboard.png)

### Wishlist

![Wishlist](docs/wishlistTitles.png)

## Setup

### Prerequisites

* Java 17+
* Maven
* Steam Web API key
* Google Cloud Project with Sheets API enabled
* Google Service Account credentials

### Environment Variables

Create the following environment variables:

```properties
STEAM_API_KEY=<your-steam-api-key>
STEAM_ID=<your-steam-id>
SPREADSHEET_ID=<your-google-sheet-id>
```

### Required Files

The application requires a Google Service Account credentials JSON file.

The credentials file is intentionally excluded from version control and must be created manually following the Google Sheets Credentials section.

### Google Sheets Credentials

1. Create a Google Cloud Project.
2. Enable the Google Sheets API.
3. Create a Service Account.
4. Download the credentials JSON file.
5. Place the credentials file in the project root.
6. Share the target spreadsheet with the service account email.

### Running the Application

Clone the repository:

```bash
git clone https://github.com/LucasPrette/SteamTracker.git
cd SteamTracker
```

Run the application:

```bash
mvn spring-boot:run
```

### Local Development

Create:

```text
src/main/resources/application-local.properties
```

Example:

```properties
scheduler.owned-games.cron=0 */10 * * * *
scheduler.recent-games.cron=0 */5 * * * *
scheduler.wishlist.cron=0 */15 * * * *
```

Run with:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## Deployment

SteamTracker is designed to run continuously as a background service.

Typical deployment options:

* Linux server
* VPS
* Home server
* Docker container (planned)

The application uses scheduled jobs to synchronize gaming data with Google Sheets automatically.

## Supported Platforms

| Steam | ✅ | ✅ | ✅ | ✅ |
| GOG | Planned | Planned | Planned | Planned |
| Xbox | Planned | Planned | Planned | Planned |

## Roadmap

* [x] Steam Library Synchronization
* [x] Achievement Tracking
* [x] Wishlist Synchronization
* [x] Price Monitoring
* [x] Provider-Based Architecture
* [x] Automatic Status Engine
* [x] Completion Tier Classification
* [x] Manual Status Overrides
* [ ] IsThereAnyDeal Integration
* [ ] GOG Support
* [ ] Xbox Support
* [ ] Database Integration
* [ ] Web Dashboard

## Status

🚧 Active Development

SteamTracker is currently being developed as a personal project focused on learning software architecture, API integrations, and multi-platform game tracking.

