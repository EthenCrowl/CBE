package com.lexxie.cbs2.parser;

import com.lexxie.cbs2.parser.tokenizer.Tokenizer;

public class Initialize {

	/**
	 * 	adds token regex to tokenizer
	 * 	@param Tokenizer
	 * */
	public static void addExp(Tokenizer tokenizer){
		
		tokenizer.add("var\\[\\]", 33); //define new array
		tokenizer.add("%[a-zA-Z@_]+\\[[0-9]+\\]%", 34); //array
		tokenizer.add("var", 1); // define variable
		tokenizer.add("\\/\\/", 2); // comment
		tokenizer.add("\\#[a-zA-Z0-9_\\W]*", 3); //command
		tokenizer.add("func", 4); // define function
		tokenizer.add("assign", 35);
		tokenizer.add("\\-t", 5); // parameter tag
		tokenizer.add("\\-d", 6); // parameter debug
		tokenizer.add("\\-cn", 37);//set name
		tokenizer.add("\\-S", 38);// set stat
		tokenizer.add("\\-c", 8); // parameter conditional
		tokenizer.add("\\-r", 9); // parameter need redstone
		tokenizer.add("\\-b", 10); //parameter set behavior
		tokenizer.add("\\-R", 11); // parameter repeating command block
		tokenizer.add("\\-C", 12); // parameter chain command block
		tokenizer.add("\\-N", 13); //parameter normal command block
		tokenizer.add("\\-n", 32);//parameter negate 
		tokenizer.add("repeat", 14); //repeating function
		tokenizer.add("execute\\:", 17); //execute
		tokenizer.add("init\\:", 18); // init
		tokenizer.add("[+-]", 19); // plus or minus
		tokenizer.add("[*/]", 20); // mult or divide
		tokenizer.add("\\=", 21); // equals
		tokenizer.add("[a-zA-Z@][a-zA-Z0-9@_.\\[\\]=]*", 22); // Variable
		tokenizer.add("%[a-zA-Z0-9@_]+%", 23); // Placeholder
		tokenizer.add("\\(", 24); // open brackets
		tokenizer.add("\\)", 25); // close brackets
		tokenizer.add("\\{", 26); // open curly bracket
		tokenizer.add("\\}", 27); // close curly brackets
		tokenizer.add("\\s+", 28); // space
		tokenizer.add("[0-9]+", 29); //digit
		tokenizer.add("\"(.*?)\".*", 30); //text
		tokenizer.add(",", 31); //Comma
		tokenizer.add("\\W+", 36); //all other things
		
	}
	
	
}
