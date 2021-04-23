package com.hellhole.hhsoj.common;

import java.util.ArrayList;
import java.util.HashMap;

public class StandingTable {
	public ArrayList<StandingRow> table=new ArrayList<>();
	public String name;
	public String policy;
	public HashMap<String,Integer> index=new HashMap<>();
	public long edTime;
	
	public StandingTable() {
	}
	
	public StandingTable(Problemset ps) {
		this.name=ps.name;
		this.policy=(ps.policy==null?"best":ps.policy);
		this.edTime=ps.edTime;
	}
	
	public boolean tryUpdate(String usr,String id,Submission s) {
		if(!index.containsKey(usr)) {
			index.put(usr, table.size());
			table.add(new StandingRow(usr));
		}
		
		if(s.submitTime>edTime) {
			return false;
		}
		
		StandingRow row=table.get(index.get(usr));
		boolean success=row.tryUpdate(id, s, policy);
		
		if(success) {
			for(int i=index.get(usr)-1;i>=0;i--) {
				if(table.get(i).score<row.score) {
					//swap adjacent row
					StandingRow tmp=table.get(i);
					table.set(i, table.get(i+1));
					table.set(i+1, tmp);
					index.put(tmp.name, index.get(tmp.name)+1);
				}else {
					index.put(usr, i+1);
					return true;
				}
			}
			
			index.put(usr, 0);
			return true;
		}else {
			return false;
		}
	}
}
