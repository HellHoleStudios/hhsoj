package com.hhstudios.hhsoj.common;

/**
 * result of a single testcase
 * @author think
 *
 */
public class TestResult {
	public String verdict="";
	public int time;
	public int memory;
	/**
	 * Checker information
	 */
	public String info="";
	/**
	 * The input file (1KB)
	 */
	public String input="";
	/**
	 * The output file (1KB)
	 */
	public String output="";
	/**
	 * The answer file (1KB)
	 */
	public String answer="";
	
	/**
	 * The score
	 */
	public float score;
	public TestResult(String verdict, int time, int memory, String info, String input, String output, float score) {
		this.verdict = verdict;
		this.time = time;
		this.memory = memory;
		this.info = info;
		this.input = input;
		this.output = output;
		this.score = score;
	}
	
	public TestResult(String verdict, String time, String memory, String info, String input, String output,String answer, float score) {
		this.verdict = verdict;
		this.time = Integer.parseInt(time);
		this.memory = Integer.parseInt(memory);
		this.info = info;
		this.input = input;
		this.output = output;
		this.answer = answer;
		this.score = score;
	}
	public TestResult(){
		
	}

}
