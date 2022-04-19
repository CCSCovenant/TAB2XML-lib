package com.tab2xml.converter.note;

import com.tab2xml.models.measure.note.Note;
import com.tab2xml.utility.ValidationError;

import java.util.List;

public class InvalidNote extends TabNote {

    public InvalidNote(int stringNumber, String origin, int position, String lineName, int distanceFromMeasureStart) {
        super(stringNumber, origin, position, lineName, distanceFromMeasureStart);
    }

    public InvalidNote(InvalidNote n) {
        super(n);
    }
    
    @Override
	public TabNote copy() {
		return new InvalidNote(this);
	}

	@Override
    public Note getModel() {
        return null;
    }

    public List<ValidationError> validate() {

        addError("Unrecognized text, will be ignored.", 3, getRanges());
        return errors;
    }

	@Override
	protected void setStems(Note noteModel) {
		// No stems for invalid notes
	}
}
