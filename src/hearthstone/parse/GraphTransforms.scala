package hearthstone.parse

import org.apache.spark.graphx._
import scala.collection.mutable.ListBuffer
import org.apache.spark.graphx.Edge
import org.apache.spark.rdd._

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
	def graphFromEdges(edges:RDD[Edge[PlayCombo]]): Graph[String,PlayCombo] = {
		Graph.fromEdges(edges, "Card").groupEdges((combo1, combo2)=> 
			new PlayCombo(combo1.firstCard, combo1.secondCard, combo1.hero,combo1.wins+combo2.wins, combo1.wins+combo2.wins))
	}
}