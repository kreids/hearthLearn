package hearthstone.parse


import Hero._;
import scala.collection.mutable.ListBuffer



case class Game(hasCoin: Boolean, didWin: Boolean, hero: Hero.Value, rank:Int, turns: List[Turn])extends java.io.Serializable {

	
	def getCardListBuff:ListBuffer[Card] ={
		val cardBuff = new ListBuffer[Card]
		turns.foreach{turn =>
			turn.getCardList
			.foreach { card =>cardBuff+= card }
		}
		cardBuff
	}
	def getCardList:List[Card] = {
		getCardListBuff.toList
	}
	
	def getCardIdListBuff: ListBuffer[String] ={
		val cardIdBuff = new ListBuffer[String]
		turns.foreach{turn =>
			turn.getCardIdList
			.foreach { cardId =>cardIdBuff+= cardId }
		}
		cardIdBuff
	}
	
	def getCardIdList: List[String] ={
		getCardIdListBuff.toList
	}
	
	  def toStringBuilder: StringBuilder ={
		val sb = new StringBuilder
		sb.append("hero: ")
		sb.append(hero)
		sb.append(", hasCoin: ") 
		sb.append(hasCoin)
		sb.append(", didWin: ")
		sb.append(didWin)
		sb.append(", rank: ")
		sb.append(rank)
		sb.append(",\nturns: [")
		turns.foreach { turn => 
			sb.append(turn.toStringBuilder) 
			sb.append(", ") }
    	sb.delete(sb.length-3, sb.length-1)
    	sb.append("]\n")
	}
	override def toString: String=
		toStringBuilder.toString
		
	
}