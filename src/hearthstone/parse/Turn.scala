package hearthstone.parse

class Turn(xTurn: Int, xPlays: List[Card]) extends java.io.Serializable {
	val turn = xTurn
	val plays = xPlays
	
	override def toString: String =
		"{turn:"+turn+", plays:["+plays.foreach(print)+"]}"
}