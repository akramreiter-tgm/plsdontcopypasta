package debugMonitor;

import jfxApplication.JFXMainApplication;

public class launchJFXThread implements Runnable {

	@Override
	public void run() {
		JFXMainApplication.selflaunch();
	}

}
