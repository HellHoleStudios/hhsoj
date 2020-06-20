package com.hellhole.hhsoj.common;

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
				if(!"Accepted".equals(tr.verdict)){
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
		
		if("min".equalsIgnoreCase(scheme)){
			float ans=1e9f;
			for(TestResult tr:res){
				ans=Math.min(ans, tr.score);
			}
			
			return ans;
		}
		
		if("max".equalsIgnoreCase(scheme)){
			float ans=-1e9f;
			for(TestResult tr:res){
				ans=Math.max(ans, tr.score);
			}
			
			return ans;
		}
		
		if("sum".equalsIgnoreCase(scheme)){
			float ans=0;
			for(TestResult tr:res){
				ans+=tr.score;
			}
			
			return ans;
		}
		
		if("avg".equalsIgnoreCase(scheme)){
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
			if("Accepted".equals(i.verdict)||"Point".equals(i.verdict)) {
				cnt++;
			}
		}
		return (float)cnt/res.size();
	}
}
