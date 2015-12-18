package org.iisc.mile.tts.voiceofchoice.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Tam")
public class TamilSentences extends Sentences implements Serializable {
	private static final long serialVersionUID = 1L;
}