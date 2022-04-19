package com.tab2xml.converter;

import com.tab2xml.utility.MusicXMLCreator;
import com.tab2xml.utility.ValidationError;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Converter {

	private Score score;
	private MusicXMLCreator mxlc;

	public Converter(String Tablature) {
		score = new Score(Tablature);
		mxlc = new MusicXMLCreator(score);
	}
	
	public void update() {

	}
	
	public String getMusicXML() {
		return mxlc.generateMusicXML();
	}
	
	public Score getScore() {
		return score;
	}
	
	public List<ValidationError> validate() {
		return score.validate();
	}
    public void saveMusicXMLFile(File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(mxlc.generateMusicXML());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
