package org.constantin.ktchess

import org.constantin.ktchess.ui.BoardUI
import org.constantin.ktchess.ui.Tile

public class Board(boardUI: BoardUI) {
    var tiles: MutableList<Pair<Position, Tile?>> // List of all positions and their corresponding tiles (if any)

    init {
        this.tiles = mutableListOf<Pair<Position, Tile?>>()

        for (row in 0..7) {
            for (column in 0..7) {
                val currentPosition = Position(column, row)
                var figure: Boolean = row in 0..1 || row in 6..7
                this.tiles.add(Pair(currentPosition, null))
                if (figure) {
                    var piece = Piece(currentPosition)
                }
                println("Square: $column, $row, Position: ${this.tiles.last().toString()}")
            }
        }
    }

    fun getListPositionFromChessNotation(notation: String): Int {
        val file = notation[0] // 'a' bis 'h'
        val rank = notation[1] // '1' bis '8'

        val column = file - 'a' // Umwandlung von 'a'-'h' zu 0-7
        val row = 8 - (rank - '0') // Umwandlung von '1'-'8' zu 7-0

        return this.tiles.indexOfFirst { (pos, _) -> pos.toChessNotation() == notation }
    }

    // fun getPositionFromTileList

    fun connectTileAndPosition(tile: Tile) {
        val position = getListPositionFromChessNotation(tile.toChessNotation())
        if (position == -1) throw IllegalArgumentException("Invalid tile position: ${tile.toChessNotation()}")
        this.tiles[position] = Pair(this.tiles[position].first, tile)
        println("Connecting tile at ${tile.toChessNotation()} to position ${this.tiles[position]?.first?.toChessNotation()}")
    }
}