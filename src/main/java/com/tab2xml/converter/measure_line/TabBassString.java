package com.tab2xml.converter.measure_line;

import com.tab2xml.converter.note.BassNoteFactory;
import com.tab2xml.converter.note.NoteFactory;
import com.tab2xml.utility.AnchoredText;

public class TabBassString extends TabGuitarString{
    public TabBassString(int stringNumber, AnchoredText dataAT, AnchoredText nameAT) {
        super(stringNumber, dataAT, nameAT);
    }

	@Override
	protected NoteFactory createNoteFactory() {
		return new BassNoteFactory();
	}
}
