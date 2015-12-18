package org.iisc.mile.tts.voiceofchoice.model;

import javax.persistence.Embeddable;

@Embeddable
public class RecordedAudioId {

	public RecordedAudioId() {
	}

	public int sentenceId;

	public String userId;

	public int getSentenceId() {
		return this.sentenceId;
	}

	public void setSentenceId(int sentenceId) {
		this.sentenceId = sentenceId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RecordedAudioId recordedAudioId = (RecordedAudioId) obj;
		if ((userId == recordedAudioId.userId) && (sentenceId == recordedAudioId.sentenceId)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = 89 * hash + (this.userId != null ? this.userId.hashCode() : 0);
		hash = 89 * hash + (this.sentenceId != 0 ? this.sentenceId : 0);
		return hash;
	}
}
