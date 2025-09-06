# 💣 Minesweeper Game

A modern, feature-rich Minesweeper game built with **Kotlin** and **Jetpack Compose** for Android. This project showcases clean architecture, smooth animations, sound effects, and an intuitive user interface.

## 🎮 Features

### ✨ **Core Gameplay**
- **Classic Minesweeper Logic**: 9x9 grid with 10 mines
- **Smart First Click**: First click never hits a mine
- **Flood Fill Algorithm**: Automatic reveal of empty areas
- **Flag System**: Long-press to flag/unflag cells
- **Win/Lose Detection**: Automatic game state management

### 🎨 **Modern UI/UX**
- **Material Design 3**: Beautiful, modern interface
- **Centered Layout**: Perfectly centered game content
- **Responsive Design**: Works on all screen sizes
- **Status Bar Safe**: Properly positioned below system UI
- **Custom Styling**: Attractive cards, buttons, and typography

### ⚡ **Animations & Effects**
- **Cell Reveal Animations**: Smooth scale effects when revealing cells
- **Flag Animations**: Bounce effect when flagging cells
- **Game Over Animations**: Animated victory/defeat messages
- **Fast Transitions**: Snappy, responsive animations (50-150ms)
- **Visual Feedback**: Every interaction has satisfying visual response

### 🔊 **Sound Effects**
- **Click Sounds**: Audio feedback for cell reveals
- **Flag Sounds**: Distinct sound for flagging actions
- **Mine Explosion**: Alert sound when hitting a mine
- **Victory Sound**: Celebration audio for winning
- **System Integration**: Uses Android's ToneGenerator for performance

### ⏱️ **Timer System**
- **Real-time Tracking**: Live timer during gameplay
- **Auto Start/Stop**: Timer starts on first click, stops on game end
- **Formatted Display**: Shows time as MM:SS format
- **Final Time**: Displays completion time when game ends
- **Reset on New Game**: Timer resets for each new game

### 📚 **Help System**
- **How to Play**: Comprehensive game rules dialog
- **Visual Instructions**: Emoji-enhanced, easy-to-understand guide
- **Modal Interface**: Clean overlay with dismiss functionality
- **Accessible Design**: Large touch targets and clear typography

## 🏗️ **Architecture**

### **Clean Architecture Pattern**
```
📁 app/src/main/java/com/example/minesweeper/
├── 🎮 game/
│   ├── Models.kt          # Game logic, Board, Cell, Difficulty
│   └── GameViewModel.kt  # State management, timer, game flow
├── 🎨 ui/
│   └── GameScreen.kt     # Compose UI components
├── 🔊 audio/
│   └── SoundManager.kt   # Sound effects management
└── MainActivity.kt       # App entry point
```

### **Key Components**

#### **Models.kt**
- `Cell`: Individual cell state (mine, revealed, flagged, adjacent count)
- `Board`: Complete game board with logic
- `Difficulty`: Game configuration (currently EASY: 9x9, 10 mines)
- **Algorithms**: Mine placement, flood reveal, win condition checking

#### **GameViewModel.kt**
- **State Management**: Board state, timer, game flow
- **Timer Logic**: Coroutine-based real-time timer
- **Game Actions**: Cell reveal, flagging, new game
- **Time Formatting**: MM:SS display format

#### **GameScreen.kt**
- **Compose UI**: Modern, responsive interface
- **Animations**: Smooth transitions and effects
- **Sound Integration**: Audio feedback for all actions
- **Help System**: Interactive rules dialog

#### **SoundManager.kt**
- **Audio System**: Android ToneGenerator integration
- **Sound Effects**: Click, flag, mine, victory sounds
- **Performance**: Lightweight, efficient audio handling

## 🚀 **Getting Started**

### **Prerequisites**
- Android Studio Hedgehog or later
- Android SDK 24+ (Android 7.0+)
- Kotlin 2.0.21+
- Jetpack Compose BOM 2024.09.00+

### **Installation**

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/minesweeper.git
   cd minesweeper
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Build and Run**
   - Sync project with Gradle files
   - Connect Android device or start emulator
   - Click "Run" or press `Shift + F10`

### **Build Requirements**
- **Compile SDK**: 36
- **Min SDK**: 24
- **Target SDK**: 36
- **Java Version**: 11

## 🎯 **How to Play**

1. **Start**: Tap any cell to begin the game
2. **Reveal**: Tap cells to reveal their contents
3. **Flag**: Long-press cells to flag/unflag potential mines
4. **Numbers**: Revealed numbers show adjacent mine count
5. **Win**: Reveal all safe cells without hitting mines
6. **Lose**: Avoid clicking on mines (💣)

### **Controls**
- **Tap**: Reveal cell
- **Long Press**: Flag/unflag cell
- **New Game**: Tap "🎮 NEW GAME" button
- **Help**: Tap "?" button for rules

## 🛠️ **Technical Details**

### **Dependencies**
```kotlin
// Core Android
implementation("androidx.core:core-ktx:1.10.1")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.2")

// Compose
implementation("androidx.activity:activity-compose:1.8.0")
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.foundation:foundation")

// Media
implementation("androidx.media3:media3-exoplayer:1.2.1")
```

### **Key Features Implementation**

#### **Timer System**
- Uses `CoroutineScope` with `Dispatchers.Main`
- Updates every 100ms for smooth display
- Automatically starts/stops based on game state

#### **Sound Effects**
- `ToneGenerator` for system-level audio
- Different tones for different actions
- Lightweight, no external audio files needed

#### **Animations**
- `Animatable` for smooth scale transitions
- `AnimatedVisibility` for fade effects
- Optimized durations (50-150ms) for responsiveness

#### **Game Logic**
- Immutable `Board` data class
- Functional approach with pure functions
- Efficient flood-fill algorithm for empty cell reveals


## 🎨 **Customization**

### **Easy Modifications**
- **Grid Size**: Change `Difficulty.EASY` values in `Models.kt`
- **Colors**: Modify color schemes in `GameScreen.kt`
- **Animations**: Adjust timing in animation specs
- **Sounds**: Customize tones in `SoundManager.kt`

### **Adding New Features**
- **Themes**: Add dark/light mode support
- **Difficulty Levels**: Extend `Difficulty` enum
- **Statistics**: Add game history tracking
- **Achievements**: Implement badge system

## 🤝 **Contributing**

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

### **Development Guidelines**
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Test on multiple screen sizes
- Ensure accessibility compliance


## 🙏 **Acknowledgments**

- **Jetpack Compose** team for the amazing UI framework
- **Material Design** for design guidelines
- **Android Developer Community** for inspiration and resources

## 📞 **Contact**

- **GitHub**: [@Robin-Kumar-rk](https://github.com/Robin-Kumar-rk)
- **Email**: robinkumarrk2005@gmail.com
- **Project Link**: [https://github.com/yourusername/minesweeper](https://github.com/Robin-Kumar-rk/Minesweeper)

---

**Made with ❤️ using Kotlin and Jetpack Compose**

*Enjoy playing Minesweeper! 🎮*
