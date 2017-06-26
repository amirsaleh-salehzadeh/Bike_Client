package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ExportCSVServlet
 */
@WebServlet("/ExportCSVServlet")
public class ExportCSVServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ExportCSVServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void servletEnv(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ request.getParameter("fileName") + ".csv\"");
		try {
			OutputStream outputStream = response.getOutputStream();
			String outputResult = "";
			String savestr = "";
			if (!ClientAppDAO.linux)
				savestr = "c:\\BikeFiles\\race\\"
						+ request.getParameter("fileName");
			else
				savestr = "/home/pi/BikeFiles/race/"
						+ request.getParameter("fileName");
			BufferedReader br = new BufferedReader(new FileReader(savestr));
			String sCurrentLine = "";
			while ((sCurrentLine = br.readLine()) != null) {
				String[] tmp = sCurrentLine.split(",");
				for (int i = 0; i < tmp.length; i++) {
					if (i == 4) {
						SimpleDateFormat df = new SimpleDateFormat(
								"YYYY-MM-dd HH:mm:ss:SSS");
						outputResult += df.parse(tmp[i]) + ",";
					} else if (i < tmp.length - 1)
						outputResult += tmp[i] + ",";
					 else
						outputResult += tmp[i] + "\n";
				}
				// outputResult += sCurrentLine + "\n";
			}
			outputStream.write(outputResult.getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		try {
			response.sendRedirect("fileManagement.jsp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		servletEnv(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		servletEnv(request, response);
	}

}
