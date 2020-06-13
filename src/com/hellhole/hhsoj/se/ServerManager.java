package com.hellhole.hhsoj.se;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import com.google.gson.Gson;
import com.hellhole.hhsoj.common.Submission;

/**
 * The main server to control everything. <br/>
 * File operations and socket connections 
 * @author XGN
 *
 */
public class ServerManager {

	public static void main(String[] args) throws Exception {
		ServerManager m=new ServerManager();
		m.solve(args);
	}

	
	String ip;
	int port;
	
	public Vector<Judger> judgers=new Vector<>();
	
	public synchronized void addJudger(String name,Socket sock,DataInputStream dis,DataOutputStream dos){
		System.out.println("Added new judge:"+name+" from "+sock);
		judgers.add(new Judger(name,sock,dis,dos));
		
		notifyJudge();
	}
	
	public Vector<Submission> submissions=new Vector<>();
	
	public synchronized void addSubmission(Submission s){
		submissions.add(s);
		saveSubmission(s);
		System.out.println("Added submission:"+s.id+" from "+s.author+" to "+s.problemSet+"."+s.problemId);
		notifyJudge();
	}
	
	/**
	 * Notify the first item in the queue to be judged
	 */
	public synchronized void notifyJudge(){
		if(submissions.isEmpty()){
			return;
		}
		
		for(int i=0;i<judgers.size();i++){
			Judger j=judgers.get(i);
			if(!j.isOnline()){ //kill offline judges
				System.out.println("Killed judge:"+j.name);
				judgers.remove(i);
				i--;
				continue;
			}
			if(j.isFree){
				j.work(submissions.get(0),this);
				submissions.remove(0);
				break;
			}
		}
	}
	
	public void solve(String[] args) throws Exception{
		if(args.length<1){
			System.out.println("ServerManager <port>");
			System.out.println("7512 is recommended");
			System.exit(1);
		}
		
		//debug
//		addSubmission(CommonUtil.generateBlankSubmission("XGN", "#include <iostream>\nusing namespace std;\nint main(){cout<<\"Hello,World\"<<endl;}", "cpp", 0, "testP", "testProblemSet"));
		
		port=Integer.parseInt(args[0]);
		start(port);
	}
	
	public void start(int port) throws Exception{
		ServerSocket ss=new ServerSocket(port);
		System.out.println("ServerSocket started successfully");
		while(true){
			Socket s=ss.accept();
			
			Thread t=new SocketThread(s,this);
			t.start();
		}
	}

	public synchronized void saveSubmission(Submission sub) {
		try{
			Gson gs=new Gson();
			String js=gs.toJson(sub);
			
			if(new File("submission").exists()==false){
				new File("submission").mkdirs();
			}
			
			PrintWriter pw=new PrintWriter(new File("submission/"+sub.id+".json"));
			pw.print(js);
			pw.close();
		}catch(Exception e){
			System.err.println("Cannot save submission!");
			e.printStackTrace();
		}
	}
}
