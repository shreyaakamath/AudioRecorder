package org.iisc.mile.tts.voiceofchoice.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@DiscriminatorValue("Kan")
public class KannadaSentences extends Sentences implements Serializable {
	private static final long serialVersionUID = 1L;
}