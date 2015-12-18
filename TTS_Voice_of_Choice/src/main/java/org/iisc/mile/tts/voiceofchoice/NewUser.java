package org.iisc.mile.tts.voiceofchoice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

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

public class NewUser  {
	private static EntityManagerFactory factory;
	private static EntityManager em;
	private static Logger logger = Logger.getLogger(NewUser.class.getName());
	private static Shell sChoose = new Shell(MainClass.d);
	private static String selUserOld;
	private boolean fromDropDown = false;
	private static List<?> list;
	private static boolean regCompleteFlag;
	protected  static int  numberLang=4;

	void multipleUsersLoading(String nameId) {
		// if (counterNext != 1) {
		System.out.println("entered the loading part");
		String selUserLocal;
		selUserLocal = nameId;
		MainClass.setAllButtons(true);
		MainClass.langText.setText(" ");

		MainClass.s.setText(selUserLocal + "-TTS Voice of choice");
		// store the counter in the present selUserId's row

		selUserOld = MainClass.selUserId;
		fromDropDown = true;

		if (MainClass.selUserId != null) {

			factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
			em = factory.createEntityManager();
			em.getTransaction().begin();

			UserInfo curUser = em.find(UserInfo.class, MainClass.selUserId);

			if (MainClass.audioFlag == true) {
				MainClass.counterNext += 1;
			}

			if (("Kannada".equalsIgnoreCase(MainClass.langChosen.trim()))) {
				MainClass.langChosen = "Kannada";
				curUser.setLastRecordedKannadaSentence(MainClass.counterNext);

			} else {
				MainClass.langChosen = "Tamil";

				curUser.setLastRecordedTamilSentence(MainClass.counterNext);
			}
			em.persist(curUser);
			em.getTransaction().commit();
			em.close();
			factory.close();
			// if(langChosen==Hindi) etc

			logger.info("successfully updates the counter in the userInfo table while shell close");
			// }end of 1 if
		}

		// change the selUserId to the selected one
		MainClass.selUserId=selUserLocal;
		// selUserId = selUserLocal;

		// check number of known languages if one then display load the sentences and make drop
		// down invisible
		// if more make populate the drop down properly
		int languageCheck;
		int numberLanguages = 0;
		factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
		em = factory.createEntityManager();
		em.getTransaction().begin();
		UserInfo user = em.find(UserInfo.class, MainClass.selUserId);
		languageCheck = user.getLastRecordedKannadaSentence();
		if (languageCheck > 0) {
			numberLanguages++;
		}
		languageCheck = user.getLastRecordedTamilSentence();
		if (languageCheck > 0) {
			numberLanguages++;
		}
		languageCheck = user.getLastRecordedEnglishSentence();
		if (languageCheck > 0) {
			numberLanguages++;
		}
		languageCheck = user.getLastRecordedHindiSentence();
		if (languageCheck > 0) {
			numberLanguages++;
		}

		String languagesUsers[] = new String[numberLanguages];
		int i = 0;

		// populate an array with the known languages
		languageCheck = user.getLastRecordedKannadaSentence();
		if (languageCheck > 0) {
			languagesUsers[i] = "Kannada";
			i++;
		}
		languageCheck = user.getLastRecordedTamilSentence();
		if (languageCheck > 0) {
			languagesUsers[i] = "Tamil";
			i++;
		}
		languageCheck = user.getLastRecordedEnglishSentence();
		if (languageCheck > 0) {
			languagesUsers[i] = "English";
			i++;
		}
		languageCheck = user.getLastRecordedHindiSentence();
		if (languageCheck > 0) {
			languagesUsers[i] = "Hindi";
			i++;
		}
		MainClass.langChosen=languagesUsers[0];
		// langChosen = languagesUsers[0];

		// check if one or multiple
		if (numberLanguages == 1) {
			MainClass.langDrop.setVisible(false);
			MainClass.setAllButtons(true);

			// get the counter

			factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
			em = factory.createEntityManager();
			em.getTransaction().begin();
			UserInfo curUser = em.find(UserInfo.class, MainClass.selUserId);

			if ("Kannada".equalsIgnoreCase(MainClass.langChosen.trim())) {
				MainClass.counterNext = curUser.getLastRecordedKannadaSentence();
			} else {
				MainClass.counterNext = curUser.getLastRecordedTamilSentence();

			}
			em.persist(curUser);
			em.getTransaction().commit();
			em.close();

			factory.close();

			// get the sentence

			factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
			em = factory.createEntityManager();
			em.getTransaction().begin();

			if ("Kannada".equalsIgnoreCase(MainClass.langChosen.trim())) {

				KannadaSentences ks = em.find(KannadaSentences.class, MainClass.counterNext);
				MainClass.langText.setText(ks.getSentenceText());
				em.persist(ks);
			} else {
				TamilSentences ts = em.find(TamilSentences.class, MainClass.counterNext);
				MainClass.langText.setText(ts.getSentenceText());

				em.persist(ts);
			}
			em.getTransaction().commit();
			em.close();
			factory.close();
		} else {
			MainClass.langDrop.setForeground(MainClass.d.getSystemColor(SWT.COLOR_YELLOW));
			MainClass.langDrop.setVisible(true);
			if(MainClass.langDrop.getSelectionIndex()==-1){
			MainClass.setAllButtons(false);
			MainClass.langDrop.setEnabled(true);
			}else{
				MainClass.setAllButtons(true);
			}
			MainClass.langDrop.setItems(languagesUsers);
		}
		System.out.println("exiting the load part");

		// }// end of 1 if
	}

	void multipleUsersChoosing() {
		
		
		MainClass.s.setEnabled(false);
		
		sChoose.setText("Choosing a User");
		Point pt=MainClass.s.getLocation();
		sChoose.setLocation(pt.x+200,pt.y+150);
		//sChoose.setLocation(300,200);
		sChoose.setSize(300, 300);
		GridLayout gd = new GridLayout(3, false);
		sChoose.setLayout(gd);
		gd.marginHeight = 60;
		gd.marginWidth = 40;

		GridData gdLabel = new GridData(SWT.FILL, SWT.FILL, true, false);
		gdLabel.horizontalSpan = 5;
		gdLabel.verticalIndent = 15;
		Label chooseLabel = new Label(sChoose, SWT.NONE);
		chooseLabel.setText("Please  Choose");
		Label chooseLabel1 = new Label(sChoose, SWT.NONE);
		chooseLabel1.setText("  your  ID:");
		final Combo idDropDown = new Combo(sChoose, SWT.READ_ONLY | SWT.DROP_DOWN | SWT.BORDER);
		UserInfo ui = new UserInfo();
		list = ui.getUserIdList();
		Iterator<?> iter = list.iterator();
		while (iter.hasNext()) {
			String id = iter.next().toString();
			idDropDown.add(id);
		}
		idDropDown.setLayoutData(gdLabel);

		GridData gdButton1 = new GridData(SWT.FILL, SWT.FILL, true, false);
		gdButton1.verticalIndent = 15;
		gdButton1.horizontalIndent = 15;
		Button next = new Button(sChoose, SWT.PUSH);
		next.setLayoutData(gdButton1);
		next.setText("   OK   ");

		GridData gdButton2 = new GridData(SWT.FILL, SWT.FILL, true, false);
		gdButton2.verticalIndent = 15;
		// gdButton2.horizontalIndent=5;
		Button cancel = new Button(sChoose, SWT.PUSH);
		cancel.setLayoutData(gdButton2);
		cancel.setText("  Cancel  ");

		GridData gdButton3 = new GridData(SWT.FILL, SWT.FILL, true, false);
		gdButton3.verticalIndent = 15;
		gdButton3.horizontalIndent = 5;
		Button newUser = new Button(sChoose, SWT.PUSH);
		newUser.setLayoutData(gdButton3);
		newUser.setText(" New User ");

		
		next.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				
				
				String id = idDropDown.getText();
				 System.out.println("this is the id" + id);
					
					multipleUsersLoading(id);
					MainClass.s.setEnabled(true);
					sChoose.close();
					
				
				
				

			}
		});
	
		

		cancel.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				MainClass.s.setEnabled(true);
				sChoose.close();

			}
		});

		newUser.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				newUserPage();

			}
		});
		
		sChoose.addListener(SWT.Close, new Listener(){
			public void handleEvent(Event e){
				MainClass.s.setEnabled(true);
			}
		});
		sChoose.open();

	}

	void newUserListener(final String nameId, MenuItem userItem) {

		userItem.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {

				multipleUsersLoading(nameId);
			}
		});
	}

	void newUserPage() {
		
		MainClass.s.setEnabled(false);
		
		System.out.println("in the new user listener");

		final int oldCounterNext = MainClass.counterNext;
		
		MainClass.langDrop.setForeground(MainClass.d.getSystemColor(SWT.COLOR_YELLOW));
		final Shell shell1 = new Shell(MainClass.d);
		Point pt=MainClass.s.getLocation();
		shell1.setLocation(pt.x+150,pt.y+50);
		shell1.setSize(500, 500);
		shell1.setText("New User ");
		// shell1.setImage(new Image(d, NewUser.class
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
        final Text nameIdText = new Text(shell1, SWT.BORDER);
        gdnameId.horizontalSpan = 3;
        nameIdText.setLayoutData(gdnameId);
        nameIdText.setTextLimit(150);

		GridData gdCheckId = new GridData(SWT.FILL, SWT.FILL, true, false);
		Button checkAvailable = new Button(shell1, SWT.PUSH);
		checkAvailable.setText("check availability!");
		gdCheckId.horizontalSpan = 1;
		checkAvailable.setLayoutData(gdCheckId);

		GridData gdCheckId1 = new GridData(SWT.FILL, SWT.FILL, true, false);
		final Label labelAvail = new Label(shell1, SWT.NONE);
		gdCheckId1.horizontalSpan = 3;
		labelAvail.setLayoutData(gdCheckId1);	
	
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
						// if( items[i].toLowerCase().compareTo(key)==0){
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
		final Text addText = new Text(shell1, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
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
				MainClass.s.setEnabled(true);
				shell1.close();
			}
		};

		cancelIt.addListener(SWT.Selection, listener);
		if (MainClass.selUserId != null) {
			factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
			em = factory.createEntityManager();
			em.getTransaction().begin();

			if (MainClass.audioFlag == true) {
				MainClass.counterNext += 1;
			}
			UserInfo curUser = em.find(UserInfo.class, MainClass.selUserId);

			if ("Kannada".equalsIgnoreCase(MainClass.langChosen.trim())) {

				curUser.setLastRecordedKannadaSentence(MainClass.counterNext);
			} else {

				curUser.setLastRecordedTamilSentence(MainClass.counterNext);
			}

			em.persist(curUser);
			em.getTransaction().commit();
			em.close();
			factory.close();

			logger.info("successfully updates the counter  in the userInfo table");
		}


		checkAvailable.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {

                UserInfo ui = new UserInfo();
                list = ui.getUserIdList();
                Iterator<?> iter = list.iterator();

                boolean availFlag = true;
                while (iter.hasNext()) {

                    String id = iter.next().toString();
                    if (id.equalsIgnoreCase(nameIdText.getText())) {
                        availFlag = false;
                        break;
                    } else {
                        availFlag = true;
                    }
                }

                if (availFlag == true) {
                    labelAvail.setText("this Id is available");
                    
                } else {
                    labelAvail.setText("this Id is not available");
                }

            }
        });

		save.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				
				MainClass.s.setEnabled(true);
				int n = 0;
				MainClass.s.setText(nameIdText.getText() + "-TTS Voice of choice");

				regCompleteFlag = true;

				int arrayLength = 0;// determine the number of selected languages which will be later
									// used to populate the languages array
				for (int q = 0; q < numberLang; q++) {
					if (langArray[q].getSelection())
						arrayLength++;
				}

				// check if all details have been furnished correctly

				if (nameText.getCharCount() == (0)) {
					regCompleteFlag = false;
					EditUser.popUp(shell1,"New User", "ERROR\nPlease enter the Name");
				}
				if (nameIdText.getCharCount() == (0)) {
					regCompleteFlag = false;
					EditUser.popUp(shell1,"New User", "ERROR\nPlease enter the User Id");
				}
				if (numText.getCharCount() == (0)) {
					regCompleteFlag = false;
					EditUser.popUp(shell1,"New User", "ERROR\nPlease enter the Contact Number");
				}
				if (addText.getCharCount() == (0)) {
					regCompleteFlag = false;
					EditUser.popUp(shell1,"New User", "ERROR\nPlease enter the Address");
				}
				if (parentText.getCharCount() == (0)) {
					regCompleteFlag = false;
					EditUser.popUp(shell1,"New User", "ERROR\nPlease enter the Parent/Guardian name");
				}
				if (dayDropDown.getText() == null) {
					regCompleteFlag = false;
					EditUser.popUp(shell1,"New User", "ERROR\nPlease enter the Day of the DOB");
				}
				if (monthDropDown.getText() == null) {
					regCompleteFlag = false;
					EditUser.popUp(shell1,"New User", "ERROR\nPlease enter the Month of the DOB");
				}
				if (yrDropDown.getText() == null) {
					regCompleteFlag = false;
					EditUser.popUp(shell1,"New User", "ERROR\nPlease enter the Year of the DOB");
				}
				if (sexDropDown.getText() == null) {
					regCompleteFlag = false;
					EditUser.popUp(shell1,"New User", "ERROR\nPlease enter the Gender");
				}
				if (arrayLength <= 0) {
					regCompleteFlag = false;
					EditUser.popUp(shell1,"New User", "ERROR\nPlease choose atleast one language");
				}
				if (!(EditUser.match(emailText.getText()))) {
					regCompleteFlag = false;
					EditUser.popUp(shell1,"New User", "ERROR\nPlease enter your correct Email Id\neg)name@email.com");
				}
				System.out.println("this is selUser" + MainClass.selUserId);
				// 1)DB UPDATE THE COUNTER IN THE USERINFO TABLE WITH THIS USERID AS PRIMARY KEY.

				if (regCompleteFlag) {
					MainClass.langText.setText(" ");
					MainClass.counterNext = 1;// since reg is done this is a new user thus counterNext=1
					MainClass.selUserId = nameIdText.getText();// similarly selUserId can also change
					System.out.println(MainClass.selUserId);
					MenuItem user = new MenuItem(MainClass.userMenu, SWT.RADIO);
					user.setText(MainClass.selUserId);

					newUserListener(MainClass.selUserId, user);
					logger.info(" after setting the userId in the drop down");
					System.out.println("this is the updates selUserId " + MainClass.selUserId);
					MainClass.langText.setText(" ");// text box should be empty for the new user

					int selection[] = new int[6];// creating a selection array to track the selected
													// and unselected languages
					for (int j = 0; j < numberLang; j++) {
						if (langArray[j].getSelection() == false) {
							selection[j] = -1;
						} else {
							selection[j] = 1;
							// selection[j] = oldCounterNext;
						}
					}

					/*
					 * double array[]=new double[401]; for(int p=0;p<=400;p++){ array[p]=p; }
					 */

					// 2) UPDATE THESE VALUES TO THE USERINFO TABLE

					String dob = dayDropDown.getText() + "-" + monthDropDown.getText() + "-"
							+ yrDropDown.getText();// creating the dob
					factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
					em = factory.createEntityManager();
					em.getTransaction().begin();

					UserInfo userInfo = new UserInfo();
					userInfo.setUserId(nameIdText.getText());
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
						if (selection[0] > 0) {

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

						if (selection[1] > 0) {

							ByteArrayOutputStream bout = new ByteArrayOutputStream();
							DataOutputStream dout = new DataOutputStream(bout);
							for (int p = 0; p < MainClass.jumperLength; p++) {

								dout.writeInt(p);

							}
							dout.close();
							byte[] asBytes = bout.toByteArray();
							userInfo.setJumperArrayTamil(asBytes);
							bout.close();

						}

					} catch (IOException e) {

						e.printStackTrace();
					}

					em.persist(userInfo);
					em.getTransaction().commit();
					em.close();
					factory.close();
					logger.info(" successfully populated userinfo in the UserInfo tabel");

					try {
						factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
						em = factory.createEntityManager();
						UserInfo curUser = em.find(UserInfo.class, MainClass.selUserId);

						int[] dubs = new int[401];
						byte[] asBytes = curUser.getJumperArrayKannada();
						ByteArrayInputStream bin = new ByteArrayInputStream(asBytes);
						DataInputStream din = new DataInputStream(bin);
						for (int i = 0; i < dubs.length; i++) {
							dubs[i] = din.readInt();
							System.out.println(dubs[i]);
						}

						em.close();
						factory.close();
					} catch (IOException e) {

						e.printStackTrace();
					}

					String[] languages = new String[arrayLength];

					for (int q = 0; q < numberLang; q++) {// languages array with only the selected languages
						if (langArray[q].getSelection()) {
							String lang = langArray[q].getText();
							languages[n++] = lang;
						}
					}
					MainClass.langChosen = languages[0];// by dafault the first language is the assumed
														// selected
					// one
					if (arrayLength > 1) {
						MainClass.langDrop.setVisible(true);
						MainClass.setAllButtons(false);
						MainClass.langDrop.setEnabled(true);
						
						MainClass.langDrop.setItems(languages);

					}

					// 3) ONLY ONE LANGUAGE SO DISPLAY THE FIRST SENTENCE OF THE CHOSEN LANGUAGE

					if (arrayLength == 1) {
						MainClass.langDrop.setVisible(false);
					}
					String sentenceFromTable;
					factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
					em = factory.createEntityManager();
					em.getTransaction().begin();
					if ("Kannada".equalsIgnoreCase(MainClass.langChosen)) {
						KannadaSentences ks = em.find(KannadaSentences.class, 1);
						sentenceFromTable = ks.getSentenceText();
						MainClass.langText.setText(sentenceFromTable);
						em.persist(ks);
						logger.info(" only kannada to display:has been displayed");
					}
					if ("Tamil".equalsIgnoreCase(MainClass.langChosen)) {
						TamilSentences ts = em.find(TamilSentences.class, 1);
						sentenceFromTable = ts.getSentenceText();
						MainClass.langText.setText(sentenceFromTable);
						em.persist(ts);
						logger.info(" only Tamil to display:has been displayed");
					}

					em.getTransaction().commit();
					em.close();
					factory.close();
					shell1.close();

				}

			}

		});

		shell1.addListener(SWT.Close, new Listener(){
			public void handleEvent(Event e){
				MainClass.s.setEnabled(true);
			}
		});
		shell1.open();

	}

	void new_user(MenuItem openItem) {

		openItem.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {

				newUserPage();
			}

		});
	}
}
