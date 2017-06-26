package app;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.alien.enterpriseRFID.reader.AlienReaderException;

@Path("GetWS")
public class GetService extends Application {
	// REST/GetWS/.....

	@GET
	@Path("/GetAllTags")
	@Produces("application/json")
	public String getAllTags(@QueryParam("raceNo") String raceNo,
			@QueryParam("commitDelay") int commitDelay,
			@QueryParam("autoCommit") String autoCommit,
			@QueryParam("scannerDelay") int scannerDelay,
			@QueryParam("checkPointType") String checkPointType,
			@QueryParam("checkPointNo") String checkPointNo,
			@QueryParam("rssi1") float rssi1,
			@QueryParam("rssi2") float rssi2,
			@QueryParam("strength") int strength,
			@QueryParam("isAppend") boolean isAppend) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "[]";
		if (checkPointType.contains("orma"))
			ThreadSubmitLine.type = ThreadSubmitLine.NORMAL;
		if (checkPointType.contains("tanda"))
			ThreadSubmitLine.type = ThreadSubmitLine.STANDARD;
		if (checkPointType.contains("tag"))
			ThreadSubmitLine.type = ThreadSubmitLine.STAGGERED;
		boolean autoC = false;
		if (autoCommit != null && autoCommit.equalsIgnoreCase("on"))
			autoC = true;
		ThreadSubmitLine.autoCommit = autoC;
		ThreadSubmitLine.commitDelay = commitDelay;
		ThreadSubmitLine.strength = strength;
		ThreadSubmitLine.rssi1 = rssi1;
		ThreadSubmitLine.rssi2 = rssi2;
		try {
			json = mapper.writeValueAsString(ClientAppDAO.getAllTags(
					checkPointType, checkPointNo, raceNo, isAppend));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	@GET
	@Path("/FileExist")
	@Produces("application/json")
	public String fileExist(@QueryParam("raceNo") String raceNo,
			@QueryParam("checkPointType") String checkPointType,
			@QueryParam("checkPointNo") String checkPointNo) {
		String json = "[]";
		boolean file = ClientAppDAO.fileExist(checkPointType, checkPointNo,
				raceNo);
		if (file)
			json = "{\"res\": \"true\"}";
		else
			json = "{\"res\": \"false\"}";
		return json;
	}

	@GET
	@Path("/PingMe")
	@Produces("application/json")
	public String pingMe(@QueryParam("ipAdd") String ipAdd) {
		String json = "[]";
		try {
			json = ClientAppDAO.streamTagReaderPing(ipAdd, 23, "alien",
					"password") + "";
		} catch (AlienReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	@GET
	@Path("/StartRace")
	@Produces("application/json")
	public String startRace(@QueryParam("staggered") boolean staggered,
			@QueryParam("raceNo") String raceNo,
			@QueryParam("riderGroup") String riderGroup,
			@QueryParam("checkPointType") String checkPointType,
			@QueryParam("checkPointNo") String checkPointNo) {
		if (staggered) {
			ClientAppDAO.updateAllTimes(checkPointType, checkPointNo, raceNo,
					riderGroup);
		}
		ThreadSubmitLine.type = ThreadSubmitLine.STANDARD;
		if (staggered)
			ThreadSubmitLine.type = ThreadSubmitLine.STAGGERED;
		return "[success: true]";
	}

	@GET
	@Path("/NewGroup")
	@Produces("application/json")
	public String newGroup(@QueryParam("groupId") int groupId) {
		ThreadSubmitLine.groupId = groupId;
		return "[success: true]";
	}

	@GET
	@Path("/AddLine")
	@Produces("application/json")
	public String addLine(@QueryParam("time") String time,
			@QueryParam("riderNo") String riderNo,
			@QueryParam("raceNo") String raceNo,
			@QueryParam("riderGroup") String riderGroup,
			@QueryParam("checkPointType") String checkPointType,
			@QueryParam("checkPointNo") String checkPointNo) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String today = dateFormat.format(date);
		try {
			String dateT = "";
			if (!checkPointType.contains("tag")) {
				Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(today + " " + time);
				dateT = date1.getTime() + "";
			} else
				dateT = "Waiting";
			String tmp = riderNo + "," + ClientAppDAO.getTagNo(riderNo) + ","
					+ dateT;
			if (riderGroup != null)
				tmp += "," + riderGroup;
			tmp += "\n";
			ClientAppDAO.writeAline(tmp, checkPointType, checkPointNo, raceNo);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "[]";
	}

	@GET
	@Path("/RemoveALine")
	@Produces("application/json")
	public String removeLine(@QueryParam("raceNo") String raceNo,
			@QueryParam("checkPointType") String checkPointType,
			@QueryParam("checkPointNo") String checkPointNo,
			@QueryParam("rowId") int rowId) {
		ClientAppDAO.removeAline(rowId, checkPointType, checkPointNo, raceNo);
		return "[]";
	}

	@GET
	@Path("/EditALine")
	@Produces("application/json")
	public String editALine(@QueryParam("time") String time,
			@QueryParam("riderNo") String riderNo,
			@QueryParam("raceNo") String raceNo,
			@QueryParam("riderGroup") String riderGroup,
			@QueryParam("checkPointType") String checkPointType,
			@QueryParam("checkPointNo") String checkPointNo,
			@QueryParam("rowId") int rowId) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String today = dateFormat.format(date);
		try {
			String dateT = "";
			if (!checkPointType.contains("tag")) {
				Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(today + " " + time);
				dateT = date1.getTime() + "";
			} else
				dateT = "Waiting";
			ClientAppDAO.editALine(rowId, checkPointType, checkPointNo, raceNo,
					riderNo, dateT);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "[]";
	}

	@GET
	@Path("/GetAllFiles")
	@Produces("application/json")
	public String getAllFiles(@QueryParam("sdate") String sdate,
			@QueryParam("edate") String edate, @QueryParam("name") String name,
			@QueryParam("chkName") String chkName,
			@QueryParam("chktype") int chktype) {
		return ClientAppDAO.getAllfiles(sdate, edate, name, chkName, chktype);
	}


	@GET
	@Path("/Commit")
	@Produces("application/json")
	public String commit(@QueryParam("raceNo") String raceNo,
			@QueryParam("checkPointType") String checkPointType,
			@QueryParam("checkPointNo") String checkPointNo) {
		Date d = new Date();
		ClientAppDAO.exportCSV(checkPointType, checkPointNo, raceNo);
		return "[]";
	}

	@GET
	@Path("/Finish")
	@Produces("application/json")
	public String finish(@QueryParam("raceNo") String raceNo,
			@QueryParam("checkPointType") String checkPointType,
			@QueryParam("checkPointNo") String checkPointNo) {
		ThreadSubmitLine.killer = false;
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
		ThreadSubmitLine.type = ThreadSubmitLine.NORMAL;
		// ThreadCommitReport.killer = false;
		ThreadSubmitLine.killer = false;
		for (int i = 0; i < threadArray.length; i++) {
			if (threadArray[i].getName().equalsIgnoreCase("Sleeper"))
				threadArray[i].stop();
		}
		for (int i = 0; i < threadArray.length; i++) {
			if (threadArray[i].getName().equalsIgnoreCase("threadReader")
					|| threadArray[i].getName()
							.equalsIgnoreCase("threadCommit"))
				threadArray[i].stop();
		}
		ClientAppDAO.exportCSV(checkPointType, checkPointNo, raceNo);
		return "[]";
	}

	@GET
	@Path("/RestartReading")
	@Produces("application/json")
	public String restartReading(@QueryParam("raceNo") String raceNo,
			@QueryParam("scannerDelay") int delay,
			@QueryParam("autoCommit") String autoCommit,
			@QueryParam("checkPointType") String checkPointType,
			@QueryParam("checkPointNo") String checkPointNo,
			@QueryParam("commitDelay") int commitdelay,
			@QueryParam("groupId") int groupId) {
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
		// ThreadCommitReport.killer = false;
		ThreadSubmitLine.killer = false;
		for (int i = 0; i < threadArray.length; i++) {
			if (threadArray[i].getName().equalsIgnoreCase("Sleeper"))
				threadArray[i].stop();
		}
		for (int i = 0; i < threadArray.length; i++) {
			if (threadArray[i].getName().equalsIgnoreCase("threadReader")
					|| threadArray[i].getName()
							.equalsIgnoreCase("threadCommit"))
				threadArray[i].stop();
		}
		int chpType = ThreadSubmitLine.NORMAL;
		if (checkPointType.equalsIgnoreCase("Staggered"))
			chpType = ThreadSubmitLine.STAGGERED;
		boolean autoC = false;
		if (autoCommit != null && autoCommit.equalsIgnoreCase("on"))
			autoC = true;
		try {
			ThreadSubmitLine ts = new ThreadSubmitLine(delay, raceNo, autoC,
					commitdelay, chpType, groupId, checkPointNo);
			Thread ta = new Thread(ts);
			ta.setName("threadReader");
			ta.setDaemon(true);
			ta.start();
		} catch (AlienReaderException e) {
			e.printStackTrace();
		}
		return "[]";
	}

	@GET
	@Path("/RebootPi")
	@Produces("application/json")
	public String rebootPi() {
		try {
			Runtime.getRuntime().exec("reboot");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "[]";
	}

	@GET
	@Path("/ShutdownPi")
	@Produces("application/json")
	public String shutdownPi() {
		try {
			Runtime.getRuntime().exec("shutdown -h now");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "[]";
	}

	@GET
	@Path("/RestartTomcat")
	@Produces("application/json")
	public String restartTomcat() {
		try {
			Runtime.getRuntime().exec("$CATALINA_HOME/bin/startup.sh");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "[]";
	}

}
