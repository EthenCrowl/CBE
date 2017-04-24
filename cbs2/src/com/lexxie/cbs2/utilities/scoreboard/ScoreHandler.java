package com.lexxie.cbs2.utilities.scoreboard;

import java.util.HashMap;
import java.util.Map;

/**
 * 	handles scores for every project
 * */
public class ScoreHandler {

	public Map<String, String> scores = new HashMap<String, String>();
	
	public ScoreHandler(){
		
	}
	
	public void deleteScore(String key){
		scores.remove(key);
	}
	
	public void addScore(String scoreName, String scoreType){
		scores.put(scoreName, scoreType);
	}
	
	public Map<String, String> getScores(){
		return scores;
	}
	
}
