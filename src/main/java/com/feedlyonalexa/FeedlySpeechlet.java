package com.feedlyonalexa;

import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;

public class FeedlySpeechlet implements Speechlet {
	public SpeechletResponse onIntent(IntentRequest arg0, Session arg1) throws SpeechletException {
		// TODO Auto-generated method stub
		return null;
	}

	public SpeechletResponse onLaunch(LaunchRequest launchRequest, Session session) throws SpeechletException {
		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText("Welcome to Feedly");
		return SpeechletResponse.newTellResponse(outputSpeech);
	}

	public void onSessionEnded(SessionEndedRequest arg0, Session arg1) throws SpeechletException {
		// TODO Auto-generated method stub
		
	}

	public void onSessionStarted(SessionStartedRequest arg0, Session arg1) throws SpeechletException {
		// TODO Auto-generated method stub
		
	}
}
