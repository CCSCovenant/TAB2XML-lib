package com.tab2xml.converter.note;

import com.tab2xml.models.measure.note.Note;
import com.tab2xml.models.measure.note.notations.Notations;
import com.tab2xml.models.measure.note.notations.Tied;

import java.util.ArrayList;
import java.util.List;

public class StartTieDecorator implements NoteModelDecorator {

	@Override
	public boolean applyTo(Note noteModel) {
            if (noteModel.getNotations() == null) noteModel.setNotations(new Notations());
    	    Notations notations = noteModel.getNotations();
            if (notations.getTieds() == null) notations.setTieds(new ArrayList<>());
            List<Tied> tieds = notations.getTieds();
            tieds.add(new Tied("start"));
            return true;
	}
}
