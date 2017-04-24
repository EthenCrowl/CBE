package com.lexxie.cbs2.parser.tokenizer;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jdk.nashorn.internal.runtime.ParserException;

public class Tokenizer {

	private LinkedList<TokenInfo> tokenInfos;
	private LinkedList<Token> tokens;
	
	/**
	 * 	Create Tokenizer
	 * */
	public Tokenizer(){
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
		
		public final Pattern regex;
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
	
	/**
	 * 	Hold information about each the created token (token sequence and token number)
	 * */
	public static class Token{
		
		public static final int EPSILON = 0;
		public static final int DEFINE_VARIABLE = 1;
		public static final int COMMENT = 2;
		public static final int COMMAND = 3;
		public static final int DEFINE_FUNCTION = 4;
		public static final int P_TAG = 5;
		public static final int P_DEBUG = 6;
		public static final int P_TEST = 7;
		public static final int P_CONDITIONAL = 8;
		public static final int P_NEEDREDSTONE = 9;
		public static final int P_BEHAVIOUR = 10;
		public static final int P_REPEATINGCOMMAND = 11;
		public static final int P_CHAINCOMMAND = 12;
		public static final int P_NORMALCOMMAND = 13;
		public static final int REPEAT = 14;
		public static final int LASTCOMMAND = 15;
		public static final int LASTPOSITION = 16;
		public static final int EXECUTE = 17;
		public static final int INIT = 18;
		public static final int PLUSMINUS = 19;
		public static final int MULTDIV = 20;
		public static final int EQUALS = 21;
		public static final int VARIABLE = 22;
		public static final int PLACEHOLDR = 23;
		public static final int OPEN_BRACKETS = 24;
		public static final int CLOSE_BRACKETS = 25;
		public static final int OPEN_CURLY_BRACKETS = 26;
		public static final int CLOSE_CURLY_BRACKETS = 27;
		public static final int SPACE = 28;
		public static final int DIGIT = 29;
		public static final int TEXT = 30;
		public static final int COMMA = 31;
		public static final int P_NEGATE = 32;
		public static final int ARRAY = 33;
		public static final int ERROR = 34;
		public static final int ASSIGN = 35;
		public static final int UNUSED = 36;
		public static final int P_NAME = 37;
		public static final int P_STAT = 38;
		
		
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
