package hearthstone.parse

import org.scalatest.FunSpec
import org.scalatest.BeforeAndAfter


class JsonParserTest extends FunSpec with BeforeAndAfter{
	
	var objectUnderTest: JsonParser = new JsonParser
	
	
	def assertString(expected: String, actual: String, paramName:String){
		assert(expected.equals(actual), "Failure mathing" + paramName +". Expected: "+ expected +" Actual: "+ actual)
	}
	def assertInt(expected: Int, actual: Int, paramName:String){
		assert(expected == actual, "Failure mathing" + paramName +". Expected: "+ expected +" Actual: "+ actual)
	}
	
	private final val ANY_VALID_CARD:String = "{\"id\":\"BRM_013\",\"name\":\"Quick Shot\",\"mana\":2}"
	
	describe("cardParse"){
		describe("when called on any card"){
			val parsedCard= objectUnderTest.cardParse(ANY_VALID_CARD)
			it("returns the correct id"){
				assertString("BRM_013", parsedCard._1,"ID")
				assertString("Quick Shot", parsedCard._2,"NAME")
				assertInt(2, parsedCard._3, "MANA")
			}
		}
	}
	
}