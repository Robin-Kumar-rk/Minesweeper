package com.example.minesweeper.game

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val _board: MutableStateFlow<Board> = MutableStateFlow(
        Board(rows = Difficulty.EASY.rows, cols = Difficulty.EASY.cols, mines = Difficulty.EASY.mines)
    )
    val board: StateFlow<Board> = _board.asStateFlow()
    
    private val _gameTime: MutableStateFlow<Long> = MutableStateFlow(0L)
    val gameTime: StateFlow<Long> = _gameTime.asStateFlow()
    
    private val _isTimerRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()
    
    private var timerJob: kotlinx.coroutines.Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    fun onCellTap(row: Int, col: Int) {
        val currentBoard = _board.value
        if (!currentBoard.isInitialized && !currentBoard.isGameOver) {
            startTimer()
        }
        _board.value = currentBoard.reveal(row, col)
        
        // Stop timer if game is over
        if (_board.value.isGameOver) {
            stopTimer()
        }
    }

    fun onCellLongPress(row: Int, col: Int) {
        _board.value = _board.value.toggleFlag(row, col)
    }

    fun newGame() {
        stopTimer()
        _gameTime.value = 0L
        _isTimerRunning.value = false
        _board.value = Board(rows = Difficulty.EASY.rows, cols = Difficulty.EASY.cols, mines = Difficulty.EASY.mines)
    }
    
    private fun startTimer() {
        if (_isTimerRunning.value) return
        
        _isTimerRunning.value = true
        timerJob = scope.launch {
            while (_isTimerRunning.value) {
                delay(100)
                _gameTime.value += 100
            }
        }
    }
    
    private fun stopTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
        timerJob = null
    }
    
    fun formatTime(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}


