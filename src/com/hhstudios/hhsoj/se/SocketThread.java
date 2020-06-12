package com.hhstudios.hhsoj.se;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import com.google.gson.Gson;
import com.hhstudios.hhsoj.common.FileUtil;
import com.hhstudios.hhsoj.common.Submission;

public class SocketThread extends Thread {
	Socket sock;
	ServerManager boss;
	public SocketThread(Socket s,ServerManager boss){
		this.sock=s;
		this.boss=boss;
	}
	
	DataInputStream dis;
	DataOutputStream dos;
	
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
			
			if(op.equals("judger")){
				judgerRegister();
				
				dos.writeUTF(FileUtil.readFile("config/lang.json"));
			}else if(op.equals("submit")){
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
