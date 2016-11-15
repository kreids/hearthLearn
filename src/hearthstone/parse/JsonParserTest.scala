package hearthstone.parse

import org.scalatest.FunSpec
import org.scalatest.BeforeAndAfter


class JsonParserTest extends FunSpec with BeforeAndAfter{
	private final val ANY_VALID_CARD:String = "{\"id\":\"BRM_013\",\"name\":\"Quick Shot\",\"mana\":2}"
	private final val ANY_PLAY:String = "{\"player\":\"opponent\",\"turn\":3,\"card\":{\"id\":\"CS2_034\",\"name\":\"Fireblast\",\"mana\":2}}"
	private final val ANY_VALID_CARD_HISTORY_SPANNING_TURNS_AFTER_TURN_ONE = 
		"[{\"player\":\"opponent\",\"turn\":2,\"card\":{\"id\":\"OG_303\",\"name\":\"Cult Sorcerer\",\"mana\":2}},"+
		"{\"player\":\"me\",\"turn\":2,\"card\":{\"id\":\"CS2_101\",\"name\":\"Reinforce\",\"mana\":2}},"+
		"{\"player\":\"opponent\",\"turn\":3,\"card\":{\"id\":\"CS2_034\",\"name\":\"Fireblast\",\"mana\":2}},"+
		"{\"player\":\"opponent\",\"turn\":3,\"card\":{\"id\":\"NEW1_012\",\"name\":\"Mana Wyrm\",\"mana\":1}}]"
	
	var objectUnderTest: JsonParser = new JsonParser
	
	
	describe("cardParse"){
		describe("when called on any card"){
			val parsedCard: Card= objectUnderTest.cardParse(ANY_VALID_CARD)
			it("returns the correct id"){
				assertCard("BRM_013", 2, parsedCard)
			}
		}
	}
	
	describe("playParse"){
		describe("when called on any play"){
			val playParse = objectUnderTest.playParse(ANY_PLAY)
			it("Returns the correct Play"){
				assertPlay("CS2_034",2,3,true,playParse)
			}
			it("Returns the correct card"){
				val parsedCard = playParse._1
				assertString("CS2_034", parsedCard.id, "ID")
				assertInt(2, parsedCard.mana, "MANA")
			}
			it("Returns the correct turn"){
				assertInt(3,playParse._2,"TURN")
			}
			it("Returns the correct opponent"){
				assertTrue(playParse._3, "IS OPPONENT")
			}
		}
	}
	
	describe("historyParseToPlayList"){
		describe("when called on any turn history spanning turns after turn 1"){
			val historyParse = objectUnderTest.historyParseToPlayList(ANY_VALID_CARD_HISTORY_SPANNING_TURNS_AFTER_TURN_ONE)
			
		}
	}
	
	def assertPlay(expectedId:String,expectedMana:Int,expectedTurn:Int,expectedIsOpponent:Boolean, parsedTurn:(Card,Int,Boolean)){
		assertCard(expectedId, expectedMana, parsedTurn._1)
		assertInt(expectedTurn,parsedTurn._2,"TURN")
		assertBoolean(expectedIsOpponent,parsedTurn._3, "IS OPPONENT")
		
	}
	
	def assertBoolean(expected:Boolean,actual:Boolean,paramName:String){
		assert(expected.equals(actual), "Failure mathing" + paramName +". Expected: "+ expected +" Actual: "+ actual)
	}
	
	def assertCard(expectedId:String,expectedMana:Int,parsedCard:Card){
		assertString(expectedId, parsedCard.id, "ID")
		assertInt(expectedMana, parsedCard.mana, "MANA")
	}
	
	def assertString(expected: String, actual: String, paramName:String){
		assert(expected.equals(actual), "Failure mathing" + paramName +". Expected: "+ expected +" Actual: "+ actual)
	}
	def assertInt(expected: Int, actual: Int, paramName:String){
		assert(expected == actual, "Failure mathing" + paramName +". Expected: "+ expected +" Actual: "+ actual)
	}
	def assertTrue(actual:Boolean, paramName:String){
		assert(actual,""+paramName+" wasn't true as expected")
	}
	
	
}