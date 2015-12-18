package org.iisc.mile.tts.voiceofchoice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import org.iisc.mile.tts.voiceofchoice.model.KannadaRecordedAudio;
import org.iisc.mile.tts.voiceofchoice.model.RecordedAudioId;
import org.iisc.mile.tts.voiceofchoice.model.TamilRecordedAudio;

class CallAnime extends TimerTask {
	protected static boolean animeFlag = true;
	private static Logger logger = Logger.getLogger(CallAnime.class.getName());

	public static void stopTimerAnimation() {
		try {
			animeFlag = false;
			// Audio.timer.cancel();
			// Audio.timer.purge();
			Animation.redraw(5);
			Animation.bounceAnimeFlag = false;
		} catch (NullPointerException e) {
			logger.info("in timer exception ");
		}
	}

	public void run() {
		if (animeFlag) {
			double rmsValue = Audio.rmsValue;
			System.out.println("RMS" + rmsValue);
			Animation.redraw((int) rmsValue);
		}

	}
}

public class Audio {
	private static AudioFormat format;
	private static DataLine.Info info;
	private static TargetDataLine line;
	private static boolean running;
	private static ByteArrayOutputStream out;
	private static int MAX_TIME_TO_RUN = 33;
	protected static boolean playRun = true;
	protected static ByteArrayOutputStream animeOut = new ByteArrayOutputStream();
	// protected static Timer timer;
	private static Logger logger = Logger.getLogger(Audio.class.getName());
	protected static double rmsValue;

	AudioFormat getFormat() {
		float sampleRate = 8000;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	public static double getRmsValue(byte[] raw) {
		if (raw.length == 0) {
			return 0;
		}
		double mean = 0;
		for (int i = 0; i < raw.length; i++) {
			mean += raw[i];
		}
		mean /= raw.length;
		double sumMeanSquare = 0;
		for (int i = 0; i < raw.length; i++) {
			sumMeanSquare += Math.pow(raw[i] - mean, 2);
		}
		double rootMeanSquare = Math.sqrt(sumMeanSquare / raw.length);
		return rootMeanSquare;
	}

	protected void lineOpen() {
		format = getFormat();
		info = new DataLine.Info(TargetDataLine.class, format);
		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
		} catch (LineUnavailableException e1) {

			e1.printStackTrace();
		}

	}

	protected void lineClose() {
		line.stop();
		line.drain();
		line.close();
		Animation.bounceAnimeFlag = false;
		CallAnime.animeFlag = false;
		running = false;
		playRun = false;

	}

	protected void lineDrain() {
		line.stop();
		line.drain();
	}

	protected void setRunning(boolean valueToSet) {
		running = valueToSet;
	}

	void captureAudioFinal() {

		line.start();
		Runnable runner = new Runnable() {
			int bufferSize = (int) format.getSampleRate() * format.getFrameSize() * MAX_TIME_TO_RUN / 1000;
			byte buffer[] = new byte[bufferSize];

			public void run() {
				out = new ByteArrayOutputStream();
				running = true;
				// timer = new Timer();
				// timer.scheduleAtFixedRate(new CallAnime(), 0, MAX_TIME_TO_RUN);
				try {
					while (running) {
						int count = line.read(buffer, 0, buffer.length);
						if (count > 0) {
							out.write(buffer, 0, count);
							rmsValue = Audio.getRmsValue(buffer);
							System.out.println("RMS" + rmsValue);
							System.out.flush();
							Animation.redraw((int) rmsValue);
						}
					}
					out.close();
				} catch (IOException e) {
					logger.info("capturing line ERROR");
					System.exit(-1);
				}
			}
		};
		Thread captureThread = new Thread(runner);
		captureThread.start();
	}

	void playAudioFinal(final int sentenceId) {
		try {
			try {
				final byte audio[];
				if (MainClass.audioFlag == false) {
					audio = out.toByteArray();
				} else {
					EntityManagerFactory factory = Persistence
							.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
					EntityManager em1 = factory.createEntityManager();
					RecordedAudioId rs = new RecordedAudioId();
					rs.sentenceId = sentenceId;
					rs.userId = MainClass.selUserId;
					if ("Kannada".equalsIgnoreCase(MainClass.langChosen.trim())) {
						KannadaRecordedAudio kra = em1.find(KannadaRecordedAudio.class, rs);
						audio = kra.getRecordedAudio();
					} else {
						TamilRecordedAudio tra = em1.find(TamilRecordedAudio.class, rs);
						audio = tra.getRecordedAudio();
					}
					em1.close();
					factory.close();
				}
				InputStream input = new ByteArrayInputStream(audio);
				final AudioFormat format = getFormat();
				final AudioInputStream ais = new AudioInputStream(input, format, audio.length
						/ format.getFrameSize());
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
				final SourceDataLine line1 = (SourceDataLine) AudioSystem.getLine(info);
				line1.open(format);
				line1.start();

				Runnable runner = new Runnable() {
					int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
					byte buffer[] = new byte[bufferSize];

					public void run() {
						try {
							int count;

							while (playRun && (count = ais.read(buffer, 0, buffer.length)) != -1) {
								if (count > 0) {
									line1.write(buffer, 0, count);

								}
							}

							line1.drain();
							line1.close();
						} catch (IOException e) {
							System.err.println("I/O problems: " + e);
							System.exit(-3);
						}

					}
				};
				Thread playThread = new Thread(runner);
				playThread.start();
			} catch (LineUnavailableException e) {
				System.err.println("Line unavailable: " + e);
				System.exit(-4);
			}

		} catch (NullPointerException e) {
			if (MainClass.playCheckerNorm == false) {
				EditUser.popUp(MainClass.s, "Error!", "Sorry nothing to play ");
			}
		}
	}

	void saveRecordedAudio(int sentenceId) {
		final byte audio[];
		audio = out.toByteArray();
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("TTS_VoiceOfChoice_JPA");
		EntityManager em1 = factory.createEntityManager();
		em1.getTransaction().begin();
		System.out.println(MainClass.langChosen);
		if ("Kannada".equalsIgnoreCase(MainClass.langChosen.trim())) {
			KannadaRecordedAudio kra = new KannadaRecordedAudio();
			kra.setDtype("Kan");
			kra.setRecordedAudio(audio);
			kra.setSentenceId(sentenceId);
			kra.setUserId(MainClass.selUserId);
			em1.persist(kra);
		} else {
			TamilRecordedAudio tra = new TamilRecordedAudio();
			tra.setDtype("Tam");
			tra.setRecordedAudio(audio);
			tra.setSentenceId(sentenceId);
			tra.setUserId(MainClass.selUserId);
			em1.persist(tra);
		}
		em1.getTransaction().commit();
		em1.close();
		factory.close();
	}

}