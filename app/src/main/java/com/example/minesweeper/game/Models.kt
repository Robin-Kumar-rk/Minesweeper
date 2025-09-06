package com.example.minesweeper.game

import kotlin.random.Random

data class Cell(
    val row: Int,
    val col: Int,
    val isMine: Boolean = false,
    val isRevealed: Boolean = false,
    val isFlagged: Boolean = false,
    val adjacentMines: Int = 0
)

enum class Difficulty(val rows: Int, val cols: Int, val mines: Int) {
    EASY(rows = 9, cols = 9, mines = 10)
}

data class Board(
    val rows: Int,
    val cols: Int,
    val mines: Int,
    val isInitialized: Boolean = false,
    val isGameOver: Boolean = false,
    val isWin: Boolean = false,
    val flagsPlaced: Int = 0,
    val revealedSafeCells: Int = 0,
    val cells: List<List<Cell>> = List(rows) { r ->
        List(cols) { c -> Cell(row = r, col = c) }
    }
) {
    val safeCellsTotal: Int = rows * cols - mines

    fun toggleFlag(row: Int, col: Int): Board {
        if (isGameOver) return this
        val target = cells[row][col]
        if (target.isRevealed) return this

        val updated = updateCell(row, col) { it.copy(isFlagged = !it.isFlagged) }
        val newFlags = flagsPlaced + if (target.isFlagged) -1 else 1
        return copy(cells = updated, flagsPlaced = newFlags).checkWinCondition()
    }

    fun reveal(row: Int, col: Int): Board {
        if (isGameOver) return this
        val firstRevealBoard = if (!isInitialized) initializeBoard(firstClickRow = row, firstClickCol = col) else this
        val target = firstRevealBoard.cells[row][col]
        if (target.isFlagged || target.isRevealed) return firstRevealBoard

        if (target.isMine) {
            // Reveal all mines and mark game over
            val revealedAll = firstRevealBoard.mapCells { cell ->
                if (cell.isMine) cell.copy(isRevealed = true) else cell
            }
            return firstRevealBoard.copy(cells = revealedAll, isGameOver = true, isWin = false)
        }

        // Flood reveal for empty cells
        val (newCells, newlyRevealedCount) = floodReveal(firstRevealBoard.cells, row, col)
        val totalRevealed = firstRevealBoard.revealedSafeCells + newlyRevealedCount
        return firstRevealBoard.copy(
            cells = newCells,
            revealedSafeCells = totalRevealed
        ).checkWinCondition()
    }

    fun reset(newRows: Int = rows, newCols: Int = cols, newMines: Int = mines): Board {
        return Board(rows = newRows, cols = newCols, mines = newMines)
    }

    private fun initializeBoard(firstClickRow: Int, firstClickCol: Int): Board {
        val placed = placeMinesAvoiding(firstClickRow, firstClickCol)
        val withCounts = computeAdjacency(placed)
        return copy(
            cells = withCounts,
            isInitialized = true
        )
    }

    private fun placeMinesAvoiding(avoidRow: Int, avoidCol: Int): List<List<Cell>> {
        val total = rows * cols
        val allIndices = (0 until total).toMutableList()
        val avoidIndex = avoidRow * cols + avoidCol
        allIndices.remove(avoidIndex)

        // Shuffle and pick mines
        repeat(3) { allIndices.shuffle(Random.Default) }
        val mineIndices = allIndices.take(mines).toSet()

        return List(rows) { r ->
            List(cols) { c ->
                val idx = r * cols + c
                cells[r][c].copy(isMine = idx in mineIndices)
            }
        }
    }

    private fun computeAdjacency(grid: List<List<Cell>>): List<List<Cell>> {
        return List(rows) { r ->
            List(cols) { c ->
                val isMine = grid[r][c].isMine
                if (isMine) grid[r][c] else {
                    val count = neighborsOf(r, c).count { (nr, nc) -> grid[nr][nc].isMine }
                    grid[r][c].copy(adjacentMines = count)
                }
            }
        }
    }

    private fun floodReveal(grid: List<List<Cell>>, startRow: Int, startCol: Int): Pair<List<List<Cell>>, Int> {
        val queue: ArrayDeque<Pair<Int, Int>> = ArrayDeque()
        var newGrid = grid
        var revealedCount = 0
        fun revealCell(r: Int, c: Int) {
            val cell = newGrid[r][c]
            if (cell.isRevealed || cell.isFlagged) return
            newGrid = updateCell(newGrid, r, c) { it.copy(isRevealed = true) }
            revealedCount += if (!cell.isMine) 1 else 0
        }

        revealCell(startRow, startCol)
        if (newGrid[startRow][startCol].adjacentMines == 0) {
            queue.add(startRow to startCol)
        }

        while (queue.isNotEmpty()) {
            val (r, c) = queue.removeFirst()
            for ((nr, nc) in neighborsOf(r, c)) {
                val neighbor = newGrid[nr][nc]
                if (neighbor.isRevealed || neighbor.isFlagged || neighbor.isMine) continue
                revealCell(nr, nc)
                if (newGrid[nr][nc].adjacentMines == 0) queue.add(nr to nc)
            }
        }
        return newGrid to revealedCount
    }

    private fun neighborsOf(r: Int, c: Int): List<Pair<Int, Int>> {
        val result = ArrayList<Pair<Int, Int>>(8)
        for (dr in -1..1) for (dc in -1..1) {
            if (dr == 0 && dc == 0) continue
            val nr = r + dr
            val nc = c + dc
            if (nr in 0 until rows && nc in 0 until cols) result.add(nr to nc)
        }
        return result
    }

    private fun updateCell(r: Int, c: Int, transform: (Cell) -> Cell): List<List<Cell>> {
        return updateCell(cells, r, c, transform)
    }

    private fun mapCells(mapper: (Cell) -> Cell): List<List<Cell>> {
        return List(rows) { r ->
            List(cols) { c -> mapper(cells[r][c]) }
        }
    }

    private fun updateCell(source: List<List<Cell>>, r: Int, c: Int, transform: (Cell) -> Cell): List<List<Cell>> {
        return List(rows) { rowIndex ->
            if (rowIndex == r) {
                List(cols) { colIndex ->
                    if (colIndex == c) transform(source[rowIndex][colIndex]) else source[rowIndex][colIndex]
                }
            } else {
                source[rowIndex]
            }
        }
    }

    private fun checkWinCondition(): Board {
        if (isGameOver) return this
        val hasWon = revealedSafeCells >= safeCellsTotal
        return if (hasWon) {
            val revealedAll = mapCells { cell ->
                if (cell.isMine) cell else cell.copy(isRevealed = true)
            }
            copy(cells = revealedAll, isGameOver = true, isWin = true)
        } else this
    }
}


