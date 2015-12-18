package org.iisc.mile.tts.voiceofchoice;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Animation {

	private static int IMAGE_WIDTH = 0;
	private static final int TIMER_INTERVAL =1;
	private static int IMAGE_HEIGHT = 20;
	private static boolean onlyOnce = true;
	private static int x = 20;
	private static int y = 20;
	protected static boolean bounceAnimeFlag;
	static Runnable runnable;

	public static void redraw(final int aw) {
		bounceAnimeFlag = true;
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				IMAGE_WIDTH = aw;
				System.out.println("animation width:" + aw);
				MainClass.animeCanvas.redraw();
			}
		});
	}

	protected static void create() {
		MainClass.animeCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				// Create the image to fill the canvas
				MainClass.animeCanvas.setBackground(MainClass.s.getDisplay().getSystemColor(
						SWT.COLOR_DARK_BLUE));
				Image image = new Image(MainClass.s.getDisplay(), MainClass.animeCanvas.getBounds());
				// Set up the offscreen gc
				GC gcImage = new GC(image);
				gcImage.setBackground(event.gc.getBackground());// can change to (shell.getBackground);
				gcImage.fillRectangle(image.getBounds());
				gcImage.setBackground(MainClass.s.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
				gcImage.fillRectangle(5, 5, IMAGE_WIDTH, IMAGE_HEIGHT);
				gcImage.drawRectangle(0, 0, 150, 30);
				// Draw the offscreen buffer to the screen
				event.gc.drawImage(image, x, y);
				image.dispose();
				gcImage.dispose();
			}
		});
	}

}