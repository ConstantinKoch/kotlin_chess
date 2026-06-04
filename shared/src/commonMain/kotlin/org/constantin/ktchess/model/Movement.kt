package org.constantin.ktchess

public fun getPossibleMoves(piece: Piece, board: Board): List<Position> {
  when (piece.type) {
    PieceType.PAWN -> return getPawnMoves(piece, board)
    PieceType.KNIGHT -> return getKnightMoves(piece, board)
    PieceType.BISHOP -> return getBishopMoves(piece, board)
    PieceType.ROOK -> return getRookMoves(piece, board)
    PieceType.QUEEN -> return getQueenMoves(piece, board)
    PieceType.KING -> return getKingMoves(piece, board)
  }
}
  
private fun getPawnMoves(piece: Piece, board: Board): List<Position> {
  var moves = mutableListOf<Position>(Position(piece.position.column, piece.position.row + if (piece.color == Color.WHITE) 1 else -1))
  moves.addAll(getPawnSpecialMoves(piece, board))
  return moves
}

private fun getPawnSpecialMoves(piece: Piece, board: Board): List<Position> {
  var moves = mutableListOf<Position>()
  // moves.add(pawnFirstMove(piece, board))
  // moves.add(pawnEnPassant(piece, board))
  // moves.add(pawnCaptureMoves(piece, board))
  return moves
}

private fun  getKnightMoves(piece: Piece, board: Board): List<Position> { return listOf() }
private fun  getBishopMoves(piece: Piece, board: Board): List<Position> { return listOf() }
private fun  getRookMoves(piece: Piece, board: Board): List<Position> { return listOf() }
private fun  getQueenMoves(piece: Piece, board: Board): List<Position> { return listOf() }
private fun  getKingMoves(piece: Piece, board: Board): List<Position> { return listOf() }
