package com.lexxie.cbs2.commands.tokenizer;

public class CInitialize {

	/**
	 * 	adds token regex to tokenizer
	 * 	@param Tokenizer
	 * */
	public static void addExo(CTokenizer tokenizer){
		
		tokenizer.add("\"(.*?)\"", 1);
		tokenizer.add("~", 2);
		tokenizer.add(":", 3);
		tokenizer.add(";", 4);
		tokenizer.add("\\{", 5);
		tokenizer.add("\\}", 6);
		tokenizer.add("\\s+", 7);
		tokenizer.add("\\*", 8);
		tokenizer.add(",", 9);
		tokenizer.add("\\|", 11);
		tokenizer.add("\\+", 12);
		tokenizer.add("n", 13);
		tokenizer.add("c", 14);
		tokenizer.add("NBT", 15);
		tokenizer.add("E", 16);
		tokenizer.add("\\?", 17);
	}
	
}
