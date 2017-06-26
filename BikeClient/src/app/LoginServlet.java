package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	private void initServlet(HttpServletRequest request,
			HttpServletResponse response) {
		String savestr = "";
		if (!ClientAppDAO.linux)
			savestr = "c:\\BikeFiles\\conf\\credintial.txt";
		else
			savestr = "/home/pi/BikeFiles/conf/credintial.txt";
		File f = new File(savestr);
		BufferedReader br;
		try {
			if (request.getSession().getAttribute("loginSession") == null
					&& request.getParameter("username") == null) {
				response.sendRedirect("index.jsp");
			}
			if (!f.exists()){
				response.sendRedirect("index.jsp?message=Credintial File NOT FOUND");
			}
			br = new BufferedReader(new FileReader(savestr));
			String sCurrentLine = "";
			boolean login = false;
			while ((sCurrentLine = br.readLine()) != null) {
				String[] line = sCurrentLine.split(";");
				for (int i = 0; i < line.length; i++) {
					String[] usePass = line[i].split(",");
					if (usePass[0].equals(request.getParameter("username"))
							&& usePass[1].equals(request
									.getParameter("password")))
						login = true;
				}
			}
			if (login) {
				response.sendRedirect("CheckPointConfigServlet");
				request.getSession().setAttribute(
						"loginSession",
						request.getParameter("username") + ","
								+ request.getParameter("password"));
			} else
				response.sendRedirect("index.jsp?message=Invalid username or password");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		initServlet(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		initServlet(request, response);
	}

}
