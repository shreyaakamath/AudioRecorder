package org.iisc.mile.tts.voiceofchoice;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Upload  {

	protected void upload1(MenuItem uploadItem)

	{
		uploadItem.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {

				final Shell dialog = MainClass.createShell();
				MainClass.s.setEnabled(false);
				Point pt=MainClass.s.getLocation();
				dialog.setLocation(pt.x+100,pt.y+100);
				
				dialog.setSize(500, 300);
				dialog.setText("Upload to IISc Server");
				dialog.setBackground(MainClass.d.getSystemColor(SWT.COLOR_BLACK));
				 dialog.setImage(new Image(MainClass.d,
				 Upload.class.getResourceAsStream("/logo1.bmp")));

				Label label1 = new Label(dialog, SWT.NONE | SWT.CENTER);
				label1.setText("Thank you for uploading your TTS-Voice of choice to IISc server \n\n"
						+ " Many people will benefit from your contribution\n\n"
						+ "Please make sure you are connected to the internet and click confirm  ");
				label1.setSize(500, 150);
				label1.setLocation(0, 50);
				label1.setFont(new Font(MainClass.d, "Arial", 12, SWT.ITALIC));
				final Button ok = new Button(dialog, SWT.PUSH);
				label1.setBackground(MainClass.d.getSystemColor(SWT.COLOR_BLACK));
				label1.setForeground(MainClass.d.getSystemColor(SWT.COLOR_GRAY));
				ok.setText("      Confirm    ");
				ok.setLocation(100, 200);
				ok.pack();

				Button cancel = new Button(dialog, SWT.PUSH);
				cancel.setText("      Cancel      ");
				cancel.setLocation(250, 200);

				cancel.pack();
			
			cancel.addListener(SWT.Selection, new Listener(){
				public void handleEvent(Event event) {
					MainClass.s.setEnabled(true);
					dialog.close();
				}
			});
				
				ok.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent arg0){
						dialog.close();
						upload2();
					}
				});

				dialog.addListener(SWT.Close, new Listener() {
				      public void handleEvent(Event event) {
				       MainClass.s.setEnabled(true);
				      }
				    });
				
				dialog.open();
/*				while (!dialog.isDisposed()) {
					if (!MainClass.d.readAndDispatch())
						MainClass.d.sleep();
				}
	*/
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

	}

	protected void upload2()

	{
		final Shell shell1 = MainClass.createShell();
		MainClass.s.setEnabled(false);
		Point pt=MainClass.s.getLocation();
		shell1.setLocation(pt.x+100,pt.y+100);
		shell1.setSize(500, 300);
		shell1.setText("Upload to IISc server..");
		shell1.setBackground(MainClass.d.getSystemColor(SWT.COLOR_BLACK));
		 shell1.setImage(new Image(MainClass.d,
		 Upload.class.getResourceAsStream("/logo1.bmp")));

				Link link = new Link(shell1, SWT.NONE);

				String message = ("Thank you for uploading your TTS-Voice of choice to IISc server \n\n"
						+ "Your voice is being uploaded to the IISc server\n\n"
						+ "visit us at :<a>http//:mile.ee.iisc.ernet.in/tts/voice</a>");

				link.setText(message);
				link.setForeground(MainClass.d.getSystemColor(SWT.COLOR_GRAY));
				link.setBackground(MainClass.d.getSystemColor(SWT.COLOR_BLACK));
				link.setEnabled(true);
				link.setBounds(50, 50, 500, 100);
				link.setFont(new Font(MainClass.d, "Arial", 12, SWT.ITALIC));
				link.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {

						final Shell shell2 = MainClass.createShell();
						shell2.setText("IISC MILE lab");
						shell2.setImage(new Image(MainClass.d, Upload.class
								.getResourceAsStream("/logo1.bmp")));
						GridLayout gridLayout = new GridLayout();
						gridLayout.numColumns = 3;
						shell2.setLayout(gridLayout);
						ToolBar toolbar = new ToolBar(shell2, SWT.NONE);
						ToolItem itemBack = new ToolItem(toolbar, SWT.PUSH);
						itemBack.setText("Back");
						ToolItem itemForward = new ToolItem(toolbar, SWT.PUSH);
						itemForward.setText("Forward");
						ToolItem itemStop = new ToolItem(toolbar, SWT.PUSH);
						itemStop.setText("Stop");
						ToolItem itemRefresh = new ToolItem(toolbar, SWT.PUSH);
						itemRefresh.setText("Refresh");
						ToolItem itemGo = new ToolItem(toolbar, SWT.PUSH);
						itemGo.setText("Go");

						GridData data = new GridData();
						data.horizontalSpan = 3;
						toolbar.setLayoutData(data);

						Label labelAddress = new Label(shell2, SWT.NONE);
						labelAddress.setText("Address");

						final Text location = new Text(shell2, SWT.BORDER);
						data = new GridData();
						data.horizontalAlignment = GridData.FILL;
						data.horizontalSpan = 2;
						data.grabExcessHorizontalSpace = true;
						location.setLayoutData(data);

						final Browser browser;
						try {
							browser = new Browser(shell2, SWT.NONE);
						} catch (SWTError e) {
							System.out
									.println("Could not instantiate Browser: "
											+ e.getMessage());
							MainClass.d.dispose();
							return;
						}
						data = new GridData();
						data.horizontalAlignment = GridData.FILL;
						data.verticalAlignment = GridData.FILL;
						data.horizontalSpan = 3;
						data.grabExcessHorizontalSpace = true;
						data.grabExcessVerticalSpace = true;
						browser.setLayoutData(data);

						final Label status = new Label(shell2, SWT.NONE);
						data = new GridData(GridData.FILL_HORIZONTAL);
						data.horizontalSpan = 2;
						status.setLayoutData(data);

						final ProgressBar progressBar = new ProgressBar(shell2,
								SWT.NONE);
						data = new GridData();
						data.horizontalAlignment = GridData.END;
						progressBar.setLayoutData(data);

						/* event handling */
						Listener listener = new Listener() {
							public void handleEvent(Event event) {
								ToolItem item = (ToolItem) event.widget;
								String string = item.getText();
								if (string.equals("Back"))
									browser.back();
								else if (string.equals("Forward"))
									browser.forward();
								else if (string.equals("Stop"))
									browser.stop();
								else if (string.equals("Refresh"))
									browser.refresh();
								else if (string.equals("Go"))
									browser.setUrl(location.getText());
							}
						};
						browser.addProgressListener(new ProgressListener() {
							public void changed(ProgressEvent event) {
								if (event.total == 0)
									return;
								int ratio = event.current * 100 / event.total;
								progressBar.setSelection(ratio);
							}

							public void completed(ProgressEvent event) {
								progressBar.setSelection(0);
							}
						});
						browser.addStatusTextListener(new StatusTextListener() {
							public void changed(StatusTextEvent event) {
								status.setText(event.text);
							}
						});
						browser.addLocationListener(new LocationListener() {
							public void changed(LocationEvent event) {
								if (event.top)
									location.setText(event.location);
							}

							public void changing(LocationEvent event) {
							}
						});
						itemBack.addListener(SWT.Selection, listener);
						itemForward.addListener(SWT.Selection, listener);
						itemStop.addListener(SWT.Selection, listener);
						itemRefresh.addListener(SWT.Selection, listener);
						itemGo.addListener(SWT.Selection, listener);
						location.addListener(SWT.DefaultSelection,
								new Listener() {
									public void handleEvent(Event e) {
										browser.setUrl(location.getText());
									}
								});

						shell2.open();
						browser.setUrl("http://mile.ee.iisc.ernet.in:8080/tts_demo/");

					}
				});

				final Button pause = new Button(shell1, SWT.PUSH);
				pause.setText("     Pause    ");
				pause.setLocation(100, 200);
				pause.pack();
				final Button res = new Button(shell1, SWT.PUSH);
				res.setText("      Resume   ");
				res.setLocation(100, 200);
				res.setVisible(false);
				res.pack();

				pause.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent arg0) {
						pause.setVisible(false);
						res.setVisible(true);
					}
				});

				res.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent arg0) {
						pause.setVisible(true);
						res.setVisible(false);
					}
				});

				Button cancel = new Button(shell1, SWT.PUSH);
				cancel.setText("      Cancel      ");
				cancel.setLocation(270, 200);
				cancel.pack();
			
				cancel.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent arg0) {
						   shell1.close();
				   }
				});
				
				 shell1.addListener(SWT.Close, new Listener() {
				      public void handleEvent(Event event) {
				        int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO|SWT.ICON_QUESTION;
				        MessageBox messageBox = new MessageBox(shell1, style);
				        messageBox.setText("Upload to IISc server");
				        messageBox.setMessage("Are you sure you want to cancel\n your upload to IISc server?");
				        event.doit = messageBox.open() == SWT.YES;
				        MainClass.s.setEnabled(true);
				        
				      }
				    });

				shell1.open();
			
				while (!shell1.isDisposed()) {
					if (!MainClass.d.readAndDispatch())
						MainClass.d.sleep();
				}

				while (!MainClass.s.isDisposed()) {
					if (!MainClass.d.readAndDispatch())
						MainClass.d.sleep();
				}
				MainClass.d.dispose();
			}



}
