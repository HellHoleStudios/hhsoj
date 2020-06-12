package com.hhstudios.hhsoj.se;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import com.hhstudios.hhsoj.common.Submission;

/**
 * Represents a judging server
 * @author think
 *
 */
public class Judger {
	public boolean isFree;
	/**
	 * The name of it
	 */
	public String name;
	/**
	 * The socket
	 */
	public Socket sock;
	public DataInputStream dis;
	public DataOutputStream dos;
	/**
	 * The dealing thread
	 */
	public Thread deal;
	/**
	 * Manually set dead
	 */
	public boolean dead;
	
	public Judger(String name,Socket sock,DataInputStream dis,DataOutputStream dos){
		deal=null;
		isFree=true;
		this.name=name;
		this.sock=sock;
		this.dis=dis;
		this.dos=dos;
	}

	public boolean isOnline() {
		return !sock.isClosed() && !sock.isInputShutdown() && !sock.isOutputShutdown() && !dead ;
	}
	
	@Deprecated
	/**
	 * Nah, old-fashioned checking way
	 * @return
	 */
	public boolean handshake(){
		try{
			dos.writeUTF("Hi?");
			String res=dis.readUTF();
			return res.equals("Hi!");
		}catch(Exception e){
			System.out.println("Unsuccessful handshake:"+e);
			return false;
		}
	}
		
	public void work(Submission submission,ServerManager boss) {
		deal=new JudgingThread(submission,this,boss);
		isFree=false;
		deal.start();
	}
}
