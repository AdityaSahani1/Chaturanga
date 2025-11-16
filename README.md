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

The app includes placeholder ad unit IDs for testing. Replace these in `Constants.java` with your actual ad unit IDs:

```java
BANNER_AD_UNIT_ID = "YOUR_BANNER_AD_UNIT_ID"
INTERSTITIAL_AD_UNIT_ID = "YOUR_INTERSTITIAL_AD_UNIT_ID"
REWARDED_AD_UNIT_ID = "YOUR_REWARDED_AD_UNIT_ID"
NATIVE_AD_UNIT_ID = "YOUR_NATIVE_AD_UNIT_ID"
```

Current placeholder IDs are Google's test ad units.

## How to Build

This is an Android application written in Java. To build and run:

1. Open the project in Android Studio
2. Sync Gradle files
3. Connect an Android device or start an emulator
4. Click Run or use `./gradlew assembleDebug`

## Requirements

- Android SDK 21+ (Android 5.0 Lollipop and above)
- Java 8+
- Gradle 7.4.2+

## Historical Background

Chaturanga (चतुरंग) means "having four limbs" in Sanskrit, referring to the four divisions of an ancient Indian army. The game originated in 6th century CE India and evolved into:

- Shatranj in Persia (8th-9th century)
- Chess in Europe (12th-15th century)

This app faithfully recreates the original 4-player version with both dice-based and strategic variants, plus later historical versions.

## License

Created for educational and cultural preservation purposes.

## Credits

Based on historical research and traditional game rules documented in ancient Sanskrit literature.
