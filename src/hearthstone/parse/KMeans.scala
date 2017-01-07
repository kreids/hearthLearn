package hearthstone.parse

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame
import org.apache.spark.ml.feature.CountVectorizer
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row

object KMeans {
	
	def cardFreqFromGames(sc:SparkContext, games:RDD[Game]):
	RDD[(Hero.Value,List[String])] ={
		games.map { game => (game.hero, game.getCardIdList) }
	}
	
	def dataFrameFromCardFreq(sc: SparkContext,sql:SQLContext, 
			cardFreq:RDD[(Hero.Value,List[String])]):DataFrame ={
		
		val schemaString = "cards"
		val schemaFields =  StructField("cards", StringType, nullable = true)
		
		val schema = StructType(Seq(
				StructField("hero",StringType,false),
				StructField("card id",ArrayType(StringType,false),false)
				))
		//cardFreq.take(10).foreach(println)
		val rows = cardFreq.map(freq=> Row(freq._1.toString(),freq._2))
		
		sql.createDataFrame(rows, schema)
	}
	
	def cardFreqVectorFromCardFreq(sc: SparkContext, cardDf: DataFrame)
	:DataFrame = {
		
		
		
		val countVectorizer = new CountVectorizer()
		.setInputCol("card id").setOutputCol("card ID Freqs")
		
		val idVocabModel = countVectorizer.fit(cardDf)
		val idFreqModel = idVocabModel.transform(cardDf)
		idVocabModel.vocabulary.foreach {word=> print(word +", ")}		
		
		idFreqModel
	}

	
  
}