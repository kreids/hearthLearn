package hearthstone.parse

import play.api.libs.json._
import play.api.libs.functional.syntax._

class JsonParser {
	
	
	//ToDo: fix Big Decimal to Int to go FAST
	def cardParse(card:String):(String, String, Int) ={
		val jCard: JsValue = Json.parse(card)
		((jCard\"id").as[JsString].value,(jCard\"name").as[JsString].value, (jCard\"mana").as[JsNumber].value.toIntExact)
	}
}