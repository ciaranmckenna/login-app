package com.ciaranmckenna.isc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(urlPatterns = "/loginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
       
	public void init(ServletConfig config) {
		try {
			ServletContext context = config.getServletContext();
			Enumeration<String> parameterNames = context.getInitParameterNames();
			
			while(parameterNames.hasMoreElements()) {
				String eachName = (String) parameterNames.nextElement();
				System.out.println("Contxt param name: " + eachName);
				System.out.println("Context param value: " + context.getInitParameter(eachName));
			}
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(context.getInitParameter("dbUrl"), context.getInitParameter("dbUser"),
					context.getInitParameter("dbPassword"));
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from user where email='"+ userName + "' and password='" + password + "'");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("homeServlet");
			if(resultSet.next()) {
				request.setAttribute("message", "Welcome to Interservlet Communication " + userName);
				requestDispatcher.forward(request, response);
			}else {
				requestDispatcher = request.getRequestDispatcher("login.html");
				requestDispatcher.include(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
