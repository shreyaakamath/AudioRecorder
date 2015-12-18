package org.iisc.mile.tts.voiceofchoice.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DatabaseUtil {
	private static Logger logger = Logger.getLogger(DatabaseUtil.class.getName());

	private static final String CONFIGURATION_FILE = "/conf.properties";
	private static final String KANNADA_SENTENCES_FILE = "/Kannada.txt";
	private static final String TAMIL_SENTENCES_FILE = "/Tamil.txt";
	private static final String HINDI_SENTENCES_FILE = "/Hindi.txt";
	private static final String ENGLISH_SENTENCES_FILE = "/English.txt";

	static Properties properties = new Properties();
	private static final String DATABASE_LOCATION_KEY = "DATABASE_LOCATION";

	public static final String JPA_NAME = "TTS_VoiceOfChoice_JPA";

	static {
		try {
			properties.load(DatabaseUtil.class.getResourceAsStream(CONFIGURATION_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		createTables();
		populateSentences();
	}

	static void createTables() throws ClassNotFoundException, SQLException {
		String databaseLocation = properties.getProperty(DATABASE_LOCATION_KEY);
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		Connection con = DriverManager.getConnection("jdbc:derby:" + databaseLocation + ";create=true;");
		Statement createStatement = con.createStatement();

		createStatement.executeUpdate("CREATE TABLE UserInfo (UserId VARCHAR(120) PRIMARY KEY, "
				+ "Name VARCHAR(120), Dob VARCHAR(40), Gender VARCHAR(10), "
				+ "ParentGuardianName VARCHAR(120), Address VARCHAR(500), ContactNumber VARCHAR(120),"
				+ "EmailId VARCHAR(120), LastRecordedKannadaSentence INT, "
				+ "LastRecordedTamilSentence INT, LastRecordedHindiSentence INT, "
				+ "LastRecordedEnglishSentence INT, jumperArrayKannada BLOB, jumperArrayTamil BLOB, "
				+ "jumperArrayHindi BLOB, jumperArrayEnglish BLOB)");
		logger.info("UserInfo table created.");

		createStatement.executeUpdate("CREATE TABLE Sentences (SentenceId INT  PRIMARY KEY, "
				+ "Dtype VARCHAR(10), SentenceText VARCHAR(120))");
		createStatement.executeUpdate("CREATE TABLE KannadaSentences (SentenceId INT  PRIMARY KEY, "
				+ "Dtype VARCHAR(10), SentenceText VARCHAR(120))");
		createStatement.executeUpdate("CREATE TABLE TamilSentences (SentenceId INT  PRIMARY KEY, "
				+ "Dtype VARCHAR(10), SentenceText VARCHAR(120))");
		createStatement.executeUpdate("CREATE TABLE HindiSentences (SentenceId INT  PRIMARY KEY, "
				+ "Dtype VARCHAR(10), SentenceText VARCHAR(120))");
		createStatement.executeUpdate("CREATE TABLE EnglishSentences (SentenceId INT  PRIMARY KEY, "
				+ "Dtype VARCHAR(10), SentenceText VARCHAR(120))");
		logger.info("Sentences tables created.");

		createStatement.executeUpdate("CREATE TABLE RecordedAudio (SentenceId INT, UserId VARCHAR(10), "
				+ "PRIMARY KEY(SentenceId, UserId), ID INT, Dtype VARCHAR(10), RecordedAudio BLOB)");
		createStatement.executeUpdate("CREATE TABLE KannadaRecordedAudio (SentenceId INT, "
				+ "UserId VARCHAR(10), PRIMARY KEY(SentenceId, UserId), ID INT, "
				+ "Dtype VARCHAR(10), RecordedAudio BLOB)");
		createStatement.executeUpdate("CREATE TABLE TamilRecordedAudio (SentenceId INT, "
				+ "UserId VARCHAR(10), PRIMARY KEY(SentenceId, UserId), ID INT, "
				+ "Dtype VARCHAR(10), RecordedAudio BLOB)");
		createStatement.executeUpdate("CREATE TABLE HindiRecordedAudio (SentenceId INT, "
				+ "UserId VARCHAR(10), PRIMARY KEY(SentenceId, UserId), ID INT, "
				+ "Dtype VARCHAR(10), RecordedAudio BLOB)");
		createStatement.executeUpdate("CREATE TABLE EnglishRecordedAudio (SentenceId INT, "
				+ "UserId VARCHAR(10), PRIMARY KEY(SentenceId, UserId), ID INT, "
				+ "Dtype VARCHAR(10), RecordedAudio BLOB)");
		logger.info("RecordedAudio tables created.");

		// createStatement.executeUpdate("CREATE TABLE OPENJPA_SEQUENCE_TABLE (ID DECIMAL(15) PRIMARY KEY, SEQUENCE_VALUE DECIMAL(15))");
		// createStatement.executeUpdate("INSERT INTO OPENJPA_SEQUENCE_TABLE (SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0)");

		createStatement.close();
		con.close();
	}

	public static void populateSentences() throws IOException, ClassNotFoundException, SQLException {
		logger.info("Successfully loaded properties file");
		populateSentences(DatabaseUtil.class.getResourceAsStream(KANNADA_SENTENCES_FILE), "Kan");
		logger.info("Successully loaded Kannada sentences into database");
		populateSentences(DatabaseUtil.class.getResourceAsStream(TAMIL_SENTENCES_FILE), "Tam");
		logger.info("Successully loaded Tamil sentences into database");
	}

	static void populateSentences(InputStream sentencesStream, String dtype) throws IOException {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(JPA_NAME);
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		InputStreamReader inputStreamReader = new InputStreamReader(sentencesStream, "UTF-16");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		entityManager.getTransaction().begin();
		int count = 1;
		String lineText;
		while ((lineText = bufferedReader.readLine()) != null) {
			if (lineText.trim().isEmpty()) {
				continue;
			}
			Sentences sentence;
			if (dtype.equals("Kan")) {
				sentence = new KannadaSentences();
			} else {
				sentence = new TamilSentences();
			}
			sentence.setSentenceId(count++);
			sentence.setSentenceText(lineText);
			sentence.setDtype(dtype);
			entityManager.persist(sentence);
		}
		entityManager.getTransaction().commit();

		inputStreamReader.close();
		entityManager.close();
		entityManagerFactory.close();
	}
}
