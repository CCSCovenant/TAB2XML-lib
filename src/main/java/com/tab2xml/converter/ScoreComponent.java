package com.tab2xml.converter;

import com.tab2xml.utility.AnchoredText;
import com.tab2xml.utility.Range;
import com.tab2xml.utility.Settings;
import com.tab2xml.utility.ValidationError;

import java.util.ArrayList;
import java.util.List;

public abstract class ScoreComponent {
	
	public AnchoredText at;
	public abstract List<ValidationError> validate();

	public List<ValidationError> errors = new ArrayList<>();

	public void addError(String message, int priority, List<Range> rangeList) {
		ValidationError error = new ValidationError(message, priority, rangeList);

		if (Settings.getInstance().errorSensitivity >= error.getPriority())
			errors.add(error);
	}
	
	public abstract List<Range> getRanges();
}
