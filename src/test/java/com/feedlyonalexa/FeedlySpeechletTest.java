package com.feedlyonalexa;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;

@Ignore
public class FeedlySpeechletTest
{
	@Test
	public void testOnLaunch() throws Exception
	{
		// Arrange
		LaunchRequest launchRequest = LaunchRequest.builder().withRequestId("requestId").build();
		Session session = Session.builder().withSessionId("sessionId").build();
		
		// Act
		SpeechletResponse response = speechlet.onLaunch(launchRequest, session);
		
		// Assert
		assertTrue(response.getOutputSpeech() instanceof PlainTextOutputSpeech);
		assertEquals("Welcome to Feedly", ((PlainTextOutputSpeech)response.getOutputSpeech()).getText());
	}
	
	private static final FeedlySpeechlet speechlet = new FeedlySpeechlet();
}
