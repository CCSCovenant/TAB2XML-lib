package com.tab2xml.converter.measure;

import com.tab2xml.converter.measure_line.TabBassString;
import com.tab2xml.converter.measure_line.TabString;
import com.tab2xml.utility.AnchoredText;
import com.tab2xml.utility.Settings;

import java.util.List;

public class BassMeasure extends GuitarMeasure {

    public BassMeasure(List<AnchoredText> inputData, List<AnchoredText> inputNameData, boolean isFirstMeasure) {
        super(inputData, inputNameData, isFirstMeasure);
        MIN_LINE_COUNT = 4;
        MAX_LINE_COUNT = 4;
        tuning = Settings.getInstance().getBassTuning();
    }

	protected TabString newTabString(int stringNumber, AnchoredText data, AnchoredText name)
	{
		return new TabBassString(stringNumber, data, name);
	}
    
}
