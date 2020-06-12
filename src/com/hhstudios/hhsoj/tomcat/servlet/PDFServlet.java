package com.hhstudios.hhsoj.tomcat.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hhstudios.hhsoj.common.Problemset;
import com.hhstudios.hhsoj.tomcat.util.TomcatHelper;

/**
 * Servlet implementation class StatementServlet
 */
@WebServlet("/requirePDF")
public class PDFServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PDFServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String set=request.getParameter("set");
		String id=request.getParameter("id");
		OutputStream out=response.getOutputStream();
		if(set==null || id==null){
			response.setStatus(403);
			return;
		}
		
		Problemset ps=TomcatHelper.getProblemset(set);
		if(ps==null || ps.stTime>System.currentTimeMillis()){
			response.setStatus(403);
			return;
		}
		
		String path=TomcatHelper.getProblemPath(set,id);
		
		File f1=new File(path+"/statement.pdf");
		
		if(f1.exists()==false){
			response.setStatus(403);
			return;
		}
		response.setContentType("application/pdf");
		response.setContentLength((int)f1.length());
//		response.setHeader("Content-Disposition", "filename=\""+f1.getName()+"\"");
		
		byte[] by=new byte[1024*1024];
		FileInputStream fis=new FileInputStream(f1);
		int len;
		while((len=fis.read(by))!=-1){
			out.write(by,0,len);
		}
		fis.close();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
