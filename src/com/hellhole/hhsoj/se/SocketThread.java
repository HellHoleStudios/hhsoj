package com.hellhole.hhsoj.se;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.hellhole.hhsoj.common.FileUtil;
import com.hellhole.hhsoj.common.StandingTable;
import com.hellhole.hhsoj.common.Submission;
import com.hellhole.hhsoj.tomcat.util.TomcatHelper;

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
	
	public void rebase() throws Exception{
		String name=dis.readUTF();
		ArrayList<Submission> sub=TomcatHelper.getAllSubmissions();
		StandingTable st=new StandingTable(TomcatHelper.getProblemset(name));
		for(Submission s:sub) {
			if(s.problemSet.equals(name)) {
				st.tryUpdate(s.author, s.problemId, s);
			}
		}
		boss.standings.put(name, st);

		Gson gs=new Gson();
		FileUtil.writeFile(TomcatHelper.getConfig().path+"/cache/"+name, gs.toJson(st));
		System.out.println("Rebased:"+name);
	}
	
	public void run(){
		System.out.println("New connection from "+sock.getInetAddress());
		
		try{

			dis=new DataInputStream(sock.getInputStream());
			dos=new DataOutputStream(sock.getOutputStream());
			
			String op=dis.readUTF();
			
			if("judger".equals(op)){
				judgerRegister();
				
				dos.writeUTF(FileUtil.readFile(TomcatHelper.getConfig().path+"/config/lang.json"));
			}else if("submit".equals(op)){
				addSubmission();
			}else if("rebase".equals(op)){
				rebase();
			}else {
				System.out.println("Unknown command");
			}
			
			System.out.println("Thread exits gracefully");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
