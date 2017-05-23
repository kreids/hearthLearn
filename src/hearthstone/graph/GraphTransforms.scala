package hearthstone.graph

import org.apache.spark.graphx._
import scala.collection.mutable.ListBuffer
import org.apache.spark.graphx.Edge
import org.apache.spark.rdd._
import scala.util.MurmurHash
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import hearthstone.parse.Card
import hearthstone.parse.Game
import hearthstone.parse.PlayCombo
import javax.validation.constraints.Null
import breeze.linalg.Axis._0



object GraphTransforms {
	
	
	
	def chiSquaredIndependenceFromTriplet(YY: Long,YB:Long, YA:Long, T: Long): Double = {
		val NB = T - YB
    	val NA = T - YA
    	val YN = YA - YY
    	val NY = YB - YY
    	val NN = T - NY - YN - YY
    	val inner = math.abs(YY * NN - YN * NY) - T / 2.0
    	T * math.pow(inner, 2) / (YA * NA * YB * NB)
	}

	
	def graphFromVerteciesAndEdges(verts:RDD[(VertexId,(Card,Int))], 
			edges:RDD[Edge[PlayCombo]]):Graph[(Card,Int),PlayCombo]={
	 	Graph(verts,edges).groupEdges((combo1, combo2)=> 
			new PlayCombo(combo1.firstCard, combo1.secondCard, combo1.hero,combo1.wins+combo2.wins, combo1.losses+combo2.losses))
	}
	
	def verteciesFromRDD( games:RDD[Game]):(RDD[(VertexId,(Card,Int))], Long) = {
		val count  = games.count()
	 	val vertecies:RDD[(VertexId,(Card,Int))] =games.flatMap { game => game.getCardList.map { card => vertexFromCard(card) } }
	 	(vertecies.reduceByKey((vertexA,vertexB) => (vertexA._1,vertexA._2+vertexB._2)), count)
	}
	
	def vertexFromCard(card: Card):(VertexId, (Card, Int)) = {
		(MurmurHash.stringHash(card.id),(card,1))
	}
	
	def edgeFromCombo(combo:PlayCombo): Edge[PlayCombo] ={
		new Edge(MurmurHash.stringHash(combo.firstCard.id),
				MurmurHash.stringHash(combo.secondCard.id),
				combo)
	}
	
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
	
	
	
	
}