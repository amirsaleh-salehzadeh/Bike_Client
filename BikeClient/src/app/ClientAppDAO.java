package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.alien.enterpriseRFID.reader.AlienClass1Reader;
import com.alien.enterpriseRFID.reader.AlienReaderException;

public class ClientAppDAO {
	public static final String SERVERLOCATION = "http://localhost:8080/BikeTracking/";
	public static final String DEVICENAME = "CHECKPOINT1";
	public static boolean linux = false;

	// public static int register() {
	// try {
	// InetAddress ip;
	// String ipAdd = "";
	// String macAdd = "";
	// try {
	// ip = InetAddress.getLocalHost();
	// NetworkInterface network = NetworkInterface
	// .getByInetAddress(ip);
	// ipAdd = ip.getHostAddress();
	// byte[] mac = network.getHardwareAddress();
	// System.out.print("Current MAC address : ");
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i < mac.length; i++) {
	// sb.append(String.format("%02X%s", mac[i],
	// (i < mac.length - 1) ? "-" : ""));
	// macAdd += String.format("%02X%s", mac[i],
	// (i < mac.length - 1) ? "-" : "");
	// }
	// System.out.println(macAdd);
	// } catch (UnknownHostException e) {
	// e.printStackTrace();
	// } catch (SocketException e) {
	// e.printStackTrace();
	// }
	// String tmpURL = SERVERLOCATION + "REST/GetWS/RegisterMe?ip="
	// + ipAdd + "&mac=" + macAdd + "&name=" + DEVICENAME;
	// URL url = new URL(tmpURL);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setRequestMethod("GET");
	// conn.setRequestProperty("Accept", "application/json");
	// if (conn.getResponseCode() != 200) {
	// throw new RuntimeException("Failed : HTTP error code : "
	// + conn.getResponseCode());
	// }
	// BufferedReader br = new BufferedReader(new InputStreamReader(
	// (conn.getInputStream())));
	// String output = "";
	// System.out.println("Output from Server .... \n");
	// try {
	// PrintWriter writer = new PrintWriter("info.txt", "UTF-8");
	// while ((output = br.readLine()) != null) {
	// writer.println(output);
	// writer.close();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// conn.disconnect();
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return 0;
	// }

	// public static void pingReaders(ArrayList<CheckPointENT> chks) {
	// try {
	// for (int i = 0; i < chks.size(); i++) {
	// checkReaders(streamTagReaderPing(chks.get(i).getIp(),
	// chks.get(i).getPort(), chks.get(i).getUsername(), chks
	// .get(i).getPassword())+"");
	// }
	// } catch (AlienReaderException e) {
	// // e.printStackTrace();
	// }
	// }

	public static boolean streamTagReaderPing(String ip, int port,
			String uname, String pass) throws AlienReaderException {
		AlienClass1Reader reader = new AlienClass1Reader();
		reader.setConnection(ip, port);
		reader.setUsername(uname);
		reader.setPassword(pass);
		String mac = "";
		try {
			reader.open();
			mac = reader.getMACAddress();
			reader.close();
			return true;
			// return mac + ",true";
		} catch (AlienReaderException e) {
			// e.printStackTrace();
			reader.close();
			// return ip + ",false";
			return false;
		}
	}

	// public static ArrayList<CheckPointENT> getAllCheckPoints() {
	// ArrayList<CheckPointENT> obj = new ArrayList<>();
	// try {
	// ObjectMapper mapper = new ObjectMapper();
	// String tmpURL = SERVERLOCATION +
	// "REST/GetWS/GetCheckPoints?data=something";
	// URL url = new URL(tmpURL);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setRequestMethod("GET");
	// conn.setRequestProperty("Accept", "application/json");
	// conn.setConnectTimeout(5000);
	// if (conn.getResponseCode() != 200) {
	// conn.disconnect();
	// throw new RuntimeException("Failed : HTTP error code : "
	// + conn.getResponseCode());
	// }
	// BufferedReader br = new BufferedReader(new InputStreamReader(
	// (conn.getInputStream())));
	// String output;
	// while ((output = br.readLine()) != null) {
	// obj = mapper.readValue(
	// output,
	// mapper.getTypeFactory().constructCollectionType(
	// List.class, CheckPointENT.class));
	// }
	// conn.disconnect();
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return obj;
	// }

	// public static int submitTag(String tagCode, String time, String mac) {
	// try {
	// if (time == null)
	// time = System.currentTimeMillis() + "";
	// String tmpURL = SERVERLOCATION + "REST/GetWS/SubmitLine?tag="
	// + tagCode //
	// + "&time=" + time + "&mac=" + mac;
	// URL url = new URL(tmpURL);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setRequestMethod("GET");
	// conn.setRequestProperty("Accept", "application/json");
	// if (conn.getResponseCode() != 200) {
	// conn.disconnect();
	// throw new RuntimeException("Failed : HTTP error code : "
	// + conn.getResponseCode());
	// }
	// BufferedReader br = new BufferedReader(new InputStreamReader(
	// (conn.getInputStream())));
	// String output;
	// System.out.println("Output from Server .... \n");
	// while ((output = br.readLine()) != null) {
	// System.out.println(output);
	// }
	// conn.disconnect();
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return 0;
	// }

	// public static void checkReaders(String message) {
	// try {
	// System.out.println(message);
	// String tmpURL = SERVERLOCATION + "REST/GetWS/PingPoints?message="
	// + message;
	// URL url = new URL(tmpURL);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setRequestMethod("GET");
	// conn.setRequestProperty("Accept", "application/json");
	// conn.setReadTimeout(5000);
	// if (conn.getResponseCode() != 200) {
	// conn.disconnect();
	// throw new RuntimeException("Failed : HTTP error code : "
	// + conn.getResponseCode());
	// }
	// BufferedReader br = new BufferedReader(new InputStreamReader(
	// (conn.getInputStream())));
	// String output;
	// System.out.println("Output from Server .... \n");
	// while ((output = br.readLine()) != null) {
	// System.out.println(output);
	// }
	// conn.disconnect();
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	public static void writeAline(String line, String checkpointType,
			String checkPointNo, String RaceName) {

		String savestr = "";
		if (!linux)
			savestr = "c:\\BikeFiles\\race\\"
					+ getFileName(checkpointType, checkPointNo, RaceName)
					+ ".txt";
		else
			savestr = "/home/pi/BikeFiles/race/"
					+ getFileName(checkpointType, checkPointNo, RaceName)
					+ ".txt";
		File f = new File(savestr);
		try {
			if (!f.exists())
				f.createNewFile();
			BufferedReader br = new BufferedReader(new FileReader(savestr));
			String sCurrentLine = "";
			int counter = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				counter++;
			}
			line = counter + ",+," + line;
			FileOutputStream oFile = new FileOutputStream(f, true);
			oFile.write(line.getBytes());
			oFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getFileName(String checkpointType,
			String checkPointNo, String RaceName) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String fileName = dateFormat.format(date) + "-" + "RACE_" + RaceName
				+ "-";
		if (checkpointType.contains("tag"))
			fileName += "CHKPSTAG_";
		else
			fileName += "CHKPSTND_";
		fileName += checkPointNo;
		String savestr = "";
		if (!linux)
			savestr = "c:\\BikeFiles\\race\\";
		else
			savestr = "/home/pi/BikeFiles/race/";
		File folder = new File(savestr);
		int counter = 0;
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()
					&& listOfFiles[i].getName().contains(fileName)) {
				counter++;
			}
		}
		if (counter > 0)
			counter--;
		fileName += "_" + counter;
		return fileName;
	}

	public static ArrayList<RideEntity> getAllTags(String checkpointType,
			String checkPointNo, String RaceName, boolean isAppend) {
		ArrayList<RideEntity> res = new ArrayList<>();
		ArrayList<RideEntity> resT = new ArrayList<>();
		String savestr = "";
		if (!linux)
			savestr = "c:\\BikeFiles\\race\\"
					+ getFileName(checkpointType, checkPointNo, RaceName)
					+ ".txt";
		else
			savestr = "/home/pi/BikeFiles/race/"
					+ getFileName(checkpointType, checkPointNo, RaceName)
					+ ".txt";
		BufferedReader br;
		File f = new File(savestr);
		try {
			if (!f.exists())
				f.createNewFile();
			else if (!isAppend) {
				String tmp = f.getName();
				int fileNo = Integer.parseInt(tmp.substring(
						tmp.indexOf(".txt") - 1, tmp.indexOf(".txt")));
				tmp = tmp.replace(fileNo + ".txt", (fileNo + 1) + ".txt");
				if (!linux)
					savestr = "c:\\BikeFiles\\race\\" + tmp;
				else
					savestr = "/home/pi/BikeFiles/race/" + tmp;
				f = new File(savestr);
				f.createNewFile();
			}
			br = new BufferedReader(new FileReader(savestr));
			String sCurrentLine = "";
			while ((sCurrentLine = br.readLine()) != null) {
				RideEntity r = new RideEntity();
				String[] line = sCurrentLine.split("\n");
				for (int i = 0; i < line.length; i++) {
					String[] usePass = line[i].split(",");
					if (usePass[1].equalsIgnoreCase("#"))
						continue;
					r.setRiderNo(usePass[2]);
					r.setTag(usePass[3]);
					r.setTime(usePass[4]);
					r.setId(Integer.parseInt(usePass[0]));
					if (usePass.length > 4)
						r.setGroup(usePass[5]);
					if (usePass[4].length() > 8 && !usePass[4].contains("n")) {
						long t = Long.parseLong(usePass[4]);
						Date d = new Date(t);
						DateFormat formatter = new SimpleDateFormat(
								"HH:mm:ss.SSS");
						String dateFormatted = formatter.format(d);
						r.setTimePresentation(dateFormatted);
					} else
						r.setTimePresentation(usePass[4]);
					resT.add(r);
				}
			}
			for (int i = resT.size() - 1; i >= 0; i--) {
				res.add(resT.get(i));
			}
			// Collections.sort(res, Collections.reverseOrder());
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	public static String exportCSV(String checkpointType, String checkPointNo,
			String RaceName) {
		String res = "";
		String savestr = "";
		if (!linux)
			savestr = "c:\\BikeFiles\\race\\"
					+ getFileName(checkpointType, checkPointNo, RaceName)
					+ ".txt";
		else
			savestr = "/home/pi/BikeFiles/race/"
					+ getFileName(checkpointType, checkPointNo, RaceName)
					+ ".txt";
		BufferedReader br;
		try {
			// if (!f.exists())
			// f.createNewFile();
			br = new BufferedReader(new FileReader(savestr));
			String sCurrentLine = "";
			while ((sCurrentLine = br.readLine()) != null) {
				res += sCurrentLine;
			}
			if (res.length() > 3)
				System.out.println(res);
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	public static String getTagNo(String string) {
		return "Manual Input";
	}

	public static void updateAllTimes(String checkpointType,
			String checkPointNo, String RaceName, String riderGroup) {
		String savestr = "";
		if (!linux)
			savestr = "c:\\BikeFiles\\race\\"
					+ getFileName(checkpointType, checkPointNo, RaceName)
					+ ".txt";
		else
			savestr = "/home/pi/BikeFiles/race/"
					+ getFileName(checkpointType, checkPointNo, RaceName)
					+ ".txt";
		BufferedReader br;
		File f = new File(savestr);
		try {
			br = new BufferedReader(new FileReader(savestr));
			String sCurrentLine = "";
			String tmp = "";
			while ((sCurrentLine = br.readLine()) != null) {
				RideEntity r = new RideEntity();
				String[] line = sCurrentLine.split(";");
				for (int i = 0; i < line.length; i++) {
					String[] usePass = line[i].split(",");
					r.setRiderNo(usePass[0]);
					r.setTag(usePass[1]);
					r.setTime(usePass[2]);
					if (usePass.length > 3) {
						if (usePass[3].equalsIgnoreCase(riderGroup))
							tmp += usePass[0] + "," + usePass[1] + ","
									+ System.currentTimeMillis() + ","
									+ riderGroup + "\n";
						else
							tmp += line[i] + "\n";
					} else
						tmp = line[i] + "\n";
				}
			}
			br.close();
			f.delete();
			f.createNewFile();
			FileOutputStream oFile = new FileOutputStream(f, false);
			oFile.write(tmp.getBytes());
			oFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean fileExist(String checkpointType, String checkPointNo,
			String RaceName) {
		String savestr = "";
		if (!linux)
			savestr = "c:\\BikeFiles\\race\\";
		else
			savestr = "/home/pi/BikeFiles/race/";
		File folder = new File(savestr);
		boolean res = false;
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()
					&& listOfFiles[i].getName()
							.contains(
									getFileName(checkpointType, checkPointNo,
											RaceName))) {
				res = true;
			}
		}
		return res;
	}

	public static void removeAline(int rowId, String checkPointType,
			String checkPointNo, String raceNo) {
		String savestr = "";
		if (!linux)
			savestr = "c:\\BikeFiles\\race\\"
					+ getFileName(checkPointType, checkPointNo, raceNo)
					+ ".txt";
		else
			savestr = "/home/pi/BikeFiles/race/"
					+ getFileName(checkPointType, checkPointNo, raceNo)
					+ ".txt";
		File f = new File(savestr);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(savestr));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String sCurrentLine = "";
		String str = "";
		try {
			while ((sCurrentLine = br.readLine()) != null) {
				String[] line = sCurrentLine.split("\n");
				for (int i = 0; i < line.length; i++) {
					String[] usePass = line[i].split(",");
					if (usePass[0].equalsIgnoreCase(rowId + "")) {
						String tmp = "";
						for (int j = 0; j < usePass.length; j++) {
							if (j != 1 && j != usePass.length - 1)
								tmp += usePass[j] + ",";
							else if (j == 1)
								tmp += "#,";
							else
								tmp += usePass[j];
						}
						str += tmp + "\n";
					} else
						str += line[i] + "\n";
				}
			}
			f.delete();
			f.createNewFile();
			FileOutputStream oFile = new FileOutputStream(f, false);
			oFile.write(str.getBytes());
			oFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void editALine(int rowId, String checkPointType,
			String checkPointNo, String raceNo, String riderName, String time) {
		String savestr = "";
		if (!linux)
			savestr = "c:\\BikeFiles\\race\\"
					+ getFileName(checkPointType, checkPointNo, raceNo)
					+ ".txt";
		else
			savestr = "/home/pi/BikeFiles/race/"
					+ getFileName(checkPointType, checkPointNo, raceNo)
					+ ".txt";
		File f = new File(savestr);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(savestr));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String sCurrentLine = "";
		String str = "";
		try {
			while ((sCurrentLine = br.readLine()) != null) {
				String[] line = sCurrentLine.split("\n");
				for (int i = 0; i < line.length; i++) {
					String[] usePass = line[i].split(",");
					if (usePass[0].equalsIgnoreCase(rowId + "")) {
						String tmp = "";
						for (int j = 0; j < usePass.length; j++) {
							if (j != 2 && j != 4 && j != usePass.length - 1)
								tmp += usePass[j] + ",";
							else if (j == 2)
								tmp += riderName + ",";
							else if (j == 4)
								tmp += time + ",";
							else
								tmp += usePass[j];
						}
						str += tmp + "\n";
					} else
						str += line[i] + "\n";
				}
			}
			f.delete();
			f.createNewFile();
			FileOutputStream oFile = new FileOutputStream(f, false);
			oFile.write(str.getBytes());
			oFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String getAllfiles(String sdate, String edate, String name,
			String chkName, int chktype) {
		try {
			Date UIFormat = new SimpleDateFormat("MM/dd/yyyy").parse(sdate);
			sdate = new SimpleDateFormat("yyyy-MM-dd").format(UIFormat);
			UIFormat = new SimpleDateFormat("MM/dd/yyyy").parse(edate);
			edate = new SimpleDateFormat("yyyy-MM-dd").format(UIFormat);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String savestr = "";
		if (!linux)
			savestr = "c:\\BikeFiles\\race\\";
		else
			savestr = "/home/pi/BikeFiles/race/";
		File folder = new File(savestr);
		File[] listOfFiles = folder.listFiles();
		String resJson = "[";
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String[] fName = listOfFiles[i].getName().split("-");
				String date = fName[0] + "-" + fName[1] + "-" + fName[2];
				if (date.compareTo(sdate) >= 0 && date.compareTo(edate) <= 0) {
					if (name != null && name != "" && !fName[3].contains(name)) {
						continue;
					} else if (chkName != null && chkName != ""
							&& !fName[4].split("_")[1].contains(chkName)) {
						continue;
					} else if (chktype == 1 && !fName[4].contains("STND")) {
						continue;
					} else if (chktype == 2 && !fName[4].contains("STAG")) {
						continue;
					} else {
						resJson += "{\"dateTime\":\""
								+ date
								+ "\", "
								+ "\"race\":\""
								+ fName[3].split("_")[1]
								+ "\", "
								+ "\"checkpointName\":\""
								+ fName[4].split("_")[1]
								+ "_"
								+ fName[4].split("_")[2].substring(0,
										fName[4].split("_")[2].indexOf(".txt"))
								+ "\", \"fileName\":\""
										+ listOfFiles[i].getName()
												+ "\", ";
						if (fName[4].contains("STND"))
							resJson += "\"checkpointType\":\"Standard\"},";
						else
							resJson += "\"checkpointType\":\"Staggered\"},";
					}
				}

			}
		}
		if (resJson.length() > 2)
			resJson = resJson.substring(0, resJson.length() - 1);
		resJson += "]";
		return resJson;
	}

	public static void main(String[] args) {
		System.out.println(getAllfiles("2016-01-01", "2018-01-01", "1", "", 0));
	}
}
