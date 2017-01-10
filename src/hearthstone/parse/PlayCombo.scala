package hearthstone.parse

import org.apache.spark.graphx.Edge

case class PlayCombo(firstCard: Card, secondCard: Card, hero: Hero.Value, didWin: Boolean){
	
}