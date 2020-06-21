package com.hellhole.hhsoj.se;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import com.google.gson.Gson;
import com.hellhole.hhsoj.common.FileUtil;
import com.hellhole.hhsoj.common.MiscUtil;
import com.hellhole.hhsoj.common.Problem;
import com.hellhole.hhsoj.common.Submission;
import com.hellhole.hhsoj.tomcat.util.TomcatHelper;

/**
 * A judging thread to communicate about judging
 * @author XGN
 *
 */
public class JudgingThread extends Thread {
	public Submission sub;
	public Judger j;
	public ServerManager boss;
	public JudgingThread(Submission sub, Judger j, ServerManager boss) {
		this.sub = sub;
		this.j = j;
		this.boss = boss;
	}
	
	public void run(){
		System.out.println("Start to judge "+sub.id+" on "+j.name);
		sub.judger=j.name;
		boss.saveSubmission(sub);
		
		try{
			//send information
			Gson gs=new Gson();
			j.dos.writeUTF(gs.toJson(sub));
			
			String path=TomcatHelper.getConfig().path+"/problems/"+sub.problemSet+"/"+sub.problemId;
			
			Problem p=FileUtil.readProbInfo(path+"/problem.json");
			
			int clientVer=j.dis.readInt();
			
			System.out.println(clientVer+"<>"+p.ver);
			if(clientVer!=p.ver){
				System.out.println("Need to update client information");
				j.dos.writeUTF("Update");
				
				//send everything
				//no nested folders are expected, thus won't be sent
				File f=new File(path);
				for(File x:f.listFiles()){
					if(x.isDirectory()){
						j.dos.writeUTF("!"+x.getName());
						
						for(File y:x.listFiles()){
							sendFile(x,y);
						}
					}else{
						sendFile(null,x);
					}
				}
				
				j.dos.writeUTF("$$END");
			}else{
				j.dos.writeUTF("OK");
			}
			
			while(true){
				String s="";
				
				int r=j.dis.readInt();
				for(int i=0;i<r;i++){
					s+=j.dis.readUTF();
				}
				sub=gs.fromJson(s, Submission.class);
				boss.saveSubmission(sub);
//				System.out.println("Received:"+gs.toJson(sub));
				if(sub.isFinal){
					break;
				}
			}
			
			System.out.println("Judging has done for "+sub.id);
			j.isFree=true;
			boss.notifyJudge();
		}catch(EOFException e){
			//client really sleeping
			System.out.println("EOF reached! Is client f**ked?");
			e.printStackTrace();
			
			sub.test="Waiting for rejudge";
			sub.compilerInfo="Nah, the judger is sleeping. Please waiting for rejudge and report this issue to the server admin.";
			boss.saveSubmission(sub);
			
			boss.addSubmission(sub); //rejudge
			
			j.dead=true;
		}catch(Exception e){
			System.out.println("Communication failed");
			e.printStackTrace();
			
			sub.test="Error";
			sub.compilerInfo="Contact server admin :(";
			boss.saveSubmission(sub);
		}
	}

	private void sendFile(File x, File y) throws IOException {
		j.dos.writeUTF((x==null?"":x.getName()+"/")+y.getName());
		int snd=(int)y.length();
		j.dos.writeInt(snd);
				
		byte[] by=new byte[FileUtil.BLOCK_SIZE];
		FileInputStream fis=new FileInputStream(y);
		int len=0;
		int totsend=0,rbc=0; //verify purpose only
		
		int bc=(snd+FileUtil.BLOCK_SIZE-1)/FileUtil.BLOCK_SIZE;
		System.out.println("Start sending "+y+" with expected block count="+bc);
		
		while((len=fis.read(by, 0, FileUtil.BLOCK_SIZE))!=-1){
//			System.out.println("Send"+len);
			
			if(len==0)break;
			
			//whenever send a string, needs rollback to confirm it's correctly received.
			j.dos.write(by,0,len);
			int hash=j.dis.readInt();
			int expected=Arrays.hashCode(by);
//			System.out.println("Read hash:"+hash+" Expected:"+expected);
			
			MiscUtil.assertEql(hash,expected);
			
			totsend+=len;
			
			rbc++;
		}
		fis.close();
		
		MiscUtil.assertEql(snd,totsend);
		MiscUtil.assertEql(bc, rbc);
		System.out.println("Send file "+y+" of size "+snd);
		
	}
}
