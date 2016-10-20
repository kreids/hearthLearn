package hearthstone.parse

import org.scalatest.FunSpec
import org.scalatest.BeforeAndAfter


class JsonParserTest extends FunSpec with BeforeAndAfter{
	
	var objectUnderTest: JsonParser = new JsonParser
	
	before{
		objectUnderTest = new JsonParser
	}
	
	def assertString(expected: String, actual: String){
		assert(expected.equals(actual), "Failure. Expected: "+ expected +" Actual: "+ actual)
	}
	
	private final val ANY_VALID_CARD:String = "{\"id\":\"BRM_013\",\"name\":\"Quick Shot\",\"mana\":2}"
	
	describe("cardParse"){
		describe("when called on any card"){
			val parsedCard: String= objectUnderTest.cardParse(ANY_VALID_CARD)
			it("returns the correct id"){
				assertString("BRM_013", parsedCard)
			}
		}
	}
	
}