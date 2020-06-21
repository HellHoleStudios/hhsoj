package com.hellhole.hhsoj.tomcat.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.hellhole.hhsoj.common.FileUtil;
import com.hellhole.hhsoj.common.Language;
import com.hellhole.hhsoj.common.Problem;
import com.hellhole.hhsoj.common.Problemset;
import com.hellhole.hhsoj.common.Submission;
import com.hellhole.hhsoj.common.User;

/**
 * The class to read/write tomcat stuff
 * @author XGN
 *
 */
public class TomcatHelper {
	public static Config config;
	private static Gson gs=new Gson();
	
	public static void fetchConfig(){
		if(!new File("config.json").exists()){
			System.out.println("FATAL: CANNOT FIND CONFIG FILE. SEE DOCUMENTATION FOR DETAIL");
			System.out.println("EXPECTED TO FIND IT HERE:"+new File("config.json").getAbsolutePath());
		}else{
			System.err.println("Fetch Config Data Success");
			config=gs.fromJson(FileUtil.readFile("config.json"), Config.class);
		}
		
	}
	
	public static Config getConfig() {
		if(config==null)fetchConfig();
		return config;
	}
	
	public static ArrayList<User> getUsers(){
		File fa=new File(getConfig().path+"/users");
		if(!fa.exists()){
			fa.mkdirs();
		}
		
		ArrayList<User> arr=new ArrayList<>();
		for(File sub:fa.listFiles()){
			try{
				arr.add(gs.fromJson(FileUtil.readFile(sub.getAbsolutePath()), User.class));
			}catch(Exception e){
				System.out.println("Corrupted User Information Found:"+sub);
			}
		}
		
		return arr;
	}
	public static void addUser(String user, String pass) {
		File fa=new File(getConfig().path+"/users");
		if(!fa.exists()){
			fa.mkdirs();
		}
		User x=new User();
		x.id=getUsers().size();
		x.username=user;
		x.password=pass;
		x.isAdmin=false;
		
		FileUtil.writeFile(getConfig().path+"/users/"+x.id, gs.toJson(x));
	}
	
	public static ArrayList<Problemset> getProblemsets(){
		File fa=new File(getConfig().path+"/problems");
		if(!fa.exists()){
			fa.mkdirs();
		}
		
		ArrayList<Problemset> ap=new ArrayList<>();
		for(File sub:fa.listFiles()){
			try{
				if(sub.isFile()){
					continue;
				}
				
				Problemset p=getProblemset(sub.getName());
				
				p.id=sub.getName();
				ap.add(p);
			}catch(Exception e){
				System.out.println("Corrupted P.Set Data Found:"+sub);
			}
		}
		
		return ap;
	}
	
	public static ArrayList<Problem> getAllProblems(String set){
		File fa=new File(getConfig().path+"/problems");
		if(!fa.exists()){
			fa.mkdirs();
		}
		
		ArrayList<Problem> arr=new ArrayList<>();
		File s=new File(getConfig().path+"/problems/"+set);
		if(!s.exists()){
			return arr;
		}
		
		for(File sub:s.listFiles()){
			if(sub.isDirectory()){
				Problem p=getProblem(set,sub.getName());
				
				arr.add(p);
			}
		}
		
		return arr;
	}
	
	public static Problem getProblem(String set,String id){
		try{			
			Problem p=FileUtil.readProbInfo(getProblemPath(set, id)+"/problem.json");
			p.set=set;
			p.id=id;
			return p;
		}catch(Exception e){
			return null;
		}
	}
	public static String getProblemPath(String set, String id) {
		return getConfig().path+"/problems/"+set+"/"+id;
	}
	public static Problemset getProblemset(String set) {	
		return gs.fromJson(FileUtil.readFile(getConfig().path+"/problems/"+set+"/problemset.json"), Problemset.class);
	}
	
	public static HashMap<String,Language> getLangs(){
		File fa=new File(getConfig().path+"/config");
		if(!fa.exists()){
			fa.mkdirs();
		}
		
		return FileUtil.readLang(getConfig().path+"/config/lang.json");
	}
	
	public static String getAceNames(){
		HashMap<String,Language> langs=getLangs();
		StringBuilder str=new StringBuilder();
		str.append('{');
		for(Entry<String,Language> e:langs.entrySet()) {
			str.append('"');
			str.append(e.getKey());
			str.append("\":\"");
			str.append(e.getValue().aceName);
			str.append("\",");
		}
		str.deleteCharAt(str.length()-1);
		str.append('}');
		return str.toString();
	}
	
	public static int getSubmissionCount() {
		File fa=new File(getConfig().path+"/submission");
		if(!fa.exists()){
			fa.mkdirs();
		}
		
		return fa.list().length;
	}
	
	public static ArrayList<Submission> getAllSubmissions(){
		File fa=new File(getConfig().path+"/submission");
		if(!fa.exists()){
			fa.mkdirs();
		}
		ArrayList<Submission> arr=new ArrayList<>();
		for(File sub:fa.listFiles()){
			arr.add(FileUtil.readSubmissionInfo(sub.getAbsolutePath()));
		}
		
		return arr;
	}
	
	public static Submission getSubmission(String id){
		File fa=new File(getConfig().path+"/submission");
		if(!fa.exists()){
			fa.mkdirs();
		}
		
		return FileUtil.readSubmissionInfo(getConfig().path+"/submission/"+id+".json");
	}
}
