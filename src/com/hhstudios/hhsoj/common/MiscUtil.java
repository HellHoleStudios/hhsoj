package com.hhstudios.hhsoj.common;

public class MiscUtil {

	public static void assertEql(int x, int y) {
		if(x!=y){
			throw new AssertionError(x+"!="+y);
		}
	}

	@Deprecated
	public static int calcHash(byte[] by, int len) {
		final int p=114514;
		int hash=0;
		for(int i=0;i<len;i++){
			hash=(hash+by[i])*p;
		}
		
		return hash;
	}

}
