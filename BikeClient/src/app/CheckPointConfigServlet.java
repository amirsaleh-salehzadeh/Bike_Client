package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alien.enterpriseRFID.reader.AlienReaderException;

/**
 * Servlet implementation class CheckPointConfigServlet
 */
@WebServlet("/CheckPointConfigServlet")
public class CheckPointConfigServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckPointConfigServlet() {
		super();
		// TODO Auto-generated constructor stub
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

	private void initServlet(HttpServletRequest request,
			HttpServletResponse response) {
		BufferedReader br;
		if (request.getSession().getAttribute("loginSession") == null) {
			try {
				response.sendRedirect("index.jsp");
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			String savestr = "";
			if (ClientAppDAO.linux)
				savestr = "/home/pi/BikeFiles/conf/readerip.txt";
			else
				savestr = "c:\\BikeFiles\\conf\\readerip.txt";
			File f = new File(savestr);
			if (!f.exists())
				response.sendRedirect("checkpointConfig.jsp?message=Configuration File NOT FOUND");
			br = new BufferedReader(new FileReader(savestr));
			String sCurrentLine = "";
			boolean readerStat = false;
			if ((sCurrentLine = br.readLine()) != null) {
				try {
					readerStat = ClientAppDAO.streamTagReaderPing(sCurrentLine,
							23, "alien", "password");
				} catch (AlienReaderException e) {
					e.printStackTrace();
					readerStat = false;
				}
				if (request.getParameter("message") != null)
					try {
						response.sendRedirect("checkpointConfig.jsp?message="
								+ request.getParameter("message") + "&ipadd="
								+ sCurrentLine + "&readerStat=" + readerStat);
						return;
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				else
					response.sendRedirect("checkpointConfig.jsp?ipadd="
							+ sCurrentLine + "&readerStat=" + readerStat);
			} else
				response.sendRedirect("checkpointConfig.jsp?ipadd=0.0.0.0&readerStat="
						+ readerStat);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
