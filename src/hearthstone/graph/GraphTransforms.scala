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
	
	def graphFromVerteciesAndEdges(verts:RDD[(VertexId,(Card,Int))], 
			edges:RDD[Edge[PlayCombo]]):Graph[(Card,Int),PlayCombo]={
	 	Graph(verts,edges).groupEdges((combo1, combo2)=> 
			new PlayCombo(combo1.firstCard, combo1.secondCard, combo1.hero,combo1.wins+combo2.wins, combo1.losses+combo2.losses))
	}
	
	def verteciesFromRDD( games:RDD[Game]):RDD[(VertexId,(Card,Int))] = {
	 	val vertecies:RDD[(VertexId,(Card,Int))] =games.flatMap { game => game.getCardList.map { card => vertexFromCard(card) } }
	 	vertecies.reduceByKey((vertexA,vertexB) => (vertexA._1,vertexA._2+vertexB._2))
	}
	
	def vertexFromCard(card: Card):(VertexId, (Card, Int)) = {
		(MurmurHash.stringHash(card.ID),(card,1))
	}
	def graphFromEdges(edges:RDD[Edge[PlayCombo]]): Graph[String,PlayCombo] = {
		Graph.fromEdges(edges, "Card").groupEdges((combo1, combo2)=> 
			new PlayCombo(combo1.firstCard, combo1.secondCard, combo1.hero,combo1.wins+combo2.wins, combo1.losses+combo2.losses))
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
	def edgeFromCombo(combo:PlayCombo): Edge[PlayCombo] ={
		new Edge(MurmurHash.stringHash(combo.firstCard.id),
				MurmurHash.stringHash(combo.secondCard.id),
				combo)
	}
	
	
	//type mismatch; found : org.apache.spark.rdd.RDD[((org.apache.spark.graphx.VertexId, Array[org.apache.spark.graphx.Edge[hearthstone.parse.PlayCombo]]), Int)] (which expands to) org.apache.spark.rdd.RDD[((Long, Array[org.apache.spark.graphx.Edge[hearthstone.parse.PlayCombo]]), Int)] required: org.apache.spark.graphx.VertexRDD[String]
	def collectSampleComboStats(sc: SparkContext,sql:SQLContext,graph:Graph[String,PlayCombo])={
		def ttt[T](v:T) =v	
		var stepTreelets= graph.ops.collectEdges(EdgeDirection.Out)
		//stepTreelets.take(1).foreach(x => println(x._2.foreach { x => println(x.attr.wins +" "+ x.attr.secondCard) }))
		var treeletStats=stepTreelets.map(treelet => (treelet, treelet._2.map {x => x.attr.wins + x.attr.losses }.sum))
		//treelets2.take(4).foreach(x=>println(x._1._2.foreach {y=> println(y+" "+ x)}))
		
		var flatCardStats= treeletStats.flatMap(x=>x._1._2.map { y => (y.attr.hero,y.attr.firstCard,y.attr.secondCard,y.attr.wins, y.attr.losses,x._2) })
		
		
		flatCardStats.take(10).foreach {println}
		//var statsDF=sql.createDataFrame(treeletStats)
		//statsDF.take(10).foreach {println}
	}
}