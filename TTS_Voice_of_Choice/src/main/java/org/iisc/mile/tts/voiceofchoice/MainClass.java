/*
 * @(#)Blah.java        1.82 99/03/18
 *
 * Copyright (c) 1994-1999 Sun Microsystems, Inc.
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 */

package org.iisc.mile.tts.voiceofchoice;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.iisc.mile.tts.voiceofchoice.model.KannadaRecordedAudio;
import org.iisc.mile.tts.voiceofchoice.model.KannadaSentences;
import org.iisc.mile.tts.voiceofchoice.model.RecordedAudioId;
import org.iisc.mile.tts.voiceofchoice.model.TamilRecordedAudio;
import org.iisc.mile.tts.voiceofchoice.model.TamilSentences;
import org.iisc.mile.tts.voiceofchoice.model.UserInfo;

public class MainClass {

	private static EntityManagerFactory factory = Persistence
			.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");;
	private static EntityManager em;
	private static Logger logger = Logger.getLogger(MainClass.class.getName());
	//private static boolean playCheckerComic;
	public static int jumperLength = 401;
	protected static Display d = new Display();
	protected static Shell s = new Shell(d);
	protected static Button stop = new Button(s, SWT.PUSH | SWT.CENTER);
	protected static Button saveAudio = new Button(s, SWT.PUSH | SWT.CENTER);
	protected static Combo langDrop = new Combo(s, SWT.READ_ONLY | SWT.BORDER);
	protected static Button capture = new Button(s, SWT.PUSH | SWT.CENTER);
	protected static Button nextSent = new Button(s, SWT.ARROW | SWT.RIGHT | SWT.CENTER);
	protected static Button prev = new Button(s, SWT.ARROW | SWT.LEFT | SWT.CENTER);
	protected static Button jumpPrev = new Button(s, SWT.ARROW | SWT.LEFT | SWT.CENTER);
	protected static Button jumpNext = new Button(s, SWT.ARROW | SWT.RIGHT | SWT.CENTER);
	protected static Button play = new Button(s, SWT.PUSH | SWT.CENTER);
	protected static Button playComic = new Button(s, SWT.PUSH | SWT.CENTER);
	protected static Button[] radioButtons = new Button[3];

	protected static Button clearAudio = new Button(s, SWT.PUSH | SWT.CENTER);
	protected static boolean audioFlag = true;
	protected static String selUserId = null;
	protected static String langChosen;
	protected static Text langText = new Text(s, SWT.WRAP | SWT.BORDER | SWT.CENTER);
	protected static int counterNext = 0;
	protected static Menu m = new Menu(s, SWT.BAR);
	protected static Menu userMenu = new Menu(s, SWT.DROP_DOWN);
	private static List<?> list;
	protected static boolean playCheckerNorm;
	protected static int jumperArray[] = new int[jumperLength];
	private boolean fromDropDown;
	protected static Canvas animeCanvas;
	protected static Canvas rectCanvas;

	static void setAllButtons(boolean b) {
		MainClass.capture.setEnabled(b);
		MainClass.clearAudio.setEnabled(b);
		MainClass.play.setEnabled(b);
		MainClass.playComic.setEnabled(b);
		MainClass.radioButtons[0].setEnabled(b);
		MainClass.radioButtons[1].setEnabled(b);
		MainClass.langDrop.setEnabled(b);
		MainClass.nextSent.setEnabled(b);
		MainClass.prev.setEnabled(b);
		MainClass.jumpNext.setEnabled(b);
		MainClass.jumpPrev.setEnabled(b);

	}

	protected static Shell createShell() {
		Shell shellName = new Shell(s);
		return shellName;
	}

	public static void setHover() {
		radioButtons[0] = new Button(s, SWT.CHECK);
		radioButtons[1] = new Button(s, SWT.CHECK);
		capture.setToolTipText("Press to record the sentence");
		stop.setToolTipText("Press to stop the present recording");
		saveAudio.setToolTipText("Press to save the recorded sentence");
		play.setToolTipText("Play the recorded sentence");
		playComic.setToolTipText(" Play the recorded sentence \nin a comic voice");
		radioButtons[0].setToolTipText("Play the recorded sentence \nimmediately after clicking stop ");
		radioButtons[1]
				.setToolTipText("Play the recorded sentence in comic voice\nimmediately after clicking stop ");
		langDrop.setToolTipText("Choose the language to record in");
		clearAudio.setToolTipText("Deletes the recorded sentence");
		jumpNext.setToolTipText("Moves to the next un-recorded sentence ");
		jumpPrev.setToolTipText("Moves to the previous un-recorded sentence ");
		nextSent.setToolTipText("Moves to the next sentence");
		prev.setToolTipText("Moves to the previous sentence");
	}

	private MainClass() {
		try {
			new Audio().lineOpen();
		} catch (Exception e) {
			System.out.println("line open");
		}
		setHover();
		s.setLocation(100, 50);
		s.setSize(800, 600);
		s.setText("TTS-Voice of choice");
		s.setBackground(d.getSystemColor(SWT.COLOR_DARK_BLUE));

		s.setImage(new Image(d, MainClass.class.getResourceAsStream("/logo1.bmp")));
		FormLayout layout = new FormLayout();

		layout.marginWidth = 5;
		layout.marginHeight = 5;
		s.setLayout(layout);

		MenuItem file = new MenuItem(m, SWT.CASCADE);
		file.setText("File");
		final Menu filemenu = new Menu(s, SWT.DROP_DOWN);
		final MenuItem openItem = new MenuItem(filemenu, SWT.PUSH);
		file.setMenu(filemenu);

		openItem.setText("New User");
		MenuItem editItem = new MenuItem(filemenu, SWT.PUSH);
		editItem.setText("Edit User");
		MenuItem uploadItem = new MenuItem(filemenu, SWT.PUSH);
		uploadItem.setText("Upload");
		MenuItem exitItem = new MenuItem(filemenu, SWT.PUSH);
		exitItem.setText("Exit");

		exitItem.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				s.close();

			}
		});

		new NewUser().new_user(openItem);

		new EditUser().edit_user(editItem);

		new Upload().upload1(uploadItem);

		final MenuItem usersMenuItem = new MenuItem(m, SWT.CASCADE);
		usersMenuItem.setText("Users");
		usersMenuItem.setMenu(userMenu);

		MenuItem help = new MenuItem(m, SWT.CASCADE);
		help.setText("Help");
		Menu helpmenu = new Menu(s, SWT.DROP_DOWN);
		help.setMenu(helpmenu);
		MenuItem openItem1 = new MenuItem(helpmenu, SWT.PUSH);
		openItem1.setText("Help Contents");
		MenuItem exitItem1 = new MenuItem(helpmenu, SWT.PUSH);
		exitItem1.setText("Help Topics");
		s.setMenuBar(m);

		animeCanvas = new Canvas(s, SWT.COLOR_DARK_BLUE);
		animeCanvas.setBackground(s.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));
		Animation.create();

		UserInfo ui = new UserInfo();
		list = ui.getUserIdList();
		Iterator<?> iter = list.iterator();
		while (iter.hasNext()) {
			MenuItem user = new MenuItem(userMenu, SWT.RADIO);
			String id = iter.next().toString();
			user.setText(id);
			new NewUser().newUserListener(id, user);
		}

		int listLength = list.size();
		// int listLength=0;

		// this is the big MILE logo

		Image image = new Image(d, MainClass.class.getResourceAsStream("/logo.bmp"));
		Label labelLogo = new Label(s, 0);
		labelLogo.setImage(image);
		FormData bigLogo = new FormData();
		bigLogo.left = new FormAttachment(30, 5);
		labelLogo.setLayoutData(bigLogo);

		// the help logo at the right top
		Button helpButton = new Button(s, SWT.PUSH);
		helpButton.setImage(d.getSystemImage(SWT.ICON_QUESTION));
		helpButton.setBackground(d.getSystemColor(SWT.COLOR_DARK_BLUE));
		helpButton.setGrayed(false);
		helpButton.pack();
		FormData helpLogo = new FormData();
		// helpLogo.left = new FormAttachment(0,140);
		helpLogo.right = new FormAttachment(95, 30);
		helpButton.setLayoutData(helpLogo);

		// instruction label
		Font font1 = new Font(d, "Tahoma", 14, SWT.BOLD);
		Text sText = new Text(s, SWT.MULTI | SWT.WRAP);
		sText.setBackground(d.getSystemColor(SWT.COLOR_DARK_BLUE));
		sText.setText("Please read this sentence aloud..");
		sText.setFont(font1);
		sText.setForeground(d.getSystemColor(SWT.COLOR_YELLOW));
		sText.setLocation(200, 150);
		sText.setEditable(false);
		sText.pack();
		FormData instText = new FormData();
		instText.left = new FormAttachment(30, 5);
		instText.top = new FormAttachment(labelLogo, 50);
		sText.setLayoutData(instText);

		// main text box with the sentences
		Font fontKan = new Font(d, " ", 18, SWT.BOLD);

		langText.setFont(fontKan);
		langText.setBackground(d.getSystemColor(SWT.COLOR_BLACK));
		langText.setForeground(d.getSystemColor(SWT.COLOR_YELLOW));
		langText.setTextLimit(300);
		langText.setEditable(false);
		FormData langData = new FormData(600, 80);
		langData.left = new FormAttachment(10, 5);
		langData.right = new FormAttachment(75, 0);
		langData.top = new FormAttachment(40, 0);
		langText.setLayoutData(langData);

		// the drop down to choose the language
		langDrop.setBackground(d.getSystemColor(SWT.COLOR_BLACK));
		FormData langDropData = new FormData(150, 65);
		langDropData.left = new FormAttachment(langText, 5);
		langDropData.top = new FormAttachment(langText, 0, SWT.CENTER);
		langDrop.setLayoutData(langDropData);

		stop.setVisible(false);
		saveAudio.setVisible(false);

		if (selUserId == null) {
			setAllButtons(false);

		}

		langDrop.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("in lang drop listener");

				setAllButtons(true);
				if (!fromDropDown) {
					if (selUserId != null) {

						// STORE THE COUNTER
						em = factory.createEntityManager();
						em.getTransaction().begin();
						System.out.println("this is counter next before storing" + counterNext);
						if (audioFlag == true) {
							counterNext += 1;
						}
						UserInfo curUser = em.find(UserInfo.class, selUserId);
						if ("Kannada".equals(langChosen.trim())) {
							System.out.println(selUserId + "lang drop down storing in kannada");
							curUser.setLastRecordedKannadaSentence(counterNext);

						} else {
							System.out.println(selUserId + "lang drop down storing in tamil");
							curUser.setLastRecordedTamilSentence(counterNext);
						}
						em.persist(curUser);
						em.getTransaction().commit();
						em.close();

						logger.info("successfully updates the counter in the userInfo table when language is changed");

					}
				}

				langChosen = langDrop.getItem(langDrop.getSelectionIndex());
				System.out.println("ERROR IN LANG DROP ,langChosen=" + langChosen);
				radioButtons[1].setVisible(true);

				// GET THE COUNTER

				em = factory.createEntityManager();
				em.getTransaction().begin();
				UserInfo curUser = em.find(UserInfo.class, selUserId);

				if ("Kannada".equals(langChosen.trim())) {

					counterNext = curUser.getLastRecordedKannadaSentence();

				} else {
					counterNext = curUser.getLastRecordedTamilSentence();

				}
				em.persist(curUser);
				em.getTransaction().commit();
				em.close();

				System.out.println("langChosen" + langChosen);
				System.out.println("it's counter" + counterNext);

				em = factory.createEntityManager();
				em.getTransaction().begin();
				if ("Kannada".equals(langChosen.trim())) {

					KannadaSentences ks = em.find(KannadaSentences.class, counterNext);
					langText.setText(ks.getSentenceText());
					em.persist(ks);
				} else {
					System.out.println("till here");
					TamilSentences ts = em.find(TamilSentences.class, counterNext);
					langText.setText(ts.getSentenceText());

					em.persist(ts);
				}
				em.getTransaction().commit();
				em.close();
				fromDropDown = false;

				System.out.println("end lang drop listener");

			}

		});

		// canvas' form data
		FormData animateData = new FormData(60, 25);
		animateData.left = new FormAttachment(31, 5);// 40
		animateData.right = new FormAttachment(60, 5);// 50
		animateData.top = new FormAttachment(80, 0);
		animateData.bottom = new FormAttachment(100, 0);
		animeCanvas.setLayoutData(animateData);

		// capture's form data
		FormData captureData = new FormData(60, 25);
		captureData.left = new FormAttachment(32, 5);// 40
		captureData.right = new FormAttachment(42, 5);// 50
		captureData.top = new FormAttachment(60, 0);
		capture.setLayoutData(captureData);

		// clearData's formdata

		FormData clearData = new FormData(60, 25);
		clearData.left = new FormAttachment(45, 5);// 45
		clearData.right = new FormAttachment(55, 5);// 50
		clearData.top = new FormAttachment(60, 0);
		clearAudio.setLayoutData(clearData);

		// stop's form data
		FormData stopData = new FormData(60, 25);
		stopData.left = new FormAttachment(32, 5);
		stopData.right = new FormAttachment(42, 5);
		stopData.top = new FormAttachment(60, 0);
		stop.setLayoutData(stopData);

		// save's form data

		FormData saveData = new FormData(60, 25);
		saveData.left = new FormAttachment(32, 5);
		saveData.right = new FormAttachment(42, 5);
		saveData.top = new FormAttachment(60, 0);
		saveAudio.setLayoutData(saveData);

		// play normal's form data
		FormData playData = new FormData(140, 25);
		playData.left = new FormAttachment(11, 5);
		playData.right = new FormAttachment(31, 5);
		playData.top = new FormAttachment(65, 20);
		play.setLayoutData(playData);

		// playComic's form data
		FormData playComicData = new FormData(140, 25);
		playComicData.left = new FormAttachment(60, 5);
		playComicData.right = new FormAttachment(80, 0);
		playComicData.top = new FormAttachment(65, 20);
		playComic.setLayoutData(playComicData);

		// radioButton[0]'s form data
		FormData radioData = new FormData();
		radioData.left = new FormAttachment(8, 5);
		radioData.top = new FormAttachment(66, 20);
		radioButtons[0].setLayoutData(radioData);

		// radioButtons[1]'s form data
		FormData radioData1 = new FormData();
		radioData1.left = new FormAttachment(55, 5);
		radioData1.top = new FormAttachment(66, 20);
		radioButtons[1].setLayoutData(radioData1);

		// next sentences' form data
		FormData nextData = new FormData();
		nextData.left = new FormAttachment(66, 5);// 65
		nextData.right = new FormAttachment(70, 5);// 75
		nextData.top = new FormAttachment(60, 0);// 60
		nextSent.setLayoutData(nextData);

		// jumpNext's form data

		FormData jumpNextData = new FormData();
		jumpNextData.left = new FormAttachment(74, 5);// 65
		jumpNextData.right = new FormAttachment(78, 5);// 75
		jumpNextData.top = new FormAttachment(60, 0);// 60
		jumpNext.setLayoutData(jumpNextData);

		// prev's form data
		FormData prevData = new FormData();
		prevData.left = new FormAttachment(16, 5);
		prevData.right = new FormAttachment(20, 5);// 20
		prevData.top = new FormAttachment(60, 0);
		prev.setLayoutData(prevData);

		// jumpPrev's form data
		FormData jumpPrevData = new FormData();
		jumpPrevData.left = new FormAttachment(8, 5);
		jumpPrevData.right = new FormAttachment(12, 5);// 20
		jumpPrevData.top = new FormAttachment(60, 0);
		jumpPrev.setLayoutData(jumpPrevData);

		// the play options(2)

		play.setText("Play back in  Normal Voice");
		play.pack();
		play.setBackground(d.getSystemColor(SWT.COLOR_DARK_BLUE));

		playComic.setText("Play back in Comic voice ");
		playComic.pack();
		playComic.setBackground(d.getSystemColor(SWT.COLOR_DARK_BLUE));

		play.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("in play audioFlag:" + audioFlag);
				boolean oldPlayCheckerNorm = playCheckerNorm;
				playCheckerNorm = false;
				new Audio().playAudioFinal(counterNext);
				playCheckerNorm = oldPlayCheckerNorm;
			}

		});

		radioButtons[0].pack();
		radioButtons[0].setBackground(d.getSystemColor(SWT.COLOR_DARK_BLUE));

		radioButtons[1].pack();
		radioButtons[1].setBackground(d.getSystemColor(SWT.COLOR_DARK_BLUE));

		radioButtons[1].addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {

				//playCheckerComic = true;
				radioButtons[0].setVisible(false);

				if (radioButtons[1].getSelection() == false) {
					radioButtons[0].setVisible(true);
				}

			}
		});

		radioButtons[0].addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				System.out.println(radioButtons[0].getSelection());
				playCheckerNorm = true;

				radioButtons[1].setVisible(false);
				if (radioButtons[0].getSelection() == true) {
					new Audio().playAudioFinal(counterNext);
				}
				if (radioButtons[0].getSelection() == false) {
					radioButtons[1].setVisible(true);
				}

			}

		});

		// capture

		capture.setText("Record");
		capture.setBackground(d.getSystemColor(SWT.COLOR_DARK_BLUE));
		capture.pack();

		// stop

		stop.setText("Stop");
		stop.setBackground(d.getSystemColor(SWT.COLOR_DARK_BLUE));

		stop.pack();

		stop.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {

				CallAnime.stopTimerAnimation();
				audioFlag = false;
				new Audio().setRunning(false);

				stop.setVisible(false);
				saveAudio.setVisible(true);
				clearAudio.setEnabled(true);
				play.setEnabled(true);
				playComic.setEnabled(true);
				nextSent.setEnabled(true);
				prev.setEnabled(true);
				jumpNext.setEnabled(true);
				jumpPrev.setEnabled(true);
				langDrop.setEnabled(true);

				if (playCheckerNorm == true) {

					System.out.println("play checker true so trying to play");
					radioButtons[0].setSelection(true);

					new Audio().playAudioFinal(counterNext);

				}

			}

		});

		capture.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {

				CallAnime.animeFlag = true;
				// audioFlag = false;
				capture.setVisible(false);
				stop.setVisible(true);

				clearAudio.setEnabled(false);
				play.setEnabled(false);
				playComic.setEnabled(false);
				nextSent.setEnabled(false);
				prev.setEnabled(false);
				jumpNext.setEnabled(false);
				jumpPrev.setEnabled(false);
				langDrop.setEnabled(false);

				RecordedAudioId user = new RecordedAudioId();
				user.sentenceId = counterNext;
				user.userId = selUserId;

				em = factory.createEntityManager();
				em.getTransaction().begin();

				if ("Kannada".equalsIgnoreCase(langChosen.trim())) {
					KannadaRecordedAudio userx = em.find(KannadaRecordedAudio.class, user);
					if (userx == null) {
						audioFlag = false;
						new Audio().captureAudioFinal();
					} else {
						// CallAnime.stopTimerAnimation();

						stop.setVisible(false);
						capture.setVisible(true);

						clearAudio.setEnabled(true);
						play.setEnabled(true);
						playComic.setEnabled(true);
						nextSent.setEnabled(true);
						prev.setEnabled(true);
						jumpNext.setEnabled(true);
						jumpPrev.setEnabled(true);
						langDrop.setEnabled(true);

						EditUser.popUp(s, "Already recorded ",
								"This sentence has already been recorded.\nPress Clear and Record again ");
					}

				} else {
					TamilRecordedAudio userx = em.find(TamilRecordedAudio.class, user);
					if (userx == null) {
						audioFlag = false;
						new Audio().captureAudioFinal();
					} else {
						// CallAnime.stopTimerAnimation();
						stop.setVisible(false);
						capture.setVisible(true);

						clearAudio.setEnabled(true);
						play.setEnabled(true);
						playComic.setEnabled(true);
						nextSent.setEnabled(true);
						prev.setEnabled(true);
						jumpNext.setEnabled(true);
						jumpPrev.setEnabled(true);
						langDrop.setEnabled(true);

						EditUser.popUp(s, "Already recorded ",
								"This sentence has already been recorded.\nPress Clear and Record again ");
					}

				}

				em.getTransaction().commit();
				em.close();
			}

		});

		// save

		saveAudio.setLocation(450, 350);
		saveAudio.setText("Save");
		saveAudio.pack();
		saveAudio.setBackground(d.getSystemColor(SWT.COLOR_DARK_BLUE));

		saveAudio.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {

				System.out.println("counterNext:" + counterNext);
				audioFlag = true;
				saveAudio.setVisible(false);
				capture.setVisible(true);
				new Audio().saveRecordedAudio(counterNext);

				// get the array
				if (langChosen.equals("Kannada")) {
					factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
					em = factory.createEntityManager();
					UserInfo curUser = em.find(UserInfo.class, selUserId);
					byte[] asBytes = curUser.getJumperArrayKannada();
					jumperArray = curUser.getJumperArrayInt(asBytes);
					em.close();
				} else {
					factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
					em = factory.createEntityManager();
					UserInfo curUser = em.find(UserInfo.class, selUserId);
					byte[] asBytes = curUser.getJumperArrayTamil();
					jumperArray = curUser.getJumperArrayInt(asBytes);
					em.close();
				}

				// make the change
				jumperArray[counterNext] = 0;

				// set the array

				factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
				em = factory.createEntityManager();
				UserInfo curUser1 = em.find(UserInfo.class, selUserId);
				em.getTransaction().begin();

				if (langChosen.equals("Kannada")) {
					byte byteArray[] = new byte[401];
					byteArray = curUser1.setJumperArrayInt(jumperArray);
					curUser1.setJumperArrayKannada(byteArray);
				} else {
					byte byteArray[] = new byte[401];
					byteArray = curUser1.setJumperArrayInt(jumperArray);
					curUser1.setJumperArrayTamil(byteArray);
				}

				em.persist(curUser1);
				em.getTransaction().commit();
				em.close();
			}

		});

		clearAudio.setText("Clear");
		clearAudio.setBackground(d.getSystemColor(SWT.COLOR_DARK_BLUE));

		clearAudio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("in clear audioFlag:" + audioFlag);
				try {

					if (!audioFlag) {

						saveAudio.setVisible(false);
						capture.setVisible(true);
						audioFlag = true;

					} else {
						RecordedAudioId user = new RecordedAudioId();
						user.sentenceId = counterNext;
						user.userId = selUserId;

						EntityManagerFactory factory = Persistence
								.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
						EntityManager em = factory.createEntityManager();

						em.getTransaction().begin();
						if ("Kannada".equalsIgnoreCase(langChosen.trim())) {
							KannadaRecordedAudio userx = em.find(KannadaRecordedAudio.class, user);
							if (userx.getUserId() != null) {
								em.remove(userx);
							}
						} else {
							TamilRecordedAudio userx = em.find(TamilRecordedAudio.class, user);
							if (userx.getUserId() != null) {
								em.remove(userx);
							}
						}

						em.getTransaction().commit();

						em.close();
						factory.close();

					}
				} catch (NullPointerException e) {

					EditUser.popUp(s, "Nothing to clear",
							"Sorry no recorded sentence to clear\nPlease record the sentence. ");
				}
			}

		});
		// next sentence

		nextSent.setLocation(650, 500);
		nextSent.setText("next Sentence");
		nextSent.pack();
		nextSent.setBackground(d.getSystemColor(SWT.COLOR_DARK_BLUE));

		nextSent.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {

				audioFlag = true;
				stop.setVisible(false);
				saveAudio.setVisible(false);
				capture.setVisible(true);

				counterNext++;
				System.out.println("next " + counterNext);

				em = factory.createEntityManager();

				if ("Kannada".equalsIgnoreCase(langChosen.trim())) {

					KannadaSentences curSent = em.find(KannadaSentences.class, counterNext);
					langText.setText(curSent.getSentenceText());

				} else {
					TamilSentences curSent = em.find(TamilSentences.class, counterNext);

					langText.setText(curSent.getSentenceText());
				}

				em.close();

			}

		});

		jumpNext.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {

				stop.setVisible(false);
				saveAudio.setVisible(false);
				capture.setVisible(true);

				// get the array
				if (langChosen.equals("Kannada")) {
					factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
					em = factory.createEntityManager();
					UserInfo curUser = em.find(UserInfo.class, selUserId);
					byte[] asBytes = curUser.getJumperArrayKannada();
					jumperArray = curUser.getJumperArrayInt(asBytes);
					em.close();
				} else {
					factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
					em = factory.createEntityManager();
					UserInfo curUser = em.find(UserInfo.class, selUserId);
					byte[] asBytes = curUser.getJumperArrayTamil();
					jumperArray = curUser.getJumperArrayInt(asBytes);
					em.close();
				}

				// get the right counter to jump to
				int i;
				for (i = counterNext + 1; i < 401; i++) {
					if (jumperArray[i] != 0) {

						break;
					}
				}
				System.out.println("i:" + i);
				if (i == 1 || i == 0) {
					System.out.println("all previous sentences have been recorded");
				} else {
					counterNext = i;
					System.out.println("sentence that was skipped " + counterNext);
				}

				// display the sentence corresponding to that counter
				EntityManagerFactory factory = Persistence
						.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
				EntityManager em2 = factory.createEntityManager();

				if ("Kannada".equalsIgnoreCase(langChosen.trim())) {

					KannadaSentences curSent = em2.find(KannadaSentences.class, counterNext);
					langText.setText(curSent.getSentenceText());
				} else {
					TamilSentences curSent = em2.find(TamilSentences.class, counterNext);

					langText.setText(curSent.getSentenceText());
				}

				em2.close();

			}
		});

		prev.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				new Audio().lineDrain();

				if (counterNext > 1) {
					counterNext -= 1;
				}

				System.out.println("prev" + counterNext);

				EntityManagerFactory factory = Persistence
						.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
				EntityManager em2 = factory.createEntityManager();

				if ("Kannada".equalsIgnoreCase(langChosen.trim())) {

					KannadaSentences curSent = em2.find(KannadaSentences.class, counterNext);
					langText.setText(curSent.getSentenceText());
				} else {
					TamilSentences curSent = em2.find(TamilSentences.class, counterNext);

					langText.setText(curSent.getSentenceText());
				}

				em2.close();

			}

		});

		jumpPrev.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {

				// get the array
				if (langChosen.equals("Kannada")) {
					factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
					em = factory.createEntityManager();
					UserInfo curUser = em.find(UserInfo.class, selUserId);
					byte[] asBytes = curUser.getJumperArrayKannada();
					jumperArray = curUser.getJumperArrayInt(asBytes);
					em.close();
				} else {
					factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
					em = factory.createEntityManager();
					UserInfo curUser = em.find(UserInfo.class, selUserId);
					byte[] asBytes = curUser.getJumperArrayTamil();
					jumperArray = curUser.getJumperArrayInt(asBytes);
					em.close();
				}

				// get the right counter to jump to

				int i;
				for (i = counterNext - 1; i > 0; i--) {
					if (jumperArray[i] != 0) {

						break;
					}
				}
				System.out.println("i:" + i);
				if (i == 1 || i == 0) {
					System.out.println("all previous sentences have been recorded");
				} else {
					counterNext = i;
					System.out.println("sentence that was skipped " + counterNext);
				}

				// display the sentence corresponding to that counter
				EntityManagerFactory factory = Persistence
						.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
				EntityManager em2 = factory.createEntityManager();

				if ("Kannada".equalsIgnoreCase(langChosen.trim())) {

					KannadaSentences curSent = em2.find(KannadaSentences.class, counterNext);
					langText.setText(curSent.getSentenceText());
				} else {
					TamilSentences curSent = em2.find(TamilSentences.class, counterNext);

					langText.setText(curSent.getSentenceText());
				}

				em2.close();
			}
		});

		// warning for the shell closing
		s.addShellListener(new ShellListener() {

			public void shellActivated(ShellEvent event) {
			}

			public void shellClosed(ShellEvent event) {
				try {

					CallAnime.stopTimerAnimation();

					new Audio().lineClose();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (selUserId != null) {
					if (counterNext != 1) {

						em = factory.createEntityManager();
						em.getTransaction().begin();

						if (audioFlag == true) {
							counterNext += 1;
						}

						UserInfo curUser = em.find(UserInfo.class, selUserId);

						if ("Kannada".equalsIgnoreCase(langChosen.trim())) {

							curUser.setLastRecordedKannadaSentence(counterNext);
						} else {
							curUser.setLastRecordedTamilSentence(counterNext);
						}
						em.persist(curUser);
						em.getTransaction().commit();
						em.close();

						factory.close();
						logger.info("successfully updates the counter in the userInfo table while shell close");
					}
				}

				
				 MessageBox messageBox = new MessageBox(s, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
				  messageBox.setText("Warning");
				  messageBox.setMessage("Are you sure you want to exit the TTS-Voice of choice application?"
				  ); if (messageBox.open() == SWT.YES) event.doit = true; else event.doit = false;
				 
			}

			public void shellDeactivated(ShellEvent arg0) {
			}

			public void shellDeiconified(ShellEvent arg0) {
			}

			public void shellIconified(ShellEvent arg0) {
			}
		});

		s.open();
		if (listLength == 0) {
			new NewUser().newUserPage();// if no registered users then the new user page opens up
		} else {
			new NewUser().multipleUsersChoosing();
		}

		while (!s.isDisposed()) {
			if (!d.readAndDispatch())
				d.sleep();
		}
		d.dispose();
	}

	public static void main(String[] argv) {
		new MainClass();
	}
}
