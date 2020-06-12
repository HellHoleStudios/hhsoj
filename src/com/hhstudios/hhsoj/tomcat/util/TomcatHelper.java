package com.hhstudios.hhsoj.tomcat.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.hhstudios.hhsoj.common.FileUtil;
import com.hhstudios.hhsoj.common.Language;
import com.hhstudios.hhsoj.common.Problem;
import com.hhstudios.hhsoj.common.Problemset;
import com.hhstudios.hhsoj.common.Submission;
import com.hhstudios.hhsoj.common.User;

/**
 * The class to read/write tomcat stuff
 * @author XGN
 *
 */
public class TomcatHelper {
	public static Config config;
	public static Gson gs=new Gson();
	
	public static void fetchConfig(){
		if(!new File("config.json").exists()){
			System.out.println("FATAL: CANNOT FIND CONFIG FILE. SEE DOCUMENTATION FOR DETAIL");
			System.out.println("EXPECTED TO FIND IT HERE:"+new File("config.json").getAbsolutePath());
		}else{
			System.err.println("Fetch Config Data Success");
			config=gs.fromJson(FileUtil.readFile("config.json"), Config.class);
		}
		
	}
	public static ArrayList<User> getUsers(){
		if(config==null){
			fetchConfig();
		}
		File fa=new File(config.path+"/users");
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
		if(config==null){
			fetchConfig();
		}
		File fa=new File(config.path+"/users");
		if(!fa.exists()){
			fa.mkdirs();
		}
		User x=new User();
		x.id=getUsers().size();
		x.username=user;
		x.password=pass;
		x.isAdmin=false;
		
		FileUtil.writeFile(config.path+"/users/"+x.id, gs.toJson(x));
	}
	
	public static ArrayList<Problemset> getProblemsets(){
		if(config==null){
			fetchConfig();
		}
		File fa=new File(config.path+"/problems");
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
//				System.out.println(p.id);
				ap.add(p);
			}catch(Exception e){
				System.out.println("Corrupted P.Set Data Found:"+sub);
			}
		}
		
		return ap;
	}
	
	public static ArrayList<Problem> getAllProblems(String set){
		if(config==null){
			fetchConfig();
		}
		File fa=new File(config.path+"/problems");
		if(!fa.exists()){
			fa.mkdirs();
		}
		
		ArrayList<Problem> arr=new ArrayList<>();
		File s=new File(config.path+"/problems/"+set);
		if(s.exists()==false){
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
			if(config==null){
				fetchConfig();
			}
			
			Problem p=FileUtil.readProbInfo(getProblemPath(set, id)+"/problem.json");
			p.set=set;
			p.id=id;
			return p;
		}catch(Exception e){
			return null;
		}
	}
	public static String getProblemPath(String set, String id) {
		if(config==null){
			fetchConfig();
		}
		return config.path+"/problems/"+set+"/"+id;
	}
	public static Problemset getProblemset(String set) {
		if(config==null){
			fetchConfig();
		}
		
		return gs.fromJson(FileUtil.readFile(config.path+"/problems/"+set+"/problemset.json"), Problemset.class);
	}
	
	public static HashMap<String,Language> getLangs(){
		if(config==null){
			fetchConfig();
		}
		File fa=new File(config.path+"/config");
		if(!fa.exists()){
			fa.mkdirs();
		}
		
		return FileUtil.readLang(config.path+"/config/lang.json");
	}
	public static int getSubmissionCount() {
		if(config==null){
			fetchConfig();
		}
		
		File fa=new File(config.path+"/submission");
		if(!fa.exists()){
			fa.mkdirs();
		}
		
		return fa.list().length;
	}
	
	public static ArrayList<Submission> getAllSubmissions(){
		if(config==null){
			fetchConfig();
		}
		
		File fa=new File(config.path+"/submission");
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
		if(config==null){
			fetchConfig();
		}
		
		File fa=new File(config.path+"/submission");
		if(!fa.exists()){
			fa.mkdirs();
		}
		
		return FileUtil.readSubmissionInfo(config.path+"/submission/"+id+".json");
	}
}
