package hearthstone.parse

import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.collection.mutable.ListBuffer
import com.sun.beans.decoder.FalseElementHandler
import scala.xml.Null

class JsonParser {
	
	
	//ToDo: fix Big Decimal to Int to go FAST
	def cardParse(card:String):(Card) ={
		val jCard: JsValue = Json.parse(card)
		new Card((jCard\"id").as[JsString].value, (jCard\"mana").as[JsNumber].value.toIntExact)
	}
	
	def cardParse(jCard:JsObject):(Card) ={
		new Card((jCard\"id").as[JsString].value, (jCard\"mana").as[JsNumber].value.toIntExact)
	}
	
	
	def playParse(playString: String):(Card, Int,Boolean)={
		val jPlay: JsValue = Json.parse(playString)

		val isOpponent:Boolean = (jPlay\"player").as[JsString].value.equals("opponent")
		val turn:Int = (jPlay\"turn").as[JsNumber].value.toIntExact
		val card:Card = cardParse((jPlay\"card").as[JsObject])
		(card, turn, isOpponent)
	}
	
	def playParse(jPlay: JsObject):(Card, Int,Boolean)={
		val isOpponent:Boolean = (jPlay\"player").as[JsString].value.equals("opponent")
		val turn:Int = (jPlay\"turn").as[JsNumber].value.toIntExact
		val card:Card = cardParse((jPlay\"card").as[JsObject])
		(card, turn, isOpponent)
	}
	def historyParseToPlayList(historyString:String):(List[(Card,Int)],List[(Card,Int)]) = {
		val jHistory:List[JsObject] = Json.parse(historyString).as[List[JsObject]]
		val playerListBuff:ListBuffer[(Card,Int)] = new ListBuffer[(Card,Int)]
		val opponentListBuff:ListBuffer[(Card,Int)] = new ListBuffer[(Card,Int)]
		for(jPlay<-jHistory){
			val play = playParse(jPlay)
			val toAdd= (play._1,play._2)
			if(play._3){
				opponentListBuff+= toAdd
			}
			else{
				playerListBuff+=toAdd
			}
		}
		(playerListBuff.toList,opponentListBuff.toList)
	}
	
	
	def  historyParseToTurnList(historyString:String):(List[Turn],List[Turn]) = {
		val jHistory:List[JsObject] = Json.parse(historyString).as[List[JsObject]]
		var cardListBuff:ListBuffer[Card] = new ListBuffer[Card]
		val playerTurnBuff:ListBuffer[Turn] = new ListBuffer[Turn]
		val opponentTurnBuff:ListBuffer[Turn] = new ListBuffer[Turn]
		
		var prevTurn:Int = -1
		var prevIsOpponent:Boolean = false 
		
		for(jPlay<-jHistory){
			val play = playParse(jPlay)
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
	
	def  historyParseToTurnList(jHistory:List[JsObject]):(List[Turn],List[Turn]) = {
		var cardListBuff:ListBuffer[Card] = new ListBuffer[Card]
		val playerTurnBuff:ListBuffer[Turn] = new ListBuffer[Turn]
		val opponentTurnBuff:ListBuffer[Turn] = new ListBuffer[Turn]
		
		var prevTurn:Int = -1
		var prevIsOpponent:Boolean = false 
		
		for(jPlay<-jHistory){
			val play = playParse(jPlay)
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
	
	def gameParse(game: String ) : (Game, Game) = {
		val jGame: JsValue = Json.parse(game)
		
		val playerHero:Hero.Value = getHero((jGame\"hero").as[JsString].value)
		val opponentHero:Hero.Value = getHero((jGame\"opponent").as[JsString].value)
		val playerCoin:Boolean = (jGame\"coin").as[JsBoolean].value
		val playerWin:Boolean = ((jGame\"result").as[JsString].value.equals("win"))
		//val isStandard:Boolean = ((jGame\"mode").as[JsString].value.equals("standard"))
		var rank:Int = -1
		var ll = (jGame\"rank").as[JsValue]
		if(ll == JsNull){
			rank = 0
		}
		else{	
			rank =(jGame\"rank").as[JsNumber].value.toIntExact
		}
		
		val turns:(List[Turn],List[Turn]) = historyParseToTurnList(
				(jGame\"card_history").as[List[JsObject]])
		
		
		val playerGame:Game = new Game(playerCoin, playerWin, playerHero,rank, turns._1)
		val opponentGame:Game = new Game(!playerCoin, !playerWin, opponentHero,rank, turns._1)
		(playerGame, opponentGame)
	}
}