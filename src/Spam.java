import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import org.jnativehook.keyboard.NativeKeyEvent;

public class Spam {

	private boolean running = false;

	public void Start(String text, long ms, int keyCode) throws AWTException, InterruptedException {

		if (keyCode == NativeKeyEvent.VC_F6) {
			running = true;

			new Thread(() -> {
				try {
					Robot r = new Robot();
					StringSelection s = new StringSelection(text);
					Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
					c.setContents(s, null);

					while (running) {
						Thread.sleep(ms);

						r.keyPress(KeyEvent.VK_CONTROL);
						r.keyPress(KeyEvent.VK_V);
						r.keyRelease(KeyEvent.VK_CONTROL);
						r.keyRelease(KeyEvent.VK_V);

						r.keyPress(KeyEvent.VK_ENTER);
						r.keyRelease(KeyEvent.VK_ENTER);
					}
				} catch (AWTException | InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}

	public void stop() {
		running = false;
	}

}
