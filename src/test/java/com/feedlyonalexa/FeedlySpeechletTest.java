package com.feedlyonalexa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.feedlyonalexa.model.IntentName;
import com.feedlyonalexa.model.SessionAttribute;
import com.feedlyonalexa.util.CardMessages;
import com.feedlyonalexa.util.Prompts;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class FeedlySpeechletTest
{
	@Test
	public void testOnIntent_StopIntent() throws Exception
    {
        // Arrange
        Intent intent = Intent.builder().withName(IntentName.STOP_INTENT.toString()).build();
        IntentRequest intentRequest = IntentRequest.builder().withIntent(intent)
                .withRequestId(REQUEST_ID)
                .build();

        int numberOfArticlesSaved = 5;
        int numberOfArticlesReviewed = 15;
        Session session = Session.builder().withSessionId(SESSION_ID).build();
        session.setAttribute(SessionAttribute.SAVED_ARTICLES, numberOfArticlesSaved);
        session.setAttribute(SessionAttribute.REVIEWED_ARTICLES, numberOfArticlesReviewed);

        // Act
        SpeechletResponse response = speechlet.onIntent(intentRequest, session);

        // Assert
        assertTrue(response.getOutputSpeech() instanceof PlainTextOutputSpeech);
        assertEquals(Prompts.END_SESSION_PROMPT, ((PlainTextOutputSpeech)response.getOutputSpeech()).getText());

        assertTrue(response.getCard() instanceof SimpleCard);
        assertEquals(CardMessages.SUMMARY_TITLE, response.getCard().getTitle());
        assertEquals(CardMessages.buildSummaryMessage(numberOfArticlesSaved, numberOfArticlesReviewed),
                ((SimpleCard)response.getCard()).getContent());
	}

    /*
    Test that we use zeroes if the number of articles saved and reviewed is missing.
     */
    @Test
    public void testOnIntent_StopIntent_MissingSessionAttributes() throws Exception
    {
        // Arrange
        Intent intent = Intent.builder().withName(IntentName.STOP_INTENT.toString()).build();
        IntentRequest intentRequest = IntentRequest.builder().withIntent(intent)
                .withRequestId(REQUEST_ID)
                .build();

        Session session = Session.builder().withSessionId(SESSION_ID).build();

        // Act
        SpeechletResponse response = speechlet.onIntent(intentRequest, session);

        // Assert
        assertTrue(response.getOutputSpeech() instanceof PlainTextOutputSpeech);
        assertEquals(Prompts.END_SESSION_PROMPT, ((PlainTextOutputSpeech)response.getOutputSpeech()).getText());

        assertTrue(response.getCard() instanceof SimpleCard);
        assertEquals(CardMessages.SUMMARY_TITLE, response.getCard().getTitle());
        assertEquals(CardMessages.buildSummaryMessage(0, 0),
                ((SimpleCard)response.getCard()).getContent());
    }

    @Test
    public void testOnIntent_CancelIntent() throws Exception
    {
        // Arrange
        Intent intent = Intent.builder().withName(IntentName.CANCEL_INTENT.toString()).build();
        IntentRequest intentRequest = IntentRequest.builder().withIntent(intent)
                .withRequestId(REQUEST_ID)
                .build();

        int numberOfArticlesSaved = 5;
        int numberOfArticlesReviewed = 15;
        Session session = Session.builder().withSessionId(SESSION_ID).build();
        session.setAttribute(SessionAttribute.SAVED_ARTICLES, numberOfArticlesSaved);
        session.setAttribute(SessionAttribute.REVIEWED_ARTICLES, numberOfArticlesReviewed);

        // Act
        SpeechletResponse response = speechlet.onIntent(intentRequest, session);

        // Assert
        assertTrue(response.getOutputSpeech() instanceof PlainTextOutputSpeech);
        assertEquals(Prompts.END_SESSION_PROMPT, ((PlainTextOutputSpeech)response.getOutputSpeech()).getText());

        assertTrue(response.getCard() instanceof SimpleCard);
        assertEquals(CardMessages.SUMMARY_TITLE, response.getCard().getTitle());
        assertEquals(CardMessages.buildSummaryMessage(numberOfArticlesSaved, numberOfArticlesReviewed),
                ((SimpleCard)response.getCard()).getContent());
    }

    /*
    Test that we use zeroes if the number of articles saved and reviewed is missing.
     */
    @Test
    public void testOnIntent_CancelIntent_MissingSessionAttributes() throws Exception
    {
        // Arrange
        Intent intent = Intent.builder().withName(IntentName.CANCEL_INTENT.toString()).build();
        IntentRequest intentRequest = IntentRequest.builder().withIntent(intent)
                .withRequestId(REQUEST_ID)
                .build();

        Session session = Session.builder().withSessionId(SESSION_ID).build();

        // Act
        SpeechletResponse response = speechlet.onIntent(intentRequest, session);

        // Assert
        assertTrue(response.getOutputSpeech() instanceof PlainTextOutputSpeech);
        assertEquals(Prompts.END_SESSION_PROMPT, ((PlainTextOutputSpeech)response.getOutputSpeech()).getText());

        assertTrue(response.getCard() instanceof SimpleCard);
        assertEquals(CardMessages.SUMMARY_TITLE, response.getCard().getTitle());
        assertEquals(CardMessages.buildSummaryMessage(0, 0),
                ((SimpleCard)response.getCard()).getContent());
    }

	private static final FeedlySpeechlet speechlet = new FeedlySpeechlet();

    public static final String REQUEST_ID = "requestId";
    public static final String SESSION_ID = "sessionId";
}
