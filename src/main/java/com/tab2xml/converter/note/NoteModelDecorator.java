package com.tab2xml.converter.note;

import com.tab2xml.models.measure.note.Note;

public interface NoteModelDecorator {
    boolean applyTo(Note noteModel);
}
