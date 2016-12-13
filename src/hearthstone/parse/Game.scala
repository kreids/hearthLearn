package hearthstone.parse


import Hero._;
import scala.collection.mutable.ListBuffer



class Game(xHasCoin: Boolean, xDidWin: Boolean, xHero: Hero.Value, xRank:Int, xTurns: List[Turn])extends java.io.Serializable {
	var hasCoin = xHasCoin
	var didWin = xDidWin
	var hero = xHero
	var rank = xRank
	var turns = xTurns
	
	  override def toString: String =
    	"\nhero: "+ hero+ ", hasCoin: " + hasCoin + ", didWin: " + didWin +", rank: " + rank + ", turns: [" +turns.toString()+"]" 
}