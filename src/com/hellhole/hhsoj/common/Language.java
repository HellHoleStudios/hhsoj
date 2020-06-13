package com.hellhole.hhsoj.common;

/**
 * Represents a programming language
 * @author XGN
 *
 */
public class Language {
	public String name;
	public String id;
	public String ext;
	/**
	 * Null=no compile need <br/>
	 * file is in Main.ext <br/>
	 * should compile to Main.exe <br/>
	 */
	public String[] compileCmd; 
	/**
	 * should read from Main.exe / Main.ext
	 */
	public String[] runCmd;
	
	/**
	 * The possible operation code
	 */
	public int[] opCode;
	
	/**
	 * The possible read file
	 */
	public String[] file;
}
