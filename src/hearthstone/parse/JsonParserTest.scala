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
	private final val ANY_SHORT_GAME =
		"{\"user_hash\":\"22EB1EB98A7EACC5AAB5750557E197D6\",\"region\":\"Americas\",\"id\":45850386,\"mode\":\"ranked\",\"hero\":\"Paladin\","+
		"\"hero_deck\":null,\"opponent\":\"Mage\",\"opponent_deck\":\"Tempo\",\"coin\":true,\"result\":\"loss\",\"duration\":1039,\"rank\":null,"+
		"\"legend\":null,\"note\":null,\"added\":\"2016-10-18T17:50:28Z\", \"card_history\":" +
		"[{\"player\":\"opponent\",\"turn\":2,\"card\":{\"id\":\"OG_303\",\"name\":\"Cult Sorcerer\",\"mana\":2}},"+
		"{\"player\":\"me\",\"turn\":2,\"card\":{\"id\":\"CS2_101\",\"name\":\"Reinforce\",\"mana\":2}},"+
		"{\"player\":\"opponent\",\"turn\":3,\"card\":{\"id\":\"CS2_034\",\"name\":\"Fireblast\",\"mana\":2}},"+
		"{\"player\":\"opponent\",\"turn\":3,\"card\":{\"id\":\"NEW1_012\",\"name\":\"Mana Wyrm\",\"mana\":1}}]}"
		private final val SHORT_INPUT_STREAM = 
		"{\"range_start\":\"2016-11-20T00:00:00Z\",\"range_end\":\"2016-11-21T00:00:00Z\",\"unique_users\":25,\"total_games\":270,\"games\":"+
		"{\"user_hash\":\"22EB1EB98A7EACC5AAB5750557E197D6\",\"region\":\"Americas\",\"id\":45850386,\"mode\":\"ranked\",\"hero\":\"Paladin\","+
		"\"hero_deck\":null,\"opponent\":\"Mage\",\"opponent_deck\":\"Tempo\",\"coin\":true,\"result\":\"loss\",\"duration\":1039,\"rank\":null,"+
		"\"legend\":null,\"note\":null,\"added\":\"2016-10-18T17:50:28Z\", \"card_history\":" +
		"[{\"player\":\"opponent\",\"turn\":2,\"card\":{\"id\":\"OG_303\",\"name\":\"Cult Sorcerer\",\"mana\":2}},"+
		"{\"player\":\"me\",\"turn\":2,\"card\":{\"id\":\"CS2_101\",\"name\":\"Reinforce\",\"mana\":2}},"+
		"{\"player\":\"opponent\",\"turn\":3,\"card\":{\"id\":\"CS2_034\",\"name\":\"Fireblast\",\"mana\":2}},"+
		"{\"player\":\"opponent\",\"turn\":3,\"card\":{\"id\":\"NEW1_012\",\"name\":\"Mana Wyrm\",\"mana\":1}}]}]}"	
		
	
	var objectUnderTest: HearthJsonParser = new HearthJsonParser
	
	
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
	/*
	describe("historyParseToPlayList"){
		describe("when called on any turn history spanning turns after turn 1"){
			val historyParse = objectUnderTest.historyParseToPlayList(ANY_VALID_CARD_HISTORY_SPANNING_TURNS_AFTER_TURN_ONE)
			val playerIt = historyParse._1.iterator
			val opponentIt = historyParse._2.iterator
			it("has the correct first element in player list"){
				val elt1Player = playerIt.next
				assertPlayListElement("CS2_101", 2, 2, elt1Player)
			}
			it("has the correct first element in opponent list"){
				val elt1Opponent = opponentIt.next
				assertPlayListElement("OG_303", 2, 2, elt1Opponent)
			}
			it("has the correct second element in opponent list"){
				val elt2Opponent = opponentIt.next
				assertPlayListElement("CS2_034", 2, 3, elt2Opponent)
			}
			it("has the correct third element in opponent list"){
				val elt3Opponent = opponentIt.next
				assertPlayListElement("NEW1_012", 1, 3, elt3Opponent)
			}		
		}
	}
	*/
	describe("historyParseToTurnList"){
		describe("when called on any turn history spanning turns after turn 1"){
			val historyParse = objectUnderTest.historyParseToTurnList(ANY_VALID_CARD_HISTORY_SPANNING_TURNS_AFTER_TURN_ONE)
			val playerIt = historyParse._1.iterator
			val opponentIt = historyParse._2.iterator
			
			val elt1Player:Turn = playerIt.next
			val elt1PlayerTurnIt = elt1Player.plays.iterator
			it("has the correct first card in first Turn in player list"){
				assertInt(2, elt1Player.turn, "TURN")				
				assertCard("CS2_101", 2, elt1PlayerTurnIt.next)		
			}
			
			val elt1Opponent:Turn = opponentIt.next
			val elt1OpponentTurnIt = elt1Opponent.plays.iterator
			it("has the correct first card in first Turn in opponent list"){
				assertInt(2, elt1Opponent.turn, "TURN")
				assertCard("OG_303", 2, elt1OpponentTurnIt.next)
			}
			
			val elt2Opponent:Turn = opponentIt.next
			val elt2OpponentTurnIt = elt2Opponent.plays.iterator
			it("has the correct first card in second Turn in opponent list"){
				assertInt(3, elt2Opponent.turn, "TURN")
				assertCard("CS2_034", 2, elt2OpponentTurnIt.next)
			}
			
			it("has the correct second card in second Turn in opponent list"){
				assertCard("NEW1_012", 1, elt2OpponentTurnIt.next)
			}
		}
	}
	
	describe("gameParse"){
		describe("When called on any short game"){
			val gameParse = objectUnderTest.gameParse(ANY_SHORT_GAME)
			val playerGame = gameParse._1
			val opponentGame = gameParse._2

			it("Has the correct player game"){
				assertGame(true, false, Hero.PALADIN, 0, playerGame)
			}
			it("Has the correct opponent game"){
				assertGame(false, true, Hero.MAGE, 0, opponentGame)
			}

		}
	}
	describe("inputParse"){
		
	}

	
	def assertGame(expectedCoin:Boolean, expectedWin:Boolean, expectedHero:Hero.Value,expectedRank:Int, actualGame:Game){
		assertBoolean(expectedCoin, actualGame.hasCoin, "HAS COIN")
		assertBoolean(expectedWin, actualGame.didWin, "DID WIN")
		assertBoolean(expectedWin, actualGame.didWin, "DID WIN")
		assertHero(expectedHero, actualGame.hero)
		assertInt(expectedRank, actualGame.rank, "RANK")
	}
	
	def assertHero(expectedHero:Hero.Value, actualHero:Hero.Value){
		assert(expectedHero.equals(actualHero),"Failure mathing HERO. Expected: "+ expectedHero +" Actual: "+ actualHero)
	}

	
	def assertPlayListElement(expectedId:String,expectedMana:Int,expectedTurn:Int, parsedTurn:(Card,Int)){
		assertCard(expectedId, expectedMana, parsedTurn._1)
		assertInt(expectedTurn,parsedTurn._2,"TURN")
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
		assert(expected.equals(actual), "Failure mathing " + paramName +". Expected: "+ expected +" Actual: "+ actual)
	}
	def assertInt(expected: Int, actual: Int, paramName:String){
		assert(expected == actual, "Failure mathing " + paramName +". Expected: "+ expected +" Actual: "+ actual)
	}
	def assertTrue(actual:Boolean, paramName:String){
		assert(actual,""+paramName+" wasn't true as expected")
	}
	
}