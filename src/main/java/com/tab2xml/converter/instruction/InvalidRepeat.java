package com.tab2xml.converter.instruction;

import com.tab2xml.converter.ScoreComponent;
import com.tab2xml.utility.AnchoredText;
import com.tab2xml.utility.ValidationError;

import java.util.List;

public class InvalidRepeat extends Instruction {
	public InvalidRepeat(AnchoredText inputAT, boolean isTop) {
		super(inputAT, isTop);
		hasBeenApplied = true;
	}

	// Invalid (nested) repeats only create a validation error
	public <E extends ScoreComponent> void applyTo(E scoreComponent) {
	}

	public List<ValidationError> validate() {
		super.validate();
		addError("Nested repeats are not supported. Will be ignored.", 3, getRanges());
		return errors;
	}
}
