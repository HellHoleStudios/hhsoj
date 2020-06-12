package com.hhstudios.hhsoj.common;

import java.util.ArrayList;

/**
 * The result of a testset
 * @author XGN
 *
 */
public class TestsetResult {
	public ArrayList<TestResult> res;
	public boolean pass;
	
	public TestsetResult(){
		res=new ArrayList<>();
	}
	
	public String getVerdict(){
		if(res==null || res.isEmpty()){
			return "Skipped";
		}else{
			for(TestResult tr:res){
				if(!tr.verdict.equals("Accepted")){
					return tr.verdict;
				}
			}
			return "Accepted";
		}
	}

	public float getScore(String scheme) {
		if(res==null || res.isEmpty()){
			return 0;
		}
		
		if(scheme.equalsIgnoreCase("min")){
			float ans=1e9f;
			for(TestResult tr:res){
				ans=Math.min(ans, tr.score);
			}
			
			return ans;
		}
		
		if(scheme.equalsIgnoreCase("max")){
			float ans=-1e9f;
			for(TestResult tr:res){
				ans=Math.max(ans, tr.score);
			}
			
			return ans;
		}
		
		if(scheme.equalsIgnoreCase("sum")){
			float ans=0;
			for(TestResult tr:res){
				ans+=tr.score;
			}
			
			return ans;
		}
		
		if(scheme.equalsIgnoreCase("avg")){
			float ans=0;
			for(TestResult tr:res){
				ans+=tr.score;
			}
			
			return ans/res.size();
		}
		
		return 0;
	}
	
	public float getPassed() {
		int cnt=0;
		for(TestResult i:res) {
			if(i.verdict.equals("Accepted")||i.verdict.equals("Point")) {
				cnt++;
			}
		}
		return (float)cnt/res.size();
	}
}
