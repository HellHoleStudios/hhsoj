package com.hellhole.hhsoj.common;

import java.util.HashMap;

/**
 * A row in standing
 * @author XGN
 *
 */
public class StandingRow {
	public HashMap<String, StandingCell> col;
	public double score;
	public String name;
	
	public StandingRow(String n) {
		this.name = n;
		col = new HashMap<>();
	}
	
	/**
	 * Force update the submission <br/>
	 * 
	 * Use <code>tryUpdate</code> instead
	 * @param id
	 * @param s
	 */
	public void update(String id,Submission s) {
		StandingCell cell=col.getOrDefault(id, new StandingCell(0, 0, true,-1));
		score-=cell.score;
		col.put(id, new StandingCell(s.score,s.submitTime,s.isFinal,s.id));
		score+=s.score;
	}
	
	/**
	 * Try to update the cell with id
	 * @param id
	 * @param s
	 * @param policy
	 * @return replaced or not
	 */
	public boolean tryUpdate(String id,Submission s,String policy) {
		if(col.containsKey(id)) {
			StandingCell cell=col.get(id);
			if("first".equals(policy)) {
				if(s.submitTime<=cell.time) {
					update(id,s);
					return true;
				}else {
					return false;
				}
			}else if("last".equals(policy)) {
				if(s.submitTime>=cell.time) {
					update(id,s);
					return true;
				}else {
					return false;
				}
			}else if("best".equals(policy)) {
				if(s.score>=cell.score) {
					update(id,s);
					return true;
				}else {
					return false;
				}
			}else {
				//Unknown Rule
				return false;
			}
		}else {
			update(id,s);
			return true;
		}
	}
}
