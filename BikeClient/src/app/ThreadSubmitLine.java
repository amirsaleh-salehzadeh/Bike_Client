package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.alien.enterpriseRFID.reader.AlienClass1Reader;
import com.alien.enterpriseRFID.reader.AlienReaderConnectionException;
import com.alien.enterpriseRFID.reader.AlienReaderConnectionRefusedException;
import com.alien.enterpriseRFID.reader.AlienReaderException;
import com.alien.enterpriseRFID.reader.AlienReaderNotValidException;
import com.alien.enterpriseRFID.reader.AlienReaderTimeoutException;
import com.alien.enterpriseRFID.tags.Tag;

public class ThreadSubmitLine implements Runnable {
	public static volatile boolean killer;
	public final static int STANDARD = 1;
	public final static int STAGGERED = 2;
	public final static int NORMAL = 0;
	private static String checkPointType;
	private int port = 23;
	private String ip;
	private static String checkPointName;
	private String username = "alien";
	private String password = "password";
	public static float rssi1;
	public static float rssi2;
	public static int strength;
	public static String mac = "";
	public static AlienClass1Reader reader = null;
	private static int scanDelay;
	public static int commitDelay;
	public static boolean autoCommit;
	private static String raceNo;
	public static int type = 0;
	public static int groupId = 1;
	public static HashMap<String, String> map;
	private static ThreadCommitReport tc;

	public static String addMe(String key, String value) {
		String line = "Auto Scan," + key.toString() + "," + value.toString()
				+ "," + groupId + "\n";
		if (type == STAGGERED && !map.containsKey(key)) {
			ClientAppDAO.writeAline("Auto Scan," + key.toString() + ",Waiting,"
					+ groupId + "\n", checkPointType, checkPointName, raceNo);
			return map.put(key, "Waiting");
		} else if (type == STANDARD) {
			if (!map.containsKey(key)) {
				ClientAppDAO.writeAline(line, checkPointType, checkPointName,
						raceNo);
				return map.put(key, value);
			} else if (!map.get(key).equalsIgnoreCase("Waiting")
					&& value.compareTo(Long.parseLong(map.get(key))
							+ ((scanDelay) * 1000) + "") > 0) {
				line = "Auto Scan," + key.toString() + "," + value.toString()
						+ "," + groupId + "\n";
				ClientAppDAO.writeAline(line, checkPointType, checkPointName,
						raceNo);
				return map.put(key, value);
			} else
				return null;
		} else
			return null;

	}

	public ThreadSubmitLine(int scanDelay, String raceNo, boolean autoCommit,
			int commitDelay, int types, int groupId, String checkPointNo)
			throws AlienReaderException {
		super();
		tc = null;
		this.rssi1 = 0;
		this.rssi2 = 10000;
		this.strength = 0;
		this.killer = true;
		this.raceNo = raceNo;
		this.checkPointName = checkPointNo;
		String savestr = "";
		if (!ClientAppDAO.linux)
			savestr = "c:\\BikeFiles\\conf\\readerip.txt";
		else
			savestr = "/home/pi/BikeFiles/conf/readerip.txt";
		BufferedReader br;
		this.scanDelay = scanDelay;
		this.autoCommit = autoCommit;
		this.commitDelay = commitDelay;
		this.type = types;
		String chpnType = "";
		if (type == 0 || type == 1)
			this.checkPointType = "Standard";
		else
			this.checkPointType = "Staggered";
		this.groupId = groupId;
		try {
			br = new BufferedReader(new FileReader(savestr));
			String sCurrentLine = "";
			try {
				if ((sCurrentLine = br.readLine()) != null) {
					this.ip = sCurrentLine;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		map = new HashMap<String, String>();
		reader = new AlienClass1Reader();
		try {
			reader.setConnection(ip, port);
			reader.setUsername(username);
			reader.setPassword(password);
			// reader.autoModeTriggerNow();
			 reader.resetAutoMode();
			reader.clearTagList();
			reader.open();
		} catch (AlienReaderConnectionRefusedException e) {
			e.printStackTrace();
		} catch (AlienReaderNotValidException e) {
			e.printStackTrace();
		} catch (AlienReaderTimeoutException e) {
			reader.close();
			this.killer = false;
			Thread.currentThread().interrupt();
			e.printStackTrace();
			return;
		} catch (AlienReaderConnectionException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void run() {
		Thread ta = null;
		while (killer && !Thread.currentThread().isInterrupted()) {
			try {
				reader.setRSSIFilter(rssi1, rssi2);
				reader.setRFAttenuation(strength);
				Tag[] tags = reader.getTagList(6);
				if (tags != null || reader.getReaderReply() != null) {
					if (tags != null)
						for (int i = 0; i < tags.length; i++) {
							long millis = System.currentTimeMillis();
							addMe(tags[i].getTagID(), millis + "");
						}
					else {
						String a[] = reader.getReaderReply().split("\n");
						if (!a[0].contains("No Tag") && a[0].length() > 20) {
							for (int i = 0; i < a.length; i++) {
								String tag = a[i];
								tag = tag.substring(tag.indexOf(":") + 1,
										tag.indexOf(","));
								String time = a[i].substring(
										a[i].indexOf("Last:") + 5,
										a[i].indexOf(", Count"));
								long millis = System.currentTimeMillis();
								addMe(tag, millis + "");
							}
						} else
							reader.autoModeReset();
					}
				}
				if (!autoCommit && tc != null) {
					Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
					Thread[] threadArray = threadSet
							.toArray(new Thread[threadSet.size()]);
					// tc.killer = false;
					for (int i = 0; i < threadArray.length; i++) {
						if (threadArray[i].getName()
								.equalsIgnoreCase("Sleeper"))
							threadArray[i].stop();
					}
					for (int i = 0; i < threadArray.length; i++) {
						if (threadArray[i].getName().equalsIgnoreCase(
								"threadCommit"))
							threadArray[i].stop();
					}
					tc = null;
					ta = null;
				}
				if (autoCommit && tc != null)
					tc.commitDelay = commitDelay;
				if (autoCommit && tc == null) {
					tc = new ThreadCommitReport(raceNo, commitDelay, true,
							checkPointName, checkPointType);
					ta = new Thread(tc);
					ta.setName("threadCommit");
					ta.setDaemon(true);
					ta.start();
				}

			} catch (AlienReaderException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
		reader.close();
		// if (ta != null)
		// ta.stop();
		Thread.currentThread().interrupt();
	}

	public static void main(String[] args) {
		// try {
		// ThreadSubmitLine ts = new ThreadSubmitLine(10, "hi");
		// ts.addMe("1", "hi");
		// ts.addMe("2", "hii");
		// ts.addMe("1", "hiii");
		// System.out.println("done");
		// } catch (AlienReaderException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}