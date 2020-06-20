package com.hellhole.hhsoj.se;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import com.google.gson.Gson;
import com.hellhole.hhsoj.common.FileUtil;
import com.hellhole.hhsoj.common.Submission;

public class SocketThread extends Thread {
	private Socket sock;
	private ServerManager boss;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	public SocketThread(Socket s,ServerManager boss){
		this.sock=s;
		this.boss=boss;
	}
	
	public void judgerRegister() throws Exception{
		String name=dis.readUTF();
		boss.addJudger(name,sock,dis,dos);
	}
	
	public void addSubmission() throws Exception{
		Gson gs=new Gson();
		boss.addSubmission(gs.fromJson(dis.readUTF(), Submission.class));
		
	}
	
	public void run(){
		System.out.println("New connection from "+sock.getInetAddress());
		
		try{

			dis=new DataInputStream(sock.getInputStream());
			dos=new DataOutputStream(sock.getOutputStream());
			
			String op=dis.readUTF();
			
			if("judger".equals(op)){
				judgerRegister();
				
				dos.writeUTF(FileUtil.readFile("config/lang.json"));
			}else if("submit".equals(op)){
				addSubmission();
			}else{
				System.out.println("Unknown command");
			}
			
			System.out.println("Thread exits gracefully");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
