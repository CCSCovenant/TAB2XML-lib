package com.tab2xml.converter.note;

import com.tab2xml.models.measure.note.Note;
import com.tab2xml.utility.ValidationError;

import java.util.List;

public class TieNote extends TabNote {

    public TieNote(int stringNumber, String origin, int position, String lineName, int distanceFromMeasureStart) {
        super(stringNumber, origin, position, lineName, distanceFromMeasureStart);
    }

    public TieNote(TieNote n) {
    	super(n);
    }

	@Override
	public TabNote copy() {
		return new TieNote(this);
	}
	
    @Override
	protected void setStems(Note noteModel) {
		// Not stems for fake notes
	}

	@Override
	public Note getModel() {
	    Note noteModel = super.getModel();
	    return noteModel;
	}

	public List<ValidationError> validate() {
	    super.validate();
	    return errors;
	}
}
