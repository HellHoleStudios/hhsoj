package com.hellhole.hhsoj.tomcat.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hellhole.hhsoj.common.FileUtil;
import com.hellhole.hhsoj.common.Problemset;
import com.hellhole.hhsoj.tomcat.util.TomcatHelper;

/**
 * Servlet implementation class StatementServlet
 */
@WebServlet("/requireStatement")
public class StatementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StatementServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String set=request.getParameter("set");
		String id=request.getParameter("id");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		if(set==null || id==null){
			out.print("nope");
			return;
		}
		
		Problemset ps=TomcatHelper.getProblemset(set);
		
		if(ps.stTime>System.currentTimeMillis()){
			out.print("Problem Not Public!");
			return;
		}
		
		String path=TomcatHelper.getProblemPath(set,id);
		
		File f1=new File(path+"/statement.md");
		
		if(f1.exists()){
			out.println(FileUtil.readFile(f1.getAbsolutePath()));
		}
		
		File f2=new File(path+"/statement.pdf");
		if(f2.exists()){
			out.println("---");
			out.println("<a href=\"requirePDF?set="+set+"&id="+id+"\" target=\"_blank\">Download PDF</a>");
		}
	}

}
