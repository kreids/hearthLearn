package hearthstone.parse

import org.apache.spark.graphx._
import scala.collection.mutable.ListBuffer
import org.apache.spark.graphx.Edge

import scala.util.MurmurHash


object GraphTransforms {
	
	def playCombosFromGame(game: Game): List[PlayCombo] ={
		val comboBuffer: ListBuffer[PlayCombo] = new ListBuffer[PlayCombo]
		val cardList = game.getCardList
		var wins:Int = 0
		var losses:Int = 0
		if(game.didWin){
			wins = 1
		}
		else {
			losses = 1 
		}
		cardList.tail.foldLeft(cardList.head)((card1: Card, card2: Card) =>{
			comboBuffer += new PlayCombo(card1, card2, game.hero, wins, losses)
			card2
		})
		comboBuffer.toList
	}
	def edgeFromCombo(combo:PlayCombo): Edge[PlayCombo] ={
		new Edge(MurmurHash.stringHash(combo.firstCard.id),
				MurmurHash.stringHash(combo.secondCard.id),
				combo)
	}
}