package com.hellhole.hhsoj.tomcat.servlet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.hellhole.hhsoj.common.FileUtil;
import com.hellhole.hhsoj.common.Problem;
import com.hellhole.hhsoj.common.Problemset;
import com.hellhole.hhsoj.common.Submission;
import com.hellhole.hhsoj.tomcat.util.TomcatHelper;

/**
 * Servlet implementation class SubmitServlet
 */
@WebServlet("/rejudgeS")
public class RejudgeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id=request.getParameter("id");
		
		PrintWriter out=response.getWriter();
		if(id==null){
			out.print("Bad argument");
			return;
		}
		
		Boolean usr=(Boolean) request.getSession().getAttribute("admin");
		if(usr==null){
			out.print("Not admin!");
			return;
		}
		
		Submission sub=TomcatHelper.getSubmission(id);
		if(sub==null) {
			out.print("Submission not found");
			return;
		}
		
		Problem p=TomcatHelper.getProblem(sub.problemSet, sub.problemId);
		if(p==null){
			out.print("Problem Not Found");
			return;
		}
		
		Problemset ps=TomcatHelper.getProblemset(sub.problemSet);
		if(ps.stTime>System.currentTimeMillis()){
			out.print("Problem Not Public");
			return;
		}
		
		try{
			if(TomcatHelper.config==null){
				TomcatHelper.fetchConfig();
			}
			
			Socket s=new Socket("localhost", TomcatHelper.config.port);
			
			Submission blank=FileUtil.generateBlankSubmission(sub.author, sub.code, sub.lang, sub.id, sub.problemId, sub.problemSet, p.ver);
			
			Gson gs=new Gson();
			String js=gs.toJson(blank);
			
			DataOutputStream dos=new DataOutputStream(s.getOutputStream());
			dos.writeUTF("submit");
			dos.writeUTF(js);
			dos.close();
			
			s.close();
			
			out.print("OK");
		}catch(Exception e){
			out.print("Cannot send to main judge:"+e);
			return;
		}
	}

}
