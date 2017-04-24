package com.lexxie.cbs2.parser.data.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Array {

	//static members
	
		private static HashMap<String, Array> arrays = new HashMap();
		
		public static void addArray(String name, Array array){
			arrays.put(name, array);
		}
		
		public static Array getArray(String name){
			
			if(arrays.containsKey(name)){
				return arrays.get(name);
			}
			
			return null;
			
		}
		
		public static HashMap<String, Array> getArrays(){
			return arrays;
		}
		
		//non static members
		
		String name;
		
		List<String> values = new ArrayList();
		
		public void setName(String name){
			this.name = name;
		}
		
		public void addValue(String value){
			values.add(value);
		}

		public String getValue(int x){
			
			if(!values.get(x).isEmpty()){
				return values.get(x);
			}
			
			return null;
		}
		
		public List<String> getValues(){
			return values;
		}
		
		public String getName(){
			return name;
		}
	
}
