package hearthstone.parse

import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.collection.mutable.ListBuffer
import com.sun.beans.decoder.FalseElementHandler

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
	
	def turnParse(turn:String, isOppenent:Boolean,number:Int ) : Turn = {
		val jTurn: JsValue = Json.parse(turn)
		
		new Turn(1, null)
	}
}