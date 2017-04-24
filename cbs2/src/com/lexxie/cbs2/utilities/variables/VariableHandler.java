package com.lexxie.cbs2.utilities.variables;

import java.util.HashMap;
import java.util.Map;

public class VariableHandler {

	public Map<String, String> variables = new HashMap<String, String>();
	
	public VariableHandler(){
		
	}
	
	public void deleteVariable(String key){
		variables.remove(key);
	}
	
	public void addVariables(String name, String value){
		variables.put(name, value);
	}
	
	public Map<String, String> getVariables(){
		return variables;
	}
	
}
