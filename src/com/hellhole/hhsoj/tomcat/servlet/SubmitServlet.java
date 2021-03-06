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
@WebServlet("/submitS")
public class SubmitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String set=request.getParameter("set");
		String id=request.getParameter("id");
		String code=request.getParameter("code");
		String lang=request.getParameter("lang");
		
		PrintWriter out=response.getWriter();
		if(set==null || id==null || code==null || lang==null){
			out.print("Bad argument");
			return;
		}
		
		String usr=(String) request.getSession().getAttribute("username");
		if(usr==null){
			out.print("Login first!");
			return;
		}
		
		Problem p=TomcatHelper.getProblem(set, id);
		if(p==null){
			out.print("Problem Not Found");
			return;
		}
		
		Problemset ps=TomcatHelper.getProblemset(set);
		if(ps.stTime>System.currentTimeMillis()){
			out.print("Problem Not Public");
			return;
		}
		
		try{
			if(TomcatHelper.config==null){
				TomcatHelper.fetchConfig();
			}
			
			Socket s=new Socket("localhost", TomcatHelper.config.port);
			
			Submission blank=FileUtil.generateBlankSubmission(usr, code, lang, TomcatHelper.getSubmissionCount(), id, set);
			
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
