package com.hhstudios.hhsoj.common;

import java.util.ArrayList;

/**
 * A testset
 * @author XGN
 *
 */
public class Testset {
	/**
	 * Max - the maximum score of each subtask <br/>
	 * Min - the minimum score of each subtask <br/>
	 * Sum - the sum of score <br/>
	 * Avg - the avg of score <br/>
	 */
	public String scheme;
	
	/**
	 * All requirement for testing this subtask
	 */
	public ArrayList<String> requirement=new ArrayList<>();
	
	/**
	 * Judge to the end of the subtask even if there's something wrong already?
	 */
	public boolean toEnd;

	/**
	 * The base score of the subtask
	 */
	public float score;

	@Override
	public String toString() {
		return "Testset [scheme=" + scheme + ", requirement=" + requirement + ", toEnd=" + toEnd + "]";
	}
}
