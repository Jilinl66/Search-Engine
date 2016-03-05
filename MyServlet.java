package cs221.project3.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs221.project3.mysqldatabase.Database;
import cs221.project3.mysqldatabase.DocumentsData;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	PrintWriter out; 
	HttpServletRequest requestGlobal;
    public MyServlet() {
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Database database = new Database();
		requestGlobal = request;
		response.setContentType("text.html;charset=UTF-8");
		out = response.getWriter();
		
		System.out.println("DOING GET");
		String keyWords = request.getParameter("keyWords").toLowerCase();
//		//Core searching algorithm
		List<Integer> resDocuments = new ArrayList<Integer>();
		List<String> result = new ArrayList<String>();
		try {
			result = database.getRanking(5, keyWords);
		} catch (SQLException e) {
			e.printStackTrace();
			noResultPage();
			return ;
		}
		for(String s :result)System.out.println(s);
			
		searchResult(result, keyWords);
		
		// retrieval data from database
//		System.out.println("DOING SELECTALL");
//		List<DocumentsData> documentsDatas = new ArrayList<DocumentsData>();
//		try {
//			documentsDatas = database.selectAll(resDocuments);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		DocumentsData D1 = new DocumentsData(1, "https://www.google.com", "text1", "html1", "anchor1", "title1");
//		DocumentsData D2 = new DocumentsData(2, "https://www.baidu.com", "text2", "html2", "anchor2", "title2");
//		documentsDatas.add(D1);
//		documentsDatas.add(D2);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	
	
	private void noResultPage(){
		String keyWords = requestGlobal.getParameter("keyWords").toLowerCase();
		out.write("<html>");
		out.write("<head>");
		out.write("<link href = \"css/bootstrap.css\" rel=\"stylesheet\">");
		out.write("</head>");
		out.write("<body>");
		out.write("<div class=\"container\">");
		out.write("<h1> Dumb Search Engine!</h1>");
			out.write("<div class=\"row\">");
				out.write("<div class=\"col-1\"></div>");
				out.write("<div class=\"col-10\">");
				out.write("<form action=\"MyServlet\">");
				out.write("<table class = \"table\">");
				out.write("<tr>");
				out.write("<td><input name = \"keyWords\" class = \"form-control\" value=\""+keyWords+"\"></td>");
				out.write("<td>");
				out.write("<button class = \"btn btn-success\" name = \"action\" value = \"search\">");
				out.write("search");
				out.write("</button>");
				out.write("</td>");
				out.write("</tr>");
				out.write("</table>");
				out.write("</form>");
				out.write("</div>");
				out.write("<div class=\"col-1\"></div>");
			out.write("</div>");

			out.write("<div class=\"row\">");
				out.write("<div class=\"col-1\"></div>");
				out.write("<div class=\"col-10\">");
					out.write("<p>No Result Found...</p>");
				out.write("</div>");
				out.write("<div class=\"col-1\"></div>");
			out.write("</div>");
			
		out.write("</div>");
		out.write("</body>");
		out.write("</html>");
	}
	
	private void searchResult(List<String> result, String keyWords){
		out.write("<html>");
		out.write("<head>");
		out.write("<link href = \"css/bootstrap.css\" rel=\"stylesheet\">");
		out.write("</head>");
		out.write("<body>");
		out.write("<div class=\"container\">");
		out.write("<h1> Dumb Search Engine!</h1>");
			out.write("<div class=\"row\">");
				out.write("<div class=\"col-1\"></div>");
				out.write("<div class=\"col-10\">");
				out.write("<form action=\"MyServlet\">");
				out.write("<table class = \"table\">");
				out.write("<tr>");
				out.write("<td><input name = \"keyWords\" class = \"form-control\" value=\""+keyWords+"\"></td>");
				out.write("<td>");
				out.write("<button class = \"btn btn-success\" name = \"action\" value = \"search\">");
				out.write("search");
				out.write("</button>");
				out.write("</td>");
				out.write("</tr>");
				out.write("</table>");
				out.write("</form>");
				out.write("</div>");
				out.write("<div class=\"col-1\"></div>");
			out.write("</div>");
			
			for(String s :result){
				out.write("<div class=\"row\">");
					out.write("<div class=\"col-1\"></div>");
					out.write("<div class=\"col-10\">");
						out.write("<p>");
						out.write("<a href=");
						out.write(s);
						out.write(">");
						out.write(s);
						out.write("</a>");
						out.write("</p>");
					out.write("</div>");
					out.write("<div class=\"col-1\"></div>");
				out.write("</div>");
			}
			
		out.write("</div>");
		out.write("</body>");
		out.write("</html>");
	}
}
