package hearthstone.parse

//import play.api.libs.json._
//import play.api.libs.functional.syntax._
//import scala.collection.mutable.ListBuffer
//import com.sun.beans.decoder.FalseElementHandler
//import scala.xml.Null

import scala.collection.mutable.ListBuffer
import org.codehaus.jackson
import com.fasterxml.jackson.databind.ObjectMapper
import scala.collection.SortedMap.Default
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.codehaus.jackson.JsonParser
import org.codehaus.jackson.JsonFactory
import com.fasterxml.jackson.databind.JsonNode

class HearthJsonParser {
	
	val objMapper = new ObjectMapper()
	
	def cardParse(card:String):(Card)={
		val root = objMapper.readTree(card)
		cardParse(root)
	}
	def cardParse(root:JsonNode):(Card) = {
		new Card(root.path("id").asText(),
				root.path("mana").asInt(-1))
	}
	
	def playParse(play: String): (Card, Int, Boolean) ={
		val root = objMapper.readTree(play)
		playParse(root)
	}
	
	def playParse(root: JsonNode):(Card, Int, Boolean) ={
		val isOpponent:Boolean = root.path("player").asText().equals("opponent")
		val turn:Int = root.path("turn").asInt()
		val card:Card = cardParse(root.path("card"))
		(card, turn, isOpponent)
	}
	
	def historyParseToTurnList(root:JsonNode):(List[Turn],List[Turn]) = {
		var i:Int = 0
		val playerTurnBuff:ListBuffer[Turn] = new ListBuffer[Turn]
		val opponentTurnBuff:ListBuffer[Turn] = new ListBuffer[Turn]
		var cardListBuff:ListBuffer[Card] = new ListBuffer[Card]

		
		var prevTurn:Int = -1
		var prevIsOpponent:Boolean = false 
		
		while((root.get(i)!= null)){
			val play = playParse(root.get(i))
			i = i+1
			if(prevTurn == -1){
				prevIsOpponent = play._3
			}
			if((prevTurn != -1)&&
					(play._2 != prevTurn || play._3 != prevIsOpponent)){
				if(prevIsOpponent){
					opponentTurnBuff += new Turn(prevTurn,cardListBuff.toList) 
				}
				else{
					playerTurnBuff += new Turn(prevTurn,cardListBuff.toList) 
				}
				cardListBuff = new ListBuffer[Card]
			}
			cardListBuff += play._1
			prevTurn = play._2
			prevIsOpponent = play._3
		}
		if(prevIsOpponent){
			opponentTurnBuff += new Turn(prevTurn,cardListBuff.toList)
		}
		else{
			playerTurnBuff += new Turn(prevTurn,cardListBuff.toList)
		}
		
		(playerTurnBuff.toList,opponentTurnBuff.toList)
		
	}

	
	def historyParseToTurnList(historyString:String):(List[Turn],List[Turn]) = {
		val root = objMapper.readTree(historyString)
		
		historyParseToTurnList(root);
	}
	
	def getHero(heroString: String) :Hero.Value ={
		if(heroString.equals("Druid")){
			Hero.DRUID
		}
		else if(heroString.equals("Hunter")){
			Hero.HUNTER
		}
		else if(heroString.equals("Mage")){
			Hero.MAGE
		}
		else if(heroString.equals("Paladin")){
			Hero.PALADIN
		}
		else if(heroString.equals("Priest")){
			Hero.PRIEST
		}
		else if(heroString.equals("Rogue")){
			Hero.ROGUE
		}
		else if(heroString.equals("Shaman")){
			Hero.SHAMAN
		}
		else if(heroString.equals("Warlock")){
			Hero.WARLOCK
		}
		else if(heroString.equals("Warrior")){
			Hero.WARRIOR
		}
		else{	
			null
		}
	}
	
	def gameParse(root:JsonNode): (Game, Game) = {
		val playerHero:Hero.Value = getHero(root.get("hero").asText())
		val opponentHero:Hero.Value = getHero(root.get("opponent").asText())
		val playerCoin:Boolean = root.get("coin").asBoolean()
		val playerWin:Boolean = root.get("result").asBoolean()
		var rank:Int = root.get("rank").asInt(0)
		val turns = historyParseToTurnList(root.get("card_history"))
		val playerGame:Game = new Game(playerCoin, playerWin, playerHero,rank, turns._1)
		val opponentGame:Game = new Game(!playerCoin, !playerWin, opponentHero,rank, turns._1)
		(playerGame, opponentGame)
	}
	
	def gameParse(game:String): (Game, Game) = {
		val root = objMapper.readTree(game)
		gameParse(root)
	}
	
	def inputParse(input:String): List[Game]={
		val root = objMapper.readTree(input)
		val games = root.get("games")
		var i:Int = 0
		
		var retBuf:ListBuffer[Game] = new ListBuffer[Game]
		while ((games.get(i)!=null)){
			val gameTuple:(Game,Game) = gameParse(games.get(i))
			i =i +1
			retBuf += gameTuple._1
			retBuf += gameTuple._2
		}
		retBuf.toList
	}
	
	/*
	def inputStringParse(input: String) : List[Game] = {
		var retBuf:ListBuffer[Game] = new ListBuffer[Game]
		
		val jIn: JsValue = Json.parse(input)
		
		val jArr:List[JsObject] = Json.parse(input).as[List[JsObject]]
		
		for(jGame<-jArr){
			val games = gameParse(jGame)
			retBuf += games._1
			retBuf += games._2
		}
		retBuf.toList
*/}