package com.tab2xml.converter.measure_line;

import com.tab2xml.converter.note.GuitarNoteFactory;
import com.tab2xml.converter.note.NoteFactory;
import com.tab2xml.converter.note.TabNote;
import com.tab2xml.utility.AnchoredText;
import com.tab2xml.utility.GuitarUtils;
import com.tab2xml.utility.Settings;
import com.tab2xml.utility.ValidationError;

import java.util.List;

public class TabGuitarString extends TabString {

    public TabGuitarString(int stringNumber, AnchoredText dataAT, AnchoredText nameAT) {
        super(stringNumber, dataAT, nameAT);
    }

    @Override
    public List<ValidationError> validate() {

        if (!GuitarUtils.isValidName(this.name)) {
            String message = "Unrecognized name";
            addError(message, 1, getRanges());
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

	@Override
	protected NoteFactory createNoteFactory() {
		return new GuitarNoteFactory();
	}
    
}
