package org.iisc.mile.tts.voiceofchoice.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class RecordedAudio implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	RecordedAudioId recordedAudioId;

	private byte[] recordedAudio;

	private String Dtype;

	public RecordedAudio() {
		recordedAudioId = new RecordedAudioId();
	}

	public int getSentenceId() {
		return recordedAudioId.getSentenceId();
	}

	public void setSentenceId(int sentenceId) {
		recordedAudioId.setSentenceId(sentenceId);
	}

	public String getUserId() {
		return recordedAudioId.getUserId();
	}

	public void setUserId(String userId) {
		recordedAudioId.setUserId(userId);
	}

	public byte[] getRecordedAudio() {
		return recordedAudio;
	}

	public void setRecordedAudio(byte[] recordedAudio) {
		this.recordedAudio = recordedAudio;
	}

	public void setDtype(String dtype) {
		this.Dtype = dtype;
	}

	public String getDtype() {
		return this.Dtype;
	}
}
