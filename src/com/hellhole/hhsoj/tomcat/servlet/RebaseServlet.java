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

import com.hellhole.hhsoj.common.Problemset;
import com.hellhole.hhsoj.tomcat.util.TomcatHelper;

/**
 * Servlet implementation class SubmitServlet
 */
@WebServlet("/rebaseS")
public class RebaseServlet extends HttpServlet {
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
		
		Problemset ps=TomcatHelper.getProblemset(id);
		if(ps==null) {
			out.print("No such problemset");
			return;
		}
		
		try {
			Socket s=new Socket("localhost", TomcatHelper.config.port);
			
			DataOutputStream dos=new DataOutputStream(s.getOutputStream());
			dos.writeUTF("rebase");
			dos.writeUTF(id);
			dos.close();
			
			s.close();
			
			out.print("OK");
		}catch(Exception e) {
			out.print("Cannot communicate to manager:"+e);
		}
	}

}
