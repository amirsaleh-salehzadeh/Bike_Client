package app;

public class ThreadCommitReport implements Runnable {
	public static volatile boolean killer;
	public static int commitDelay;
	private static String raceNo;
	private static String chkType;
	private static String chkNo;

	public ThreadCommitReport(String raceNo, int commitDelay, boolean killer, String chkNo, String chkType) {
		super();
		this.killer = killer;
		this.raceNo = raceNo;
		this.commitDelay = commitDelay;
		this.chkNo = chkNo;
		this.chkType = chkType;
	}

	@Override
	public void run() {
		Thread a = new Thread();
		a.setName("Sleeper");
		while (killer && !Thread.currentThread().isInterrupted()) {
			try {
				a.sleep(commitDelay * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ClientAppDAO.exportCSV(chkType, chkNo, raceNo);
		}
//		Thread.currentThread().interrupt();
	}

}