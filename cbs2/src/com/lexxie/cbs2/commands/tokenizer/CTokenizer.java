package com.lexxie.cbs2.commands.tokenizer;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jdk.nashorn.internal.runtime.ParserException;

public class CTokenizer {

	private LinkedList<TokenInfo> tokenInfos;
	private LinkedList<Token> tokens;
	
	/**
	 * 	Create Tokenizer
	 * */
	public CTokenizer(){
		tokenInfos = new LinkedList<TokenInfo>();
		tokens = new LinkedList<Token>();
	}
	
	/**
	 * 	Adds new token
	 * 	@param regex 
	 * 	@param token code
	 * */
	public void add(String regex, int token){
		tokenInfos.add(new TokenInfo(Pattern.compile("^(" + regex + ")"), token));
	}
	
	/**
	 * 	tokenize a string
	 * 	@param string to tokenize
	 * */
	public void tokenize(String str){
		
		String s = new String(str);
		
		tokens.clear();
		
		while(!s.equals("")){
			boolean match = false;
			
			for(TokenInfo info : tokenInfos){
				Matcher m = info.regex.matcher(s);
				
				if(m.find()){
					match = true;
					
					String tok = m.group().trim();
					tokens.add(new Token(info.token, tok));
					
					s = m.replaceFirst("");
					break;
				}
			}
			
			if(!match) throw new ParserException("Unexpected character in input: "  + s);
			
		}
	}
	
	/**
	 * 	@return tokens
	 * */
	public LinkedList<Token> getTokens(){
		return tokens;
	}
	
	/**
	 * 	Holds information about each token (regex and token number)
	 * */
	private class TokenInfo{
		
		private final Pattern regex;
		public final int token;
		
		/**
		 * 	Creates a new token pattern and assign token number
		 * 	@param adds regex for the token
		 * 	@param adds the number for the token
		 * */
		public TokenInfo(Pattern regex, int token){
			super();
			this.regex = regex;
			this.token = token;
		}
		
	}
	
	public static class Token{
		
		public static final int EPSILON = 0;
		public static final int TEXT = 1;
		public static final int PARAMETER = 2;
		public static final int COLON = 3;
		public static final int SEMICOLON = 4;
		public static final int OPEN_CURLY_BRACKETS = 5;
		public static final int CLOSE_CURLY_BRACKETS = 6;
		public static final int SPACE = 7;
		public static final int ALL = 8;
		public static final int COMMA = 9;
		public static final int ERROR = 10;
		public static final int SEPERATE = 11;
		public static final int ALLTEXT = 12;
		public static final int NUMBER = 13;
		public static final int COORDIANTE = 14;
		public static final int NBT = 15;
		public static final int EXECUTE = 16;
		public static final int OPTIONAL = 17;
		
		public final int token;
		public final String sequence;
		
		/**
		 * 	Defines a new token
		 * */
		public Token(int token, String sequence){
			super();
			this.token = token;
			this.sequence = sequence;
		}
		
	}
	
}
