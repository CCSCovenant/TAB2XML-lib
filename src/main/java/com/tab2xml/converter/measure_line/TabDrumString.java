package com.tab2xml.converter.measure_line;

import com.tab2xml.converter.note.DrumNoteFactory;
import com.tab2xml.converter.note.NoteFactory;
import com.tab2xml.converter.note.TabNote;
import com.tab2xml.utility.AnchoredText;
import com.tab2xml.utility.DrumUtils;
import com.tab2xml.utility.Settings;
import com.tab2xml.utility.ValidationError;

import java.util.List;

public class TabDrumString extends TabString {

    public TabDrumString(int stringNumber, AnchoredText dataAT, AnchoredText nameAT) {
        super(stringNumber, dataAT, nameAT);    
    }
    
	@Override
	protected NoteFactory createNoteFactory() {
		return new DrumNoteFactory();
	}

    public List<ValidationError> validate() {
        
        if (!DrumUtils.getNickNameSet().contains(this.name.strip())) {
            addError("This drum piece is not recognized. Update Settings -> Current Song Settings to include it", 1, getRanges());
        }

        for (ValidationError error : errors) {
            if (error.getPriority() <= Settings.getInstance().criticalErrorCutoff) {
                return errors;
            }
        }

        for (TabNote note : this.noteList)
            errors.addAll(note.validate());

        return errors;
    }
}
