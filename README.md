# Chaturanga: The Ancient Game of Kings

A comprehensive Android game implementing multiple historical variants of Chaturanga, the ancient Indian predecessor to modern chess.

## Game Modes

### 1. 4-Player Indian Chaturanga (Dice Mode)
- Authentic 6th-century gameplay with dice rolls
- 4 kingdoms (South, West, North, East) compete
- Alliances: South+North vs East+West
- Dice determines which piece type can move each turn
- Win by being the last king standing

### 2. 4-Player Indian Chaturanga (Strategic Mode)
- Same setup as dice mode but without dice
- Players freely choose their moves
- More skill-based competitive gameplay

### 3. Persian Shatranj (2-Player)
- Historical Persian variant
- 2 players with traditional Shatranj rules
- Elephant moves 2 squares diagonally
- Queen (Mantri) has limited movement

### 4. Modern Chess (2-Player)
- Standard modern chess rules
- Full implementation with all piece movements
- Pawn promotion, castling ready

## Pieces and Movements

### Indian Chaturanga Pieces:
- **Raja (King)**: Moves 1 square in any direction
- **Ratha (Chariot)**: Moves any distance horizontally/vertically (like Rook)
- **Gaja (Elephant)**: Jumps exactly 2 squares diagonally
- **Ashva (Horse)**: L-shaped movement (like Knight)
- **Padati (Pawn)**: Moves 1 forward, captures diagonally

### Modern Chess Pieces:
- King, Queen, Rook, Bishop, Knight, Pawn with standard movements

## Features

- ✅ Multiple game modes (4-player dice, 4-player strategic, Persian, Chess)
- ✅ AI opponent with heuristic-based decision making
- ✅ Interactive tutorial explaining all rules
- ✅ Comprehensive history page about Chaturanga's origins
- ✅ Feedback system for user suggestions
- ✅ AdMob integration placeholders (banner, interstitial, rewarded, native ads)
- ✅ Clean, professional UI with color-coded players
- ✅ Move validation and highlighting
- ✅ Alliance system for 4-player modes
- ✅ Pawn promotion system
- ✅ Game over detection with victory screen

## Project Structure

```
com.adisoftc.chaturanga/
├── models/           # Data models (Piece, Player, GameMode, etc.)
├── logic/            # Game logic (Board, MoveValidator, GameState)
├── ai/               # AI opponent implementation
├── ui/               # UI components (BoardView)
├── utils/            # Utilities (Constants, AdManager)
├── MainActivity      # Home screen with mode selection
├── GameActivity      # Main gameplay screen
├── TutorialActivity  # How to play tutorial
├── HistoryActivity   # Historical information
└── FeedbackActivity  # User feedback form
```

## AdMob Integration

The app is fully configured with AdMob and includes test ad unit IDs. Ads are now properly initialized and will display:

**Current Features:**
- ✅ Banner ads in MainActivity and GameActivity
- ✅ Interstitial ads preloaded on app start
- ✅ Rewarded ads ready for future features
- ✅ Native ads support available

**To use your own ad units:**

1. Create an AdMob account at [admob.google.com](https://admob.google.com)
2. Create your app and get the Application ID
3. Update `AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.gms.ads.APPLICATION_ID"
       android:value="YOUR_APPLICATION_ID"/>
   ```
4. Create ad units and update IDs in `Constants.java`:
   ```java
   BANNER_AD_UNIT_ID = "YOUR_BANNER_AD_UNIT_ID"
   INTERSTITIAL_AD_UNIT_ID = "YOUR_INTERSTITIAL_AD_UNIT_ID"
   REWARDED_AD_UNIT_ID = "YOUR_REWARDED_AD_UNIT_ID"
   NATIVE_AD_UNIT_ID = "YOUR_NATIVE_AD_UNIT_ID"
   ```

Current test IDs use Google's official test ad units for development.

## Recent Bug Fixes (November 2025)

### Fixed Issues:
- ✅ **Ad Initialization**: Added proper AdManager initialization in MainActivity and GameActivity
- ✅ **Missing Imports**: Added `com.adisoftc.chaturanga.R` import to AdManager and SoundManager
- ✅ **Sound System**: Implemented actual sound resource loading in SoundManager.loadSounds()
- ✅ **Banner Ads**: Added banner ad containers and loading in all activities
- ✅ **Crash Prevention**: Fixed app crashes when opening game modes due to uninitialized managers
- ✅ **Interstitial Ads**: Ads now preload on app start to prevent delays
- ✅ **Layout Updates**: Added ad_container to MainActivity and GameActivity layouts

### Ad Integration
Banner ads will now display automatically in:
- Main menu (bottom of screen)
- Game screen (bottom of board area)

Interstitial ads are preloaded and ready to show between game sessions.

## How to Build

This is an Android application written in Java. To build and run:

1. Open the project in Android Studio (Arctic Fox or later)
2. Wait for Gradle sync to complete
3. Connect an Android device or start an emulator
4. Click Run (Shift+F10) or use command line:
   ```bash
   ./gradlew assembleDebug
   ```

## Requirements

- Android Studio Arctic Fox or later
- Android SDK 21+ (Android 5.0 Lollipop and above)
- Java 11+
- Gradle 7.4.2+

## Testing in Replit

Note: This is a native Android app that requires Android SDK and cannot run directly in Replit.
However, you can:
- View project documentation by opening the Webview (Documentation Server is running on port 5000)
- Edit code in Replit and sync to your local Android Studio
- Build the APK on your local machine or in Android Studio cloud

## Historical Background

Chaturanga (चतुरंग) means "having four limbs" in Sanskrit, referring to the four divisions of an ancient Indian army. The game originated in 6th century CE India and evolved into:

- Shatranj in Persia (8th-9th century)
- Chess in Europe (12th-15th century)

This app faithfully recreates the original 4-player version with both dice-based and strategic variants, plus later historical versions.

## License

Created for educational and cultural preservation purposes.

## Credits

Based on historical research and traditional game rules documented in ancient Sanskrit literature.
