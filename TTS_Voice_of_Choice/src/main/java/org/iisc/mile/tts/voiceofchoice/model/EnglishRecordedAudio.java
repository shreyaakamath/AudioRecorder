package org.iisc.mile.tts.voiceofchoice.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@DiscriminatorValue("Eng")
public class EnglishRecordedAudio extends RecordedAudio implements Serializable {
	private static final long serialVersionUID = 1L;
}