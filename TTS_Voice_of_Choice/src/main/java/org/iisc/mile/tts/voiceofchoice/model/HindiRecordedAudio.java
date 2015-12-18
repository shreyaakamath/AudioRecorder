package org.iisc.mile.tts.voiceofchoice.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@DiscriminatorValue("Hin")
public class HindiRecordedAudio extends RecordedAudio implements Serializable {
	private static final long serialVersionUID = 1L;
}