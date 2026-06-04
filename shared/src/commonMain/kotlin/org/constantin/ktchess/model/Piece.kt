package org.constantin.ktchess

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Represents a chess piece with its type, color, position and unique id
class Piece {
    // id = color + type + number (if needed)
    // example: "wP1" (white pawn 1), "bK" (black king)
    val id: String

    val type: PieceType
    val color: Color

    var position: Position

    // constructor for creating piece from id
    constructor(id: String) {
        this.id = id

        val arr = id.toCharArray()
        this.color = if (arr[0] == 'w') Color.WHITE else Color.BLACK
        if (arr.size == 2) {
            this.type = if (arr[1] == 'K') PieceType.KING else PieceType.QUEEN
            this.position =
                Position(if (this.type == PieceType.QUEEN) 3 else 4, if (this.color == Color.WHITE) 0 else 7)
        } else if (arr.size == 3) {
            this.type = when (arr[1]) {
                'P' -> PieceType.PAWN
                'K' -> PieceType.KNIGHT
                'B' -> PieceType.BISHOP
                'R' -> PieceType.ROOK
                else -> throw IllegalArgumentException("Invalid piece id: $id")
            }
            this.position = getStartPosition(this.type, this.color, arr[2])
        } else {
            throw IllegalArgumentException("Invalid piece id: $id")
        }
    }

    // constructor for creating piece from start position
    constructor(startPosition: Position) {
        this.position = startPosition
        startPosition.piece = this // place piece on its start position
        var col: Char

        if (startPosition.row > 2) {
            this.color = Color.WHITE
            col = 'w'
        } else {
            this.color = Color.BLACK
            col = 'b'
        }

        val (type, charChar, column) = if (startPosition.row == 1 || startPosition.row == 6)
            Triple(PieceType.PAWN, 'P', (startPosition.column + 1))
        else getTypeByColumn(startPosition.column)

        this.type = type
        this.id = "$col$charChar${column ?: ""}"
    }

    public override fun toString(): String {
        return "Piece($id)"
    }

    // get start position based on piece type, color and column (1-8)
    private fun getStartPosition(type: PieceType, color: Color, columnChar: Char): Position {
        var row = if (color == Color.WHITE) 0 else 7
        if (type == PieceType.PAWN) {
            row = if (color == Color.WHITE) 1 else 6
        }
        var column =
            if (columnChar in '0'..'9') columnChar.toInt() - 1 else throw IllegalArgumentException("Invalid column character: $columnChar")

        return Position(column, row)
    }

    // get piece type, column char and number based on column
    private fun getTypeByColumn(column: Int): Triple<PieceType, Char, Int?> {
        return when (column) {
            0, 7 -> Triple(PieceType.ROOK, 'R', column + 1)
            1, 6 -> Triple(PieceType.KNIGHT, 'N', column + 1)
            2, 5 -> Triple(PieceType.BISHOP, 'B', column + 1)
            3 -> Triple(PieceType.QUEEN, 'Q', null)
            4 -> Triple(PieceType.KING, 'K', null)
            else -> throw IllegalArgumentException("Invalid column: $column")
        }
    }

    // move piece to new position
    fun moveTo(newPosition: Position) {
        this.position.piece = null // remove piece from old position
        this.position = newPosition
        if (this.type == PieceType.PAWN && (newPosition.row == 0 || newPosition.row == 7)) this.promote() // promote pawn if it reaches the end of the board

        newPosition.piece = this // place piece on new position
    }

    // promote pawn
    private fun promote() {
        if (this.type != PieceType.PAWN) throw IllegalArgumentException("Only pawns can be promoted")

        // this.type = PieceType.QUEEN // for simplicity, we will always promote to queen
    }
}

enum class Color { WHITE, BLACK }
enum class PieceType { PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING }

public class Position(val column: Int, val row: Int, piece: Piece? = null) {
    var piece by mutableStateOf(piece)

    init {
        require(column in 0..7) { "Column must be between 0 and 7" }
        require(row in 0..7) { "Row must be between 0 and 7" }
    }

    public override fun toString(): String {
        return "this is position $column, $row with piece ${piece?.id ?: "none"}"
    }

    public fun toChessNotation(): String {
        val file = ('a' + column) // Spalten werden von 'a' bis 'h' bezeichnet
        val rank = 8 - row // Reihen werden von 1 bis 8 bezeichnet, von unten nach oben
        return "$file$rank"
    }

}