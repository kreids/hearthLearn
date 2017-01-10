package hearthstone.parse

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd._
import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.sql.SQLContext
import org.apache.spark.graphx._
import scala.collection.mutable.ListBuffer
import org.apache.spark.graphx._
import org.apache.spark.graphx.Graph
import org.apache.spark.graphx.Graph
import org.apache.spark.graphx.Graph
import org.apache.spark.graphx.Graph


object sparkFunctions {
	
	val parser: HearthJsonParser = new HearthJsonParser
	
	def main(args: Array[String]){
		
		
		println("Started")
	 	val conf = new SparkConf().setAppName("HearthLearn")
			.setMaster("local")
	
		val sc = new SparkContext(conf)
		val sql = new SQLContext(sc)
	
		val games = getGamesFromTextFile(sc,"data/2016-11-28.json").cache()
		games.filter { game => game.rank<15 }.filter{game => game.hero.equals(Hero.SHAMAN)}.take(15).foreach { game => println(game) }

		val combos = games.flatMap { game => GraphTransforms.playCombosFromGame(game) }.cache()
		//combos.take(2).foreach(println)
		val edges:RDD[Edge[PlayCombo]] = combos.map { combo => GraphTransforms.edgeFromCombo(combo) }.cache()
		
		val graph = Graph.fromEdges(edges, "Card");
		
				
		/*val shamanCards = KMeans.cardFreqFromGames(sc, (games.filter { game => game.hero.equals(Hero.SHAMAN)}))
		shamanCards.take(10).foreach(game => println(game))
		
		val shamanDf = KMeans.dataFrameFromCardFreq(sc, sql, shamanCards)
		
		shamanDf.show(10)
		
		val shamanCardVectorized = KMeans.cardFreqVectorFromCardFreq(sc, shamanDf)
		//shamanCardVectorized.show(10)
		
		shamanCardVectorized.select("card ID Freqs").show(10,false)*/
		
	}
	
	def getGamesFromTextFile(sc:SparkContext, pathToFile:String): RDD[Game] ={
		val textFile = sc.textFile("data/2016-11-28.json")
		textFile.flatMap{ game => parser.inputParse(game) }
	}
	
	def getCardListsFromGame(games: RDD[Game]):RDD[List[String]] ={
		games.map { game => game.getCardIdList }
	}
  
  
}