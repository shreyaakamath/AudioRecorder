package org.iisc.mile.tts.voiceofchoice;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.iisc.mile.tts.voiceofchoice.model.KannadaSentences;
import org.iisc.mile.tts.voiceofchoice.model.TamilSentences;
import org.iisc.mile.tts.voiceofchoice.model.UserInfo;

public class EditUser extends NewUser {

	private static EntityManagerFactory factory = Persistence
			.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");;
	private static EntityManager em;
	private static Logger logger = Logger.getLogger(EditUser.class.getName());

	int counter = 1;

	static String languageArray[] = { "Kannada", "Tamil", "Hindi", "Marathi", "Bengali", "Telugu" };

	public static List<?> list;

	static void popUp(final Shell dialogParent, final String head, final String body) {
		dialogParent.setEnabled(false);
		final Shell dialog = new Shell(dialogParent.getDisplay(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		Point pt = dialogParent.getLocation();// dialogParent.getDisplay().getCursorLocation();
		dialog.setLocation(pt.x + 20, pt.y + 20);
		dialog.setSize(300, 110);
		dialog.setText(head);
		dialog.setBackground(MainClass.d.getSystemColor(SWT.COLOR_BLACK));
		GridLayout rowLayout = new GridLayout(2, false);
		rowLayout.marginWidth = 10;
		rowLayout.marginHeight = 10;
		rowLayout.verticalSpacing = 3;
		dialog.setLayout(rowLayout);
		Label textLabel = new Label(dialog, SWT.NONE);
		Label wasteLabel = new Label(dialog, SWT.NONE);
		textLabel.setText(body);
		textLabel.setBackground(MainClass.d.getSystemColor(SWT.COLOR_BLACK));
		textLabel.setForeground(MainClass.d.getSystemColor(SWT.COLOR_GRAY));

		wasteLabel.setText("     ");
		wasteLabel.setBackground(MainClass.d.getSystemColor(SWT.COLOR_BLACK));

		final Button ok1 = new Button(dialog, SWT.PUSH);
		ok1.setText("   OK  ");
		Button cancel1 = new Button(dialog, SWT.PUSH);
		cancel1.setText("Cancel");

		dialog.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event e) {

				dialogParent.setEnabled(true);
			}
		});

		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				dialog.close();
				dialogParent.setEnabled(true);
			}
		};
		ok1.addListener(SWT.Selection, listener);
		cancel1.addListener(SWT.Selection, listener);

		dialog.open();

	}

	static boolean match(String mail) {
		Pattern pat;
		Matcher mat;
		boolean found;

		pat = Pattern.compile("[0-9]*[a-zA-Z]*[0-9]*@[a-zA-Z]*\\.[a-zA-Z]*");
		mat = pat.matcher(mail);

		found = mat.matches();

		if (found) {
			return true;
		} else {
			return false;
		}
	}

	void edit_user(MenuItem editItem) {

		editItem.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				MainClass.s.setEnabled(false);
				final Shell shell = new Shell(MainClass.s);
				Point pt = MainClass.s.getLocation();
				shell.setLocation(pt.x + 200, pt.y + 150);
				shell.setText("Choosing a User");
				shell.setSize(300, 300);
				GridLayout gd = new GridLayout(3, false);
				shell.setLayout(gd);
				gd.marginHeight = 60;
				gd.marginWidth = 40;

				GridData gdLabel = new GridData(SWT.FILL, SWT.FILL, true, false);
				gdLabel.horizontalSpan = 5;
				gdLabel.verticalIndent = 15;
				Label chooseLabel = new Label(shell, SWT.NONE);
				chooseLabel.setText("Please  Choose");
				Label chooseLabel1 = new Label(shell, SWT.NONE);
				chooseLabel1.setText("the  ID  you  want  to  edit:");
				final Combo idDropDown = new Combo(shell, SWT.READ_ONLY | SWT.DROP_DOWN | SWT.BORDER);
				UserInfo info = new UserInfo();
				list = info.getUserIdList();
				Iterator<?> iter = list.iterator();
				while (iter.hasNext()) {
					String id = iter.next().toString();
					idDropDown.add(id);
				}
				idDropDown.setLayoutData(gdLabel);

				GridData gdButton1 = new GridData(SWT.FILL, SWT.FILL, true, false);
				gdButton1.verticalIndent = 15;
				gdButton1.horizontalIndent = 15;
				Button next = new Button(shell, SWT.PUSH);
				next.setLayoutData(gdButton1);
				next.setText("  OK  ");

				GridData gdButton2 = new GridData(SWT.FILL, SWT.FILL, true, false);
				gdButton2.verticalIndent = 15;
				// gdButton2.horizontalIndent=5;
				Button cancel = new Button(shell, SWT.PUSH);
				cancel.setLayoutData(gdButton2);
				cancel.setText("Cancel");

				shell.addListener(SWT.Close, new Listener() {
					public void handleEvent(Event e) {
						MainClass.s.setEnabled(true);
					}
				});

				next.addSelectionListener(new SelectionAdapter() {

					public void widgetSelected(SelectionEvent arg0) {
						try {
							String id = idDropDown.getText();
							System.out.println("this is the id" + id);
							openEdit_User(id);
						} catch (NullPointerException e) {
							
							System.out.println("Please select a user to edit");
						}

					}
				});

				cancel.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent arg0) {
						shell.close();
						MainClass.s.setEnabled(true);
					}
				});

				shell.open();
			}// listener

		});// listener
	}

	void openEdit_User(String userId) {

		MainClass.langDrop.setForeground(MainClass.d.getSystemColor(SWT.COLOR_YELLOW));
		final Shell shell1 = new Shell(MainClass.s);
		Point pt = MainClass.s.getLocation();
		shell1.setLocation(pt.x + 150, pt.y + 50);
		shell1.setSize(500, 500);
		shell1.setText("Edit User ");
		// shell1.setImage(new Image(d, EditUser.class
		// .getResourceAsStream("/logo1.bmp")));
		GridLayout rowLayout = new GridLayout(4, false);
		shell1.setLayout(rowLayout);
		rowLayout.marginWidth = 60;
		rowLayout.marginHeight = 60;
		shell1.setLayout(rowLayout);

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		final Label name = new Label(shell1, SWT.NONE);
		name.setText("Name:");
		final Text nameText = new Text(shell1, SWT.BORDER);
		gd.horizontalSpan = 3;
		nameText.setLayoutData(gd);
		nameText.setTextLimit(150);

		GridData gdnameId = new GridData(SWT.FILL, SWT.FILL, true, false);
		final Label nameId = new Label(shell1, SWT.NONE);
		nameId.setText("User Id:");
		final Text nameIdText = new Text(shell1, SWT.READ_ONLY | SWT.BORDER);
		gdnameId.horizontalSpan = 3;
		nameIdText.setLayoutData(gdnameId);
		nameIdText.setTextLimit(150);

		Label ageLabel = new Label(shell1, SWT.NONE);
		ageLabel.setText("DOB:");
		final Combo dayDropDown = new Combo(shell1, SWT.DROP_DOWN | SWT.BORDER);
		dayDropDown.setEnabled(true);
		for (int i = 1; i < 32; i++) {
			dayDropDown.add("" + i);

		}
		String month[] = { "January", "February", "March", "April", "May", "June", "July", "August",
				"September", "October", "Novemeber", "December" };
		final Combo monthDropDown = new Combo(shell1, SWT.DROP_DOWN | SWT.BORDER);
		for (int i = 0; i < 12; i++) {
			monthDropDown.add(month[i]);

		}
		final Combo yrDropDown = new Combo(shell1, SWT.DROP_DOWN | SWT.BORDER);
		for (int i = 1910; i < 2010; i++) {
			yrDropDown.add("" + i);

		}
		dayDropDown.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		monthDropDown.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		yrDropDown.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		dayDropDown.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		monthDropDown.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		yrDropDown.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		dayDropDown.addKeyListener(new KeyListener() {
			String selectedItem = "";

			public void keyPressed(KeyEvent e) {
				if (dayDropDown.getText().length() > 0) {
					return;
				}
				String key = Character.toString(e.character);
				String[] items = dayDropDown.getItems();
				for (int i = 0; i < items.length; i++) {
					if (items[i].toLowerCase().startsWith(key.toLowerCase())) {
						dayDropDown.select(i);
						selectedItem = items[i];
						return;
					}
				}
			}

			public void keyReleased(KeyEvent e) {
				if (selectedItem.length() > 0)
					dayDropDown.setText(selectedItem);
				selectedItem = "";
			}
		});

		monthDropDown.addKeyListener(new KeyListener() {
			String selectedItem = "";

			public void keyPressed(KeyEvent e) {
				if (monthDropDown.getText().length() > 0) {
					return;
				}
				String key = Character.toString(e.character);
				String[] items = monthDropDown.getItems();
				for (int i = 0; i < items.length; i++) {
					if (items[i].toLowerCase().startsWith(key.toLowerCase())) {
						monthDropDown.select(i);
						selectedItem = items[i];
						return;
					}
				}
			}

			public void keyReleased(KeyEvent e) {
				if (selectedItem.length() > 0)
					monthDropDown.setText(selectedItem);
				selectedItem = "";
			}
		});

		yrDropDown.addKeyListener(new KeyListener() {
			String selectedItem = "";

			public void keyPressed(KeyEvent e) {
				if (yrDropDown.getText().length() > 0) {
					return;
				}
				String key = Character.toString(e.character);
				String[] items = yrDropDown.getItems();
				for (int i = 0; i < items.length; i++) {
					if (items[i].toLowerCase().startsWith(key.toLowerCase())) {
						yrDropDown.select(i);
						selectedItem = items[i];
						return;
					}
				}
			}

			public void keyReleased(KeyEvent e) {
				if (selectedItem.length() > 0)
					yrDropDown.setText(selectedItem);
				selectedItem = "";
			}
		});

		GridData gd_sex = new GridData(SWT.FILL, GridData.CENTER, true, false);
		Label sex = new Label(shell1, SWT.NONE);
		sex.setText("Gender:");
		final Combo sexDropDown = new Combo(shell1, SWT.DROP_DOWN | SWT.BORDER);
		sexDropDown.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		sexDropDown.setLayoutData(gd_sex);
		gd_sex.horizontalSpan = 3;
		sexDropDown.add("Female");
		sexDropDown.add("Male");

		sexDropDown.addKeyListener(new KeyListener() {
			String selectedItem = "";

			public void keyPressed(KeyEvent e) {
				if (sexDropDown.getText().length() > 0) {
					return;
				}
				String key = Character.toString(e.character);
				String[] items = sexDropDown.getItems();
				for (int i = 0; i < items.length; i++) {
					if (items[i].toLowerCase().startsWith(key.toLowerCase())) {
						sexDropDown.select(i);
						selectedItem = items[i];
						return;
					}
				}
			}

			public void keyReleased(KeyEvent e) {
				if (selectedItem.length() > 0)
					sexDropDown.setText(selectedItem);
				selectedItem = "";
			}
		});

		GridData gdParent = new GridData(SWT.FILL, GridData.CENTER, true, false);
		Label parent = new Label(shell1, SWT.NONE);
		parent.setText("Parent/Guardian Name:");
		final Text parentText = new Text(shell1, SWT.BORDER);
		parentText.setLayoutData(gdParent);
		gdParent.horizontalSpan = 3;

		GridData gridAdd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridAdd.horizontalAlignment = GridData.FILL;
		Label address = new Label(shell1, SWT.NONE);
		address.setText("Address:");
		final Text addText = new Text(shell1, SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		addText.setLayoutData(gridAdd);
		gridAdd.horizontalSpan = 3;
		addText.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
					e.doit = true;
				}
			}
		});

		GridData gd1 = new GridData(SWT.FILL, SWT.FILL, true, false);
		Label contactNum = new Label(shell1, SWT.NONE);
		contactNum.setText("Contact No:");
		final Text numText = new Text(shell1, SWT.BORDER);
		gd1.horizontalSpan = 3;
		numText.setLayoutData(gd1);
		numText.setTextLimit(300);

		GridData gdLang1 = new GridData(SWT.FILL, SWT.FILL, true, false);
		Label lang = new Label(shell1, SWT.NONE);
		lang.setText("Laguages you know to\nread and speak:");
		gdLang1.verticalSpan = 3;
		lang.setLayoutData(gdLang1);

		GridData gdLang = new GridData(SWT.FILL, SWT.FILL, true, false);

		final Button langArray[] = new Button[6];
		for (int p = 0; p < numberLang; p++) {
			langArray[p] = new Button(shell1, SWT.CHECK);
			langArray[p].pack();
			langArray[p].setLayoutData(gdLang);
		}

		langArray[0].setText("Kannada");

		langArray[1].setText("Tamil");

		langArray[2].setText("Hindi");

		GridData gdLangIdt = new GridData(SWT.FILL, SWT.FILL, true, false);
		gdLangIdt.verticalSpan = 2;
		gdLangIdt.horizontalSpan = 3;
		langArray[3].setText("English");
		langArray[3].setLayoutData(gdLangIdt);

		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false);
		Label email = new Label(shell1, SWT.NONE);
		email.setText("Email ID:");
		email.setLayoutData(gd2);
		gd2.verticalSpan = 1;
		final Text emailText = new Text(shell1, SWT.BORDER);
		emailText.setLayoutData(gd1);
		emailText.setTextLimit(300);
		gd1.horizontalSpan = 3;

		GridData gdButton = new GridData(SWT.FILL, SWT.FILL, false, false);
		gdButton.verticalIndent = 2;
		gdButton.horizontalSpan = 0;
		Button save = new Button(shell1, SWT.PUSH | SWT.CENTER);
		save.setText("Save");
		save.pack();
		save.setLayoutData(gdButton);

		GridData gdButton1 = new GridData(SWT.FILL, SWT.FILL, false, false);
		gdButton1.verticalIndent = 2;
		gdButton1.horizontalSpan = 3;
		gdButton1.horizontalIndent = 100;
		gdButton1.grabExcessHorizontalSpace = true;
		Button cancelIt = new Button(shell1, SWT.PUSH | SWT.CENTER);
		cancelIt.setText("Cancel");
		cancelIt.pack();
		cancelIt.setLayoutData(gdButton1);

		Listener listener = new Listener() {
			public void handleEvent(Event event) {

				// shell.setEnabled(true);
				shell1.close();
			}
		};

		cancelIt.addListener(SWT.Selection, listener);

		// 1)POPULATE FROM DB

		em = factory.createEntityManager();
		UserInfo curUser = em.find(UserInfo.class, userId);

		String dob = curUser.getDob();

		StringTokenizer split = new StringTokenizer(dob, "-");

		dayDropDown.setText(split.nextToken());
		monthDropDown.setText(split.nextToken());
		yrDropDown.setText(split.nextToken());

		nameText.setText(curUser.getName());
		nameIdText.setText(curUser.getUserId());
		System.out.println("gender" + curUser.getGender());
		sexDropDown.setText(curUser.getGender());
		parentText.setText(curUser.getParentGuardianName());
		addText.setText(curUser.getAddress());
		numText.setText(curUser.getContactNumber());
		emailText.setText(curUser.getEmailId());

		final int[] langPrevSel = new int[numberLang];
		if (curUser.getLastRecordedKannadaSentence() > 0) {
			langArray[0].setSelection(true);
			langPrevSel[0] = curUser.getLastRecordedKannadaSentence();

		}
		if (curUser.getLastRecordedTamilSentence() > 0) {
			langArray[1].setSelection(true);
			langPrevSel[1] = curUser.getLastRecordedTamilSentence();

		}
		if (curUser.getLastRecordedHindiSentence() > 0) {
			langArray[2].setSelection(true);
			langPrevSel[2] = curUser.getLastRecordedHindiSentence();
		}
		if (curUser.getLastRecordedEnglishSentence() > 0) {
			langArray[3].setSelection(true);
			langPrevSel[3] = curUser.getLastRecordedEnglishSentence();
		}
		em.close();

		// till here

		save.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {

				// shell.setEnabled(true);

				MainClass.s.setText(nameText.getText() + "-TTS Voice of choice");// set
				// the
				// main
				// shell
				// name
				int n = 0;
				boolean flag = true;// used to check if all fields are
									// filled correctly
				int arrayLength = 0;

				for (int q = 0; q < numberLang; q++) {
					System.out.println("change this for loop when no. of languages increase");
					if (langArray[q].getSelection())
						arrayLength++;
				}

				// checking if all the boxes are filled

				if (nameText.getCharCount() == (0)) {
					flag = false;
					popUp(shell1, "New User", "ERROR\nPlease enter the Name");
				}

				if (numText.getCharCount() == (0)) {
					flag = false;
					popUp(shell1, "New User", "ERROR\nPlease enter the Contact Number");
				}
				if (addText.getCharCount() == (0)) {
					flag = false;
					popUp(shell1, "New User", "ERROR\nPlease enter the Address");
				}
				if (parentText.getCharCount() == (0)) {
					flag = false;
					popUp(shell1, "New User", "ERROR\nPlease enter the Parent/Guardian name");
				}
				if (dayDropDown.getText() == null) {
					flag = false;
					popUp(shell1, "New User", "ERROR\nPlease enter the Day of the DOB");
				}
				if (monthDropDown.getText() == null) {
					flag = false;

					popUp(shell1, "New User", "ERROR\nPlease enter the Month of the DOB");
				}
				if (yrDropDown.getText() == null) {
					flag = false;
					popUp(shell1, "New User", "ERROR\nPlease enter the Year of the DOB");
				}
				if (sexDropDown.getText() == null) {
					flag = false;
					popUp(shell1, "New User", "ERROR\nPlease enter the Gender");
				}
				if (arrayLength <= 0) {
					flag = false;
					popUp(shell1, "New User", "ERROR\nPlease choose atleast one language");
				}
				if (!(match(emailText.getText()))) {
					flag = false;
					popUp(shell1, "New User", "ERROR\nPlease enter your correct Email Id\neg)name@email.com");
				}
				// till here

				// all fields filled ,flag=true
				if (flag) {

					int selection[] = new int[numberLang];
					for (int j = 0; j < numberLang; j++) {
						if (langArray[j].getSelection() == false) {
							selection[j] = -1;
						} else {
							if (langPrevSel[j] > 1) {
								selection[j] = langPrevSel[j];
							} else {
								selection[j] = 1;
							}
						}
					}
					// itemb.setText(nameText.getText());

					// populates a language array with the selected
					// languages MAIN PAGE
					String[] languages = new String[arrayLength];

					for (int q = 0; q < numberLang; q++) {
						if (langArray[q].getSelection()) {
							String lang = langArray[q].getText();
							languages[n++] = lang;

						}
					}

					// to fill the lang drop down on the main page MAIN
					// PAGE
					MainClass.langChosen = languages[0];// by dafault the first
					// language is the
					// assumed selected
					// one
					if (arrayLength > 1) {
						MainClass.langDrop.setVisible(true);
						MainClass.langDrop.setItems(languages);

					}

					// 2) ONLY ONE LANGUAGE SO DISPLAY THE FIRST
					// SENTENCE OF THE CHOSEN LANGUAGE

					else {

						System.out.println("this is the langChose" + MainClass.langChosen);
						MainClass.langDrop.setVisible(false);
						String sentenceFromTable;

						em = factory.createEntityManager();
						em.getTransaction().begin();
						if (MainClass.langChosen == "Kannada") {
							KannadaSentences ks = em.find(KannadaSentences.class, 1);
							sentenceFromTable = ks.getSentenceText();
							MainClass.langText.setText(sentenceFromTable);
							em.persist(ks);
							logger.info(" only kannada to display:has been displayed");
						}
						if (MainClass.langChosen == "Tamil") {
							TamilSentences ts = em.find(TamilSentences.class, 1);
							sentenceFromTable = ts.getSentenceText();
							MainClass.langText.setText(sentenceFromTable);
							em.persist(ts);
							logger.info(" only Tamil to display:has been displayed");
						}

						em.getTransaction().commit();
						em.close();

					}
					// till here

					// 3)UPDATE THE DB

					String dob = dayDropDown.getText() + "-" + monthDropDown.getText() + "-"
							+ yrDropDown.getText();// creating the dob

					em = factory.createEntityManager();
					em.getTransaction().begin();

					UserInfo userInfo = em.find(UserInfo.class, nameIdText.getText());
					userInfo.setName(nameText.getText());
					userInfo.setDob(dob);
					userInfo.setGender(sexDropDown.getText());
					userInfo.setAddress(addText.getText());
					userInfo.setParentGuardianName(parentText.getText());
					userInfo.setContactNumber(numText.getText());
					userInfo.setEmailId(emailText.getText());
					userInfo.setLastRecordedKannadaSentence(selection[0]);
					userInfo.setLastRecordedTamilSentence(selection[1]);
					userInfo.setLastRecordedHindiSentence(selection[2]);
					userInfo.setLastRecordedEnglishSentence(selection[3]);
					try {
						if (selection[0] == 1) {

							ByteArrayOutputStream bout = new ByteArrayOutputStream();
							DataOutputStream dout = new DataOutputStream(bout);
							for (int p = 0; p < MainClass.jumperLength; p++) {

								dout.writeInt(p);

							}
							dout.close();
							byte[] asBytes = bout.toByteArray();
							userInfo.setJumperArrayKannada(asBytes);
							bout.close();

						}

						if (selection[1] == 1) {

							ByteArrayOutputStream bout = new ByteArrayOutputStream();
							DataOutputStream dout = new DataOutputStream(bout);
							for (int p = 0; p < MainClass.jumperLength; p++) {

								dout.writeInt(p);

							}
							dout.close();
							byte[] asBytes = bout.toByteArray();
							userInfo.setJumperArrayKannada(asBytes);
							bout.close();

						}

					} catch (IOException e) {

						e.printStackTrace();
					}
					em.persist(userInfo);
					em.getTransaction().commit();
					em.close();
					// factory.close();
					logger.info(" successfully populated userinfo in the UserInfo tabel");

					//

					shell1.close();

				}

			}

		});

		shell1.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event e) {

				// shell.setEnabled(true);
			}
		});

		shell1.open();
	}
}
