package com.hhstudios.hhsoj.tomcat.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hhstudios.hhsoj.common.User;
import com.hhstudios.hhsoj.tomcat.util.TomcatHelper;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/regS")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
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
		String user=request.getParameter("username");
		String pass=request.getParameter("password");
		if(user==null || pass==null){
			response.getWriter().append("Where're your username and password?");
			return;
		}
		
		
		ArrayList<User> usr=TomcatHelper.getUsers();
		
		for(User x:usr){
			if(x.username.equals(user) ){
				response.getWriter().append("Nope. Username exists!");
				return;
			}
		}
		
		if(user.length()>=50){
			response.getWriter().append("Not a nice username :/");
			return;
		}
		if(pass.length()>=50){
			response.getWriter().append("Ehh.. Your password is too secure?");
			return;
		}
		
		TomcatHelper.addUser(user,pass);
		response.getWriter().append("OK, now login!");
	}

}
