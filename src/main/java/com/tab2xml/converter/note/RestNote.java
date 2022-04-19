package com.tab2xml.converter.note;

import com.tab2xml.models.measure.note.Note;
import com.tab2xml.models.measure.note.Rest;
import com.tab2xml.utility.ValidationError;

import java.util.List;

public class RestNote extends TabNote {

    public RestNote(int stringNumber, String origin, int position, String lineName, int distanceFromMeasureStart) {
        super(stringNumber, origin, position, lineName, distanceFromMeasureStart);
    }

    public RestNote(RestNote n) {
    	super(n);
    }

	@Override
	public TabNote copy() {
		return new RestNote(this);
	}
	
    @Override
	protected void setStems(Note noteModel) {
		// Not stems for rests
	}

	@Override
	public Note getModel() {
	    Note noteModel = super.getModel();
	    noteModel.setRest(new Rest());
	    return noteModel;
	}

	public List<ValidationError> validate() {
	    super.validate();
	    return errors;
	}
}
