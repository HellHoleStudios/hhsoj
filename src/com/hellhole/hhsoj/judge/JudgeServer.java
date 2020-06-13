package com.hellhole.hhsoj.judge;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hellhole.hhsoj.common.FileUtil;
import com.hellhole.hhsoj.common.Language;
import com.hellhole.hhsoj.common.Problem;
import com.hellhole.hhsoj.common.Submission;
import com.hellhole.hhsoj.common.TestResult;
import com.hellhole.hhsoj.common.TestsetResult;

public class JudgeServer {

	public static void main(String[] args) {
		JudgeServer js=new JudgeServer();
		js.solve(args);
	}
	
	String ip;
	int port;
	String name;
	
	DataInputStream dis;
	DataOutputStream dos;
	
	HashMap<String,Language> langs=new HashMap<>();
	
	String callPy;
	
	Gson gs=new Gson();
	
	final int READ_LIMIT=1024;
	
	
	
	public void downloadData(String set,String id){
		
	}
	
	void remove(File data){
		if(data.isDirectory()){
			for(File x:data.listFiles()){
				remove(x);
			}
		}else{
			data.delete();
		}
	}
	
	void downloadData(File data,String path) throws Exception{
		//first remove everything
		System.out.println("Removing...");
		remove(data);
		data.mkdirs();
		
		System.out.println("Read files");
		while(true){

			long last=System.currentTimeMillis();
			
			String name=dis.readUTF();
			
			if(name.equals("$$END")){
				break;
			}
			if(name.startsWith("!")){
				System.out.println("Read folder:"+name);
				new File(path+"/"+name.substring(1)).mkdirs();
				continue;
			}
			//transfer file
			int bytes=dis.readInt();
			FileOutputStream fos=new FileOutputStream(path+"/"+name);
			byte[] by=new byte[FileUtil.BLOCK_SIZE];
			int rc=(bytes+FileUtil.BLOCK_SIZE-1)/FileUtil.BLOCK_SIZE;
			System.out.println("Reading "+name+" Expected Bytes:"+bytes+" Block count="+rc);
			for(int i=0;i<rc-1;i++){
				dis.readFully(by,0,FileUtil.BLOCK_SIZE);
//				System.out.println("Write:"+read);
				fos.write(by);
				dos.writeInt(Arrays.hashCode(by));
			}
			int extra=bytes-Math.max(0, (rc-1)*FileUtil.BLOCK_SIZE);
			dis.readFully(by,0,extra);
//			System.out.println("ExWrite:"+read);
			dos.writeInt(Arrays.hashCode(by));
			fos.write(by,0,extra);
			fos.close();
			
			long t=System.currentTimeMillis()-last;
			System.out.println("Read "+bytes+" bytes from "+name+". Took "+t+"ms");
		}
		System.out.println("Read done");
	}
	
	/**
	 * Returns true if answer is AC, false otherwise
	 * @param sub
	 * @param in
	 * @param set
	 * @return
	 * @throws Exception 
	 */
	boolean runSingleTest(Submission sub,int id,File set,Problem pr) {

		String sn=set.getName();
		
		try{
			File in=new File(set.getAbsoluteFile()+"/test"+id+".in");
			File out=new File(set.getAbsoluteFile()+"/test"+id+".out");
			
			System.out.println("Running:"+sub.id+" on "+in.getName()+" "+set.getName());
			sub.test=set.getName()+"."+in.getName();
			rollbackInfo(sub);
			
			//copy files to given path
			FileUtil.copyFile(in,new File("judge/in.txt"));
			FileUtil.copyFile(out,new File("judge/ans.txt"));
			
			//run the core
			String[] r=getLang(sub.lang).runCmd;
			ArrayList<String> cmd=new ArrayList<>();
			cmd.add(callPy);
			cmd.add("core.py");
			cmd.add(""+pr.tl);
			cmd.add(""+pr.ml);
			
			
			addAll(cmd,getLang(sub.lang).opCode);
			addAll(cmd,getLang(sub.lang).file);
			addAll(cmd,r);
			
			
//			System.out.println("Start running core with arg:"+cmd);
			ProcessBuilder pb=new ProcessBuilder(cmd);
			pb.directory(new File("judge"));
			pb.redirectOutput(new File("judge/sbout.txt"));
			Process p=pb.start();
			p.waitFor();
//			boolean asd=p.waitFor(pr.tl+10000,TimeUnit.MILLISECONDS); 
//			
//			if(!asd){
//				System.out.println("This shouldn't happen");
//				p.destroyForcibly();
//				sub.addResult(sn,new TestResult("Time Limit Exceeded", pr.tl+"","0", "[Youkai Taiji!]\nYour program is so TLE that it breaks down our judge system.\nPlease DON'T submit like this.", "", "", "",0));
//				sub.isFinal=true;
//				rollbackInfo(sub);
//				throw new Error("In order to protect your CPU, the judging thread has been shut down.");
//			}
			
			String inp=FileUtil.readFileWithLimit(in.getAbsolutePath(),READ_LIMIT);
			
			
			int v=p.exitValue();
			if(v!=0){
				sub.addResult(sn,new TestResult("Judgement Failed", 0, 0, "The sandbox returned:"+v+"\nSee 'checker exit code' on Github for detail", inp, "", 0));
				return false;
			}
			
			//core is done
			String sbout=FileUtil.readFile("judge/sbout.txt");
			String[] arg=sbout.split("\n");
			if(Integer.parseInt(arg[1])>=pr.tl+10000){
				System.out.println("[WARNING] A program is closed too late! Is the time limit too big, or the sandbox crashed?");
			}
			
			if(arg[0].equals("RE")){
				sub.addResult(sn,new TestResult("Runtime Error", arg[1],arg[2], "Exit code is "+arg[3], inp, "", "",0));
				return false;
			}
			if(arg[0].equals("RF")){
				sub.addResult(sn,new TestResult("Restrict Function", arg[1],arg[2], arg[3], inp, "", "",0));
				return false;
			}
			if(arg[0].equals("TLE")){
				sub.addResult(sn,new TestResult("Time Limit Exceeded", arg[1],arg[2], "", inp, "", "",0));
				return false;
			}
			if(arg[0].equals("MLE")){
				sub.addResult(sn,new TestResult("Memory Limit Exceeded", arg[1],arg[2], "", inp, "", "",0));
				return false;
			}
			if(arg[0].equals("UKE")){
				sub.addResult(sn,new TestResult("Judgement Failed", arg[1], arg[2], "Please send an issue with this information:"+arg[3], "", "", "",0));
				return false;
			}
			//next is compare answers
			
			String oup=FileUtil.readFileWithLimit("judge/out.txt",READ_LIMIT);
			String ans=FileUtil.readFileWithLimit("judge/ans.txt", READ_LIMIT);
			
			ProcessBuilder pb2=new ProcessBuilder("./checker","in.txt","out.txt","ans.txt","report.txt");
			pb2.directory(new File("judge"));
			Process p2=pb2.start();
			
			boolean notle=p2.waitFor(30, TimeUnit.SECONDS);
			
			if(notle){
				
				String info=FileUtil.readFileWithLimit("judge/report.txt", READ_LIMIT);
				
				if(p2.exitValue()==0){
					sub.addResult(sn,new TestResult("Accepted", arg[1],arg[2], info, inp, oup,ans, 1));
					return true;
				}else{
					if(p2.exitValue()==7){
						sub.addResult(sn,new TestResult("Point", arg[1],arg[2], info, inp, oup, ans,Float.parseFloat(info.split(" ")[0])));
						return true;
					}else{
						sub.addResult(sn,new TestResult("Wrong Answer", arg[1],arg[2], info, inp, oup, ans,0));
						return false;
					}
				}
			}else{
				sub.addResult(sn,new TestResult("Checker Time Limit Exceeded", arg[1],arg[2], "", inp, oup, ans,0));
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			sub.addResult(sn,new TestResult("Judgement Failed", 0,0, e+"", "", "", 0));
			return false;
		}
	}
	
	private void addAll(ArrayList<String> cmd, int[] x) {
		cmd.add(""+x.length);
		for(int y:x){
			cmd.add(""+y);
		}
	}

	private void addAll(ArrayList<String> cmd, String[] x) {
		cmd.add(""+x.length);
		for(String y:x){
			cmd.add(""+y);
		}
	}
	
	void runTestset(Submission sub,File set,Problem p) throws Exception{
		System.out.println("Running on testset:"+set.getName());
		
		sub.res.put(set.getName(),new TestsetResult());
		
		boolean ok=true;
//		System.out.println(p.tests);
		System.out.println("Requiring:"+p.tests.get(set.getName().trim()).requirement);
		for(String y:p.tests.get(set.getName()).requirement){
			if(!sub.res.get(y).pass){
				System.out.println(y+" did not pass! Skipped");
				ok=false;
				break;
			}
		}
		
		if(!ok){
			return;
		}
		
		boolean allClear=true;
		
		for(int id=0;;id++){
			File in=new File(set.getAbsoluteFile()+"/test"+id+".in");
			if(in.exists()==false){
				break;
			}
			
			
			boolean b=runSingleTest(sub,id,set,p);
			sub.score=sub.calcScore(p);
			
			rollbackInfo(sub);
			if(b==false){
				allClear=false;
				if(!p.tests.get(set.getName()).toEnd){
					break;
				}
			}
		}
		
		sub.res.get(set.getName()).pass=allClear;
	}
	
	void testFull(Submission sub,File data) throws Exception{
		//clean folder
		File temp=new File("judge");
		remove(temp);
		temp.mkdirs();
		
		//write to file
		FileUtil.writeFile("judge/Main."+getLang(sub.lang).ext,sub.code);
		
		//copy library
		FileUtil.copyFile(new File("lib/core.py"), new File("judge/core.py"));
		FileUtil.copyFile(new File("data/"+sub.problemSet+"_"+sub.problemId+"/checker"),new File("judge/checker"));
		
		//chmod
		System.out.println("Chmod doing");
		ProcessBuilder pb_=new ProcessBuilder("chmod","+777","checker");
		pb_.directory(new File("judge/"));
		pb_.inheritIO();
		Process p_=pb_.start();
		p_.waitFor();
		System.out.println("chmod:"+p_.exitValue());
		//compile file
		if(getLang(sub.lang).compileCmd.length==0) {
			//no need for compiling
			System.out.println("No compiling needed");
			sub.compilerInfo="This language doesn't require compilation.";
		}
		else {
			sub.test="Compiling";
			rollbackInfo(sub);
			
			System.out.println("Compiling by:"+Arrays.toString(getLang(sub.lang).compileCmd));
			ProcessBuilder pb=new ProcessBuilder(getLang(sub.lang).compileCmd);
			pb.redirectError(new File("judge/ce.txt"));
			pb.redirectOutput(new File("judge/ce.txt"));
			pb.directory(temp);
			
			Process p=pb.start();
			
			boolean notle=p.waitFor(30,TimeUnit.SECONDS);
			
			if(notle){
				sub.compilerInfo=FileUtil.readFileWithLimit("judge/ce.txt",READ_LIMIT);
			
				if(p.exitValue()!=0){
					sub.isFinal=true;
					sub.compilerInfo="Compilation Error:\n"+sub.compilerInfo;
					return;
				}
			}else{
				sub.compilerInfo="Compile took 30 seconds, which was bad enough :(";
				sub.isFinal=true;
				return;
			}
			
			System.out.println("Compile Success");
		}
		
		rollbackInfo(sub);
		
		//collect problem data
		Problem pr=gs.fromJson(FileUtil.readFile("data/"+sub.problemSet+"_"+sub.problemId+"/problem.json"), Problem.class);
		
		for(String ord:pr.order){
			File x=new File("data/"+sub.problemSet+"_"+sub.problemId+"/"+ord);
			runTestset(sub,x,pr);
		}
		
		System.out.println("Calculating final score");
		sub.score=sub.calcScore(pr);
		sub.isFinal=true;
		
	}
	
	public void rollbackInfo(Submission sub){
		try{
			String s=gs.toJson(sub);
			final int BLOCK_SIZE=65530;
			int block=(s.length()+BLOCK_SIZE-1)/BLOCK_SIZE;
			dos.writeInt(block);
			for(int i=0;i<block;i++){
				dos.writeUTF(s.substring(i*BLOCK_SIZE, Math.min(s.length(), (i+1)*BLOCK_SIZE)));
			}
		}catch(Exception e){
			System.out.println("Warning: cannot rollback info:");
			e.printStackTrace();
		}
	}
	
	Language getLang(String lang) {
		return langs.get(lang);
	}

	void testSubmission(Submission sub) throws Exception{
		
		//try to fetch data now
		
		String path="data/"+sub.problemSet+"_"+sub.problemId;
		
		File data=new File(path);
		if(data.exists()==false){
			//no data!!
			dos.writeInt(-1);
		}else{
			//read ver
			try{
				dos.writeInt(FileUtil.readProbInfo(path+"/problem.json").ver);
			}catch(Exception e){
				//even structure isn't right!
				dos.writeInt(-1);
			}
		}
		
		String isOk=dis.readUTF();
		if(isOk.equals("Update")){
			//read everything
			downloadData(data,path);
		}
		
		//start testing!!
		testFull(sub,data);
	}
	
	public void solve(String[] args){
		if(args.length<3){
			System.out.println("judge ip port name");
			System.exit(1);
		}
		
		System.out.println("Please wait... Detecting Python...");
		
		try{
			ProcessBuilder pb=new ProcessBuilder("python");
			pb.start();
			
			callPy="python";
		}catch(Exception e){
			System.out.println("Calling 'python' failed");
		}
		
		try{
			ProcessBuilder pb=new ProcessBuilder("python3");
			pb.start();
			
			callPy="python3";
		}catch(Exception e){
			System.out.println("Calling 'python3' failed");
		}
		
		if(callPy==null){
			System.out.println("No python detected!");
			System.exit(2);
		}
		System.out.println("Detected Python command:"+callPy);
		
		ip=args[0];
		port=Integer.parseInt(args[1]);
		name=args[2];
		
		try{
			Socket s=new Socket(ip, port);
			System.out.println("Successfully connected to the server");
			System.out.println("Registering...");
			
			dis=new DataInputStream(s.getInputStream());
			dos=new DataOutputStream(s.getOutputStream());
			
			dos.writeUTF("judger");
			dos.writeUTF(name);
			
			String langCode=dis.readUTF();
			System.out.println("Read language configuration file:"+langCode);

			
			Type type = new TypeToken<HashMap<String, Language>>(){}.getType();
			langs=gs.fromJson(langCode, type);
			
			System.out.println(langs);
			
			while(true){
				
				String in=dis.readUTF();
				
				Submission sub=gs.fromJson(in, Submission.class);
				
				System.out.println("Read submission:"+sub.id);
				//TODO test it
				try{
					testSubmission(sub);
					rollbackInfo(sub);
					System.out.println("Test finished successfully");
				}catch(Exception e){
					sub.compilerInfo="Judge failed:"+e;
					sub.isFinal=true;
					
					rollbackInfo(sub);
					e.printStackTrace();
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
