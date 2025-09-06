package com.example.minesweeper.ui
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.minesweeper.audio.SoundManager
import com.example.minesweeper.audio.rememberSoundManager
import com.example.minesweeper.game.Board
import com.example.minesweeper.game.Cell
import com.example.minesweeper.game.Difficulty
import com.example.minesweeper.game.GameViewModel

@Composable
fun GameRoute(viewModel: GameViewModel = remember { GameViewModel() }) {
    val boardState by viewModel.board.collectAsState()
    val gameTime by viewModel.gameTime.collectAsState()
    val soundManager = rememberSoundManager()
    
    GameScreen(
        board = boardState,
        gameTime = gameTime,
        onCellTap = { row, col ->
            soundManager.playClick()
            viewModel.onCellTap(row, col)
        },
        onCellLongPress = { row, col ->
            soundManager.playFlag()
            viewModel.onCellLongPress(row, col)
        },
        onNewGame = {
            viewModel.newGame()
        },
        formatTime = viewModel::formatTime
    )
}

@Composable
private fun HowToPlayDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "🎯 How to Play",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                val rules = listOf(
                    "🎯 Goal: Find all mines without clicking on them",
                    "👆 Tap: Reveal a cell",
                    "🚩 Long Press: Flag/unflag a cell",
                    "🔢 Numbers: Show how many mines are nearby",
                    "✅ Win: Reveal all safe cells",
                    "💥 Lose: Click on a mine"
                )
                
                rules.forEach { rule ->
                    Text(
                        text = rule,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Got it! 👍")
                }
            }
        }
    }
}

@Composable
private fun HelpButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .wrapContentWidth()
            .height(56.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        )
    ) {
        Text(
            text = "?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun GameTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "💣 MINESWEEPER 💣",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun GameInfoCard(board: Board, gameTime: Long, formatTime: (Long) -> String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (board.isGameOver) {
                AnimatedVisibility(
                    visible = true,
                    enter = scaleIn(animationSpec = tween(150)) + fadeIn(animationSpec = tween(150)),
                    exit = scaleOut(animationSpec = tween(100)) + fadeOut(animationSpec = tween(100))
                ) {
                    Text(
                        text = if (board.isWin) "🎉 VICTORY! 🎉" else "💥 GAME OVER 💥",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (board.isWin) Color(0xFF4CAF50) else Color(0xFFF44336),
                        textAlign = TextAlign.Center
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Time: ${formatTime(gameTime)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InfoItem(
                        icon = "💣",
                        label = "Mines",
                        value = board.mines.toString(),
                        color = Color(0xFFF44336)
                    )
                    InfoItem(
                        icon = "🚩",
                        label = "Flags",
                        value = board.flagsPlaced.toString(),
                        color = Color(0xFF2196F3)
                    )
                    InfoItem(
                        icon = "✅",
                        label = "Revealed",
                        value = "${board.revealedSafeCells}/${board.safeCellsTotal}",
                        color = Color(0xFF4CAF50)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "⏱️ ${formatTime(gameTime)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun InfoItem(icon: String, label: String, value: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun Header(board: Board, gameTime: Long, onNewGame: () -> Unit, onShowHelp: () -> Unit, formatTime: (Long) -> String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Game info card
        GameInfoCard(board = board, gameTime = gameTime, formatTime = formatTime)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Buttons row
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Help button
            HelpButton(onClick = onShowHelp)
            
            // New game button - only takes required space
            Button(
                onClick = onNewGame,
                modifier = Modifier
                    .wrapContentWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Text(
                    text = "🎮 NEW GAME",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun GameScreen(
    board: Board,
    gameTime: Long,
    onCellTap: (Int, Int) -> Unit,
    onCellLongPress: (Int, Int) -> Unit,
    onNewGame: () -> Unit,
    formatTime: (Long) -> String
) {
    var showHelpDialog by remember { mutableStateOf(false) }
    val soundManager = rememberSoundManager()
    
    // Play sound effects for game state changes
    LaunchedEffect(board.isGameOver) {
        if (board.isGameOver) {
            if (board.isWin) {
                soundManager.playWin()
            } else {
                soundManager.playMine()
            }
        }
    }
    
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GameTitle()
                Header(
                    board = board, 
                    gameTime = gameTime,
                    onNewGame = onNewGame,
                    onShowHelp = { showHelpDialog = true },
                    formatTime = formatTime
                )
                BoardGrid(board = board, onCellTap = onCellTap, onCellLongPress = onCellLongPress)
            }
            
            // Help dialog overlay
            if (showHelpDialog) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    HowToPlayDialog(
                        isVisible = showHelpDialog,
                        onDismiss = { showHelpDialog = false }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BoardGrid(
    board: Board,
    onCellTap: (Int, Int) -> Unit,
    onCellLongPress: (Int, Int) -> Unit
) {
    val columns = board.cols
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(board.cells.flatten(), key = { it.row * board.cols + it.col }) { cell ->
            CellItem(
                cell = cell,
                onTap = { onCellTap(cell.row, cell.col) },
                onLongPress = { onCellLongPress(cell.row, cell.col) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CellItem(
    cell: Cell,
    onTap: () -> Unit,
    onLongPress: () -> Unit
) {
    val background = when {
        cell.isRevealed && cell.isMine -> Color.Red
        cell.isRevealed -> Color(0xFFB0BEC5)
        else -> Color(0xFF78909C)
    }
    val textColor = if (cell.isRevealed) Color.Black else Color.White
    val label = when {
        cell.isFlagged && !cell.isRevealed -> "🚩"
        cell.isRevealed && cell.isMine -> "💣"
        cell.isRevealed && cell.adjacentMines > 0 -> cell.adjacentMines.toString()
        else -> ""
    }

    // Animation for cell reveal
    val scale = remember { Animatable(1f) }
    val alpha = remember { Animatable(1f) }
    
    LaunchedEffect(cell.isRevealed) {
        if (cell.isRevealed) {
            // Fast reveal animation
            scale.animateTo(1.1f, animationSpec = tween(50))
            scale.animateTo(1f, animationSpec = tween(100))
        }
    }
    
    LaunchedEffect(cell.isFlagged) {
        if (cell.isFlagged) {
            // Fast flag animation
            scale.animateTo(1.2f, animationSpec = tween(75))
            scale.animateTo(1f, animationSpec = tween(75))
        }
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                alpha = alpha.value
            )
            .background(background, shape = MaterialTheme.shapes.small)
            .combinedClickable(onClick = onTap, onLongClick = onLongPress),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = cell.isRevealed || cell.isFlagged,
            enter = scaleIn(animationSpec = tween(100)) + fadeIn(animationSpec = tween(100)),
            exit = scaleOut(animationSpec = tween(50)) + fadeOut(animationSpec = tween(50))
        ) {
            Text(
                text = label, 
                color = textColor,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


