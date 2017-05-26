package hearthstone.parse

import scala.collection.mutable.ListBuffer

class Turn(xTurn: Int, xPlays: List[Card]) extends java.io.Serializable {
	val turn = xTurn
	val plays = xPlays
	
	def getCardListBuffer: ListBuffer[Card] ={
		val cardBuff = new ListBuffer[Card]
		plays.foreach { play =>
			cardBuff += play }
		cardBuff
	}
	def getCardList = 
		getCardListBuffer.toList
	
	def getCardIdListBuffer: ListBuffer[String] ={
		val cardIdBuff = new ListBuffer[String]
		plays.foreach { play =>
			cardIdBuff += play.id }
		cardIdBuff
	}
	def getCardIdList = 
		getCardIdListBuffer.toList
	
	def toStringBuilder: StringBuilder ={
		val sb = new StringBuilder
		sb.append("{turn: ")
		sb.append(turn)
		sb.append(", plays:[")
		plays.foreach { play => 
			sb.append(play.toStringBuilder)
			sb.append(", ")}
		sb.delete(sb.length-3, sb.length-1)
		sb.append("]")

	}
	
	override def toString: String =
		toStringBuilder.toString
}