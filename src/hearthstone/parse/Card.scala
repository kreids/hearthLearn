package hearthstone.parse

case class Card(id: String, mana: Int, name:String) extends java.io.Serializable {
	
	
	def toStringBuilder: StringBuilder = {
		val sb = new StringBuilder
		sb.append("{id: ")
		sb.append(id)
		sb.append(", name: ")
		sb.append(name)
		sb.append(", mana:")
		sb.append(mana)
		sb.append("}")
	}
	override def toString: String=
		toStringBuilder.toString
}