package org.iisc.mile.tts.voiceofchoice.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class Sentences implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int sentenceId;
	
	private String sentenceText;

	private String Dtype;
	
	public Sentences() {
	}

	public int getSentenceId() {
		return this.sentenceId;
	}

	public void setSentenceId(int id) {
		this.sentenceId = id;
	}

	public String getSentenceText() {
		return this.sentenceText;
	}

	public void setSentenceText(String sentenceText) {
		this.sentenceText = sentenceText;
	}

	public void setDtype(String dtype) {
		this.Dtype = dtype;
	}

	public String getDtype() {
		return Dtype;
	}

}