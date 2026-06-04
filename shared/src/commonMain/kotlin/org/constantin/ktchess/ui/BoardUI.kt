package org.constantin.ktchess.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import org.constantin.ktchess.Board
import org.constantin.ktchess.Position
import org.constantin.ktchess.Color as PieceColor

public class BoardUI() {
    var board: Board = Board(this) // Erstellen eines neuen Schachbretts

    @Composable
    public fun drawBoard() {
        MaterialTheme {
            for (row in 0..7) {
                var isBlack = if (row % 2 == 0) false else true
                Row {
                    for (column in 0..7) {
                        val tile = Tile(isBlack, row, column)

                        board.connectTileAndPosition(tile)
                        tile.drawTile(board.tiles[board.getListPositionFromChessNotation(tile.toChessNotation())].first)
                        isBlack = !isBlack
                    }
                }

            }
        }
    }
}

class Tile(val isBlack: Boolean, val row: Int, val column: Int) {

    @Composable
    fun drawTile(position: Position? = null) {
        Box(
            modifier = Modifier
                .size(100.dp) // Größe der Box
                .background(if (this.isBlack) Color.DarkGray else Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = position?.piece?.type?.toString() ?: "",
                color = if (position?.piece?.color == PieceColor.WHITE) Color.White else Color.Black
            )
            println("state: ${position?.piece?.id}, position: ${position?.toChessNotation()}, piece: ${position?.piece}")
        }
    }


    fun toChessNotation(): String {
        val file = ('a' + column) // Spalten werden von 'a' bis 'h' bezeichnet
        val rank = 8 - row // Reihen werden von 1 bis 8 bezeichnet, von unten nach oben
        return "$file$rank"
    }
}