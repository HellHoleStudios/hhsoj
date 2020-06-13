package com.hellhole.hhsoj.common;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Represents a submission
 * @author XGN
 *
 */
public class Submission {
	public String code="";
	public String lang="";
	public String author="";
	public long submitTime;
	
	public String judger="";
	public long id;
	
	/**
	 * The result of the submission
	 */
	public HashMap<String,TestsetResult> res=new HashMap<>();
	/**
	 * Compiler information
	 */
	public String compilerInfo="";
	
	/**
	 * The score of the submission.
	 */
	public float score;
	/**
	 * Is the verdict final
	 */
	public boolean isFinal;
	
	/**
	 * The problem set id of the submission
	 */
	public String problemSet="";
	/**
	 * The problem id of the submission
	 */
	public String problemId="";
	
	/**
	 * the current test name of the submission
	 */
	public String test="Init";
	
	public int getRunTime(){
		int ans=0;
		for(Entry<String, TestsetResult> e:res.entrySet()){
			for(TestResult tr:e.getValue().res){
				ans=Math.max(ans, tr.time);
			}
		}
		return ans;
	}
	
	public int getRunMem(){
		int ans=0;
		for(Entry<String, TestsetResult> e:res.entrySet()){
			for(TestResult tr:e.getValue().res){
				ans=Math.max(ans, tr.memory);
			}
		}
		return ans;
	}
	
	public void addResult(String tsn,TestResult testResult) {
		res.get(tsn).res.add(testResult);
	}

	public float calcScore(Problem pr) {
		
		float score=0;
		for(Entry<String,TestsetResult> tsr:res.entrySet()){
			score+=pr.tests.get(tsr.getKey()).score*tsr.getValue().getScore(pr.tests.get(tsr.getKey()).scheme);
		}
		
		return score;
	}
	
}
