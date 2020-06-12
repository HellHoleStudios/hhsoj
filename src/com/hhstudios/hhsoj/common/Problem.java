package com.hhstudios.hhsoj.common;

import java.util.HashMap;

public class Problem {
	public String name;
	public int tl;
	public int ml;
	/**
	 * Should be null and wait for being filled by system
	 */
	public String id,set;
	
	public int ver;
	/**
	 * The difficulty
	 */
	public float diff;
	
	public HashMap<String,Testset> tests=new HashMap<>();
	public String[] order=new String[0];
	
}
