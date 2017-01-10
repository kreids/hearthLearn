package hearthstone.parse

case class Card(id: String, mana: Int, name:String) extends java.io.Serializable {
	var ID:String = id
	var MANA:Int = mana
	var NAME:String = name
	
	def toStringBuilder: StringBuilder = {
		val sb = new StringBuilder
		sb.append("{id: ")
		sb.append(ID)
		sb.append(", name")
		sb.append(name)
		sb.append(", mana:")
		sb.append(MANA)
		sb.append("}")
	}
	override def toString: String=
		toStringBuilder.toString
}