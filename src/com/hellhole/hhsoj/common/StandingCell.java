package com.hellhole.hhsoj.common;

/**
 * Represent a cell in a standing
 * @author XGN
 *
 */
public class StandingCell {
	public double score;
	public long time;
	public boolean isFinal;
	public long subId;
	
	public StandingCell(double score, long time,boolean isFinal,long subId) {
		this.score = score;
		this.isFinal=isFinal;
		this.time = time;
	}
	
	
}
