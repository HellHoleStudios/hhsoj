package com.hellhole.hhsoj.se;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.hellhole.hhsoj.common.FileUtil;
import com.hellhole.hhsoj.common.Problemset;
import com.hellhole.hhsoj.common.StandingTable;
import com.hellhole.hhsoj.common.Submission;
import com.hellhole.hhsoj.tomcat.util.TomcatHelper;

/**
 * The main server to control everything. <br/>
 * File operations and socket connections 
 * @author XGN
 *
 */
public class ServerManager {
	private String ip;
	private int port;

	public Vector<Judger> judgers=new Vector<>();
	public ConcurrentHashMap<String,StandingTable> standings=new ConcurrentHashMap<>();
	public Vector<Submission> submissions=new Vector<>();
	
	
	public static void main(String[] args) throws Exception {
		ServerManager m=new ServerManager();
		m.solve(args);
	}
	
	public synchronized void addJudger(String name,Socket sock,DataInputStream dis,DataOutputStream dos){
		System.out.println("Added new judge:"+name+" from "+sock);
		judgers.add(new Judger(name,sock,dis,dos));
		
		notifyJudge();
	}
	
	
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
	
	public void loadStandingCache() {
		System.out.println("Reading Standing Cache");
		ArrayList<Problemset> pss=TomcatHelper.getProblemsets();
		
		File dir=new File(TomcatHelper.getConfig().path+"/cache");
		if(!dir.exists()) {
			dir.mkdirs();
			System.out.println("Created Cache Directory");
		}
		
		for(Problemset ps:pss) {
			System.out.println("Reading Standing Cache For "+ps.id);
			StandingTable table=FileUtil.readStandingTable(TomcatHelper.getConfig().path+"/cache/"+ps.id);
			if(table==null) {
				table=new StandingTable();
				table.policy=(ps.policy==null?"best":ps.policy);
				table.name=ps.name;
				table.edTime=ps.edTime;
				System.out.println("Created "+table.policy+" for "+table.name);
			}
			standings.put(ps.id, table);
		}
		
		System.out.println("Finished Reading Cache");
	}
	
	public void start(int port) throws Exception{
		//load standing cache
		loadStandingCache();
		
		ServerSocket ss=new ServerSocket(port);
		System.out.println("ServerSocket started successfully ->"+port);
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
			String path=TomcatHelper.getConfig().path+"/submission/";
			if(!(new File(path)).exists()){
				new File(path).mkdirs();
			}
			FileUtil.writeFile(path+sub.id+".json", js);
			
			//update standing
			standings.get(sub.problemSet).tryUpdate(sub.author, sub.problemId, sub);
			
			if(sub.isFinal) {
				FileUtil.writeFile(TomcatHelper.getConfig().path+"/cache/"+sub.problemSet, gs.toJson(standings.get(sub.problemSet)));
				System.out.println("Write Standing Cache of "+sub.problemSet);
			}
			
		}catch(Exception e){
			System.err.println("Cannot save submission!");
			e.printStackTrace();
		}
	}
}