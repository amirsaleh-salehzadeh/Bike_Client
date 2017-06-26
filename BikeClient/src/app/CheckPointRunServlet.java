package app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alien.enterpriseRFID.reader.AlienReaderException;

/**
 * Servlet implementation class CheckpointRun
 */
@WebServlet("/CheckPointRunServlet")
public class CheckPointRunServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckPointRunServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		initServlet(request, response);
	}

	private void initServlet(HttpServletRequest request,
			HttpServletResponse response) {
		if (request.getSession().getAttribute("loginSession") == null) {
			try {
				response.sendRedirect("index.jsp");
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Enumeration<String> parameterNames = request.getParameterNames();
		String queryString = "";
		boolean autoCommit = false;
		if (request.getParameter("autocommit") != null
				&& request.getParameter("autocommit").equalsIgnoreCase("on"))
			autoCommit = true;
		try {
			int checkpointType = ThreadSubmitLine.NORMAL;
			if (request.getParameter("checkPointType").equalsIgnoreCase(
					"Staggered"))
				checkpointType = ThreadSubmitLine.STAGGERED;
			ThreadSubmitLine ts = new ThreadSubmitLine(Integer.parseInt(request
					.getParameter("delay")),
					request.getParameter("raceNumber"), autoCommit,
					Integer.parseInt(request.getParameter("commitdelay")),
					checkpointType, 1, request.getParameter("checkpointNo"));
			if (!ts.killer) {
				try {
					response.sendRedirect("CheckPointConfigServlet?message= Unable to connect to the reader");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			Thread ta = new Thread(ts);
			ta.setName("threadReader");
			ta.setDaemon(true);
			ta.start();
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (AlienReaderException e1) {
			e1.printStackTrace();
			try {
				response.sendRedirect("CheckPointConfigServlet?message=Unable to connect to the reader");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// forwarding the parameters to runCheckpoint.jsp
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			for (String paramValue : request.getParameterValues(paramName)) {
				try {
					queryString += URLEncoder.encode(paramName, "UTF-8") + "="
							+ URLEncoder.encode(paramValue, "UTF-8") + "&";
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		if (queryString.length() > 0) {
			queryString = "?"
					+ (queryString.substring(0, queryString.length() - 1));
		}
		try {
			response.sendRedirect("runCheckpoint.jsp" + queryString);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
