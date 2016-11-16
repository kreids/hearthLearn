package hearthstone.parse


import Hero._;
import scala.collection.mutable.ListBuffer



class Game(xHasCoin: Boolean, xDidWin: Boolean, xHero: Hero.Value, xRank:Int, xTurns: List[Turn]){
	var hasCoin = xHasCoin
	var didWin = xDidWin
	var hero = xHero
	var rank = xRank
	var turns = xTurns
	
}