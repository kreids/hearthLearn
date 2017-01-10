package hearthstone.parse

import org.scalatest.FunSpec
import org.scalatest.BeforeAndAfter


class GraphTransformsTest extends FunSpec{
	
	val ANY_CARD: Card = new Card("BRM_013", 2 , "QuickShot")
	val ANY_OTHER_CARD: Card = new Card("OG_303", 2, "Cult Sorcerer")
	val SHORT_CARD_LIST: List[Card] = List(ANY_CARD, ANY_OTHER_CARD)
	val TURN_2 : Turn = new Turn(2, List(ANY_CARD))
	val TURN_3 : Turn = new Turn(3,List(ANY_OTHER_CARD))
	val TURN_LIST: List[Turn] = List(TURN_2,TURN_3)
	val SHORT_GAME: Game = new Game(false,true,Hero.HUNTER,2,TURN_LIST)
	
	
	describe("playCombosFromGame"){
		describe("When called on a short game"){
			val combo:List[PlayCombo] = GraphTransforms.playCombosFromGame(SHORT_GAME)
			it("has the correct cards"){
				assertPlayComboCards("BRM_013", "OG_303", 2, 2, combo.head)
			}
		}
	}
	
	def assertPlayComboCards(expectedCardId1: String, expectedCardId2: String, expectedMana1: Int, expectedMana2: Int, combo: PlayCombo){
		assertCard(expectedCardId1, expectedMana1, combo.firstCard)
		assertCard(expectedCardId2, expectedMana2, combo.secondCard)	
	}
	def assertCard(expectedId:String,expectedMana:Int,parsedCard:Card){
		assertString(expectedId, parsedCard.ID, "ID")
		assertInt(expectedMana, parsedCard.MANA, "MANA")
	}
	def assertString(expected: String, actual: String, paramName:String){
		assert(expected.equals(actual), "Failure mathing " + paramName +". Expected: "+ expected +" Actual: "+ actual)
	}
	def assertInt(expected: Int, actual: Int, paramName:String){
		assert(expected == actual, "Failure mathing " + paramName +". Expected: "+ expected +" Actual: "+ actual)
	}
	
  
}