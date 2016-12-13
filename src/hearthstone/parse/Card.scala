package hearthstone.parse

case class Card(id: String, mana: Int) extends java.io.Serializable {
	var ID:String = id
	var MANA:Int = mana
	
	override def toString: String = 
		"{id:"+ID+", mana:"+MANA+"}"
}