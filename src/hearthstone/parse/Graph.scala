package hearthstone.parse

import org.apache.spark.graphx._
import scala.collection.mutable.ListBuffer

object Graph {
	
	def playCombosFromGame(game: Game): List[PlayCombo] ={
		val comboBuffer: ListBuffer[PlayCombo] = new ListBuffer[PlayCombo]
		val cardList = game.getCardList
		cardList.tail.foldLeft(cardList.head)((card1: Card, card2: Card) =>{
			comboBuffer += new PlayCombo(card1, card2, game.hero, game.didWin)
			card2
		})
		comboBuffer.toList
	}
}