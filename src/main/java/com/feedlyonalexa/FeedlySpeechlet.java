package com.feedlyonalexa;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedlyonalexa.model.Item;
import com.feedlyonalexa.model.MarkersAction;
import com.feedlyonalexa.model.MarkersRequest;
import com.feedlyonalexa.model.StreamContents;
import com.feedlyonalexa.util.ObjectMapperFactory;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

public class FeedlySpeechlet implements Speechlet {
	public static void main(String[] args) throws SpeechletException {
		FeedlySpeechlet speechlet = new FeedlySpeechlet();

		MarkersRequest request = new MarkersRequest();
		List<String> entryIds = new ArrayList<String>();
		entryIds.add("asdf");
		request.setEntryIds(entryIds);
		request.setAction(MarkersAction.markAsRead);
		try {
			System.out.println(objectMapper.writeValueAsString(request));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//speechlet.onLaunch(null, Session.builder().withSessionId("id").withIsNew(true).build());
	}

	@Override
	public SpeechletResponse onIntent(IntentRequest intentRequest, Session session) throws SpeechletException {
		if("AMAZON.NoIntent".equals(intentRequest.getIntent().getName())
				|| "SkipIntent".equals(intentRequest.getIntent().getName()))
		{
			return handleFeedlyIntent(session);
		}
		if("AMAZON.YesIntent".equals(intentRequest.getIntent().getName())
				|| "SaveIntent".equals(intentRequest.getIntent().getName()))
		{
			return handleYesIntent(intentRequest, session);
		}
		if("RepeatIntent".equals(intentRequest.getIntent().getName()))
		{
			return handleRepeatIntent(intentRequest, session);
		}
		else if("FeedlyIntent".equals(intentRequest.getIntent().getName()))
		{
			return handleFeedlyIntent(session);
		}
		else if("AMAZON.StopIntent".equals(intentRequest.getIntent().getName())
				|| "AMAZON.CancelIntent".equals(intentRequest.getIntent().getName()))
		{
			return handleStopIntent(session);
		}

		throw new SpeechletException("No handler for the intent: " + intentRequest.getIntent().getName());
	}

	private SpeechletResponse handleStopIntent(Session session) throws SpeechletException {
		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
		outputSpeech.setText("Goodbye");

		SimpleCard card = new SimpleCard();
		card.setTitle("Feedly review summary");

		int numberOfSavedArticles = session.getAttribute("saved") == null ? 0 : (int)session.getAttribute("saved");
		int numberOfReviewedArticles = session.getAttribute("reviewed") == null ? 0 : (int)session.getAttribute("reviewed");
		card.setContent("You reviewed " + numberOfReviewedArticles + " articles and saved " + numberOfSavedArticles);

		return SpeechletResponse.newTellResponse(outputSpeech, card);
	}

	private SpeechletResponse handleYesIntent(IntentRequest intentRequest, Session session) throws SpeechletException {
		String itemAsString = (String)session.getAttribute("itemBeingDelivered");
		Item item;
		try {
			item = objectMapper.readValue(itemAsString, Item.class);
		} catch (Exception e) {
			throw new SpeechletException("Unable to deserialize items: " + itemAsString, e);
		}

		MarkersRequest request = new MarkersRequest();
		List<String> entryIds = new ArrayList<String>();
		entryIds.add(item.getId());
		request.setEntryIds(entryIds);
		request.setAction(MarkersAction.markAsSaved);
		callMarkers(request);

		int numberOfSavedArticles = session.getAttribute("saved") == null ? 0 : (int)session.getAttribute("saved");
		session.setAttribute("saved", ++numberOfSavedArticles);

		return handleFeedlyIntent(session);
	}

	private SpeechletResponse handleRepeatIntent(IntentRequest intentRequest, Session session) throws SpeechletException {
		String itemAsString = (String)session.getAttribute("itemBeingDelivered");
		Item itemToDeliver;
		try {
			itemToDeliver = objectMapper.readValue(itemAsString, Item.class);
		} catch (Exception e) {
			throw new SpeechletException("Unable to deserialize items: " + itemAsString, e);
		}

		String ssmlText = "<speak> ";
		ssmlText += "From " + StringEscapeUtils.escapeXml11(itemToDeliver.getOrigin().getTitle()) + ". " + StringEscapeUtils.escapeXml11(itemToDeliver.getTitle());
		ssmlText += " </speak>";

		SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
		outputSpeech.setSsml(ssmlText);

		Reprompt reprompt = new Reprompt();
		PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
		repromptSpeech.setText("Do you want me to add this to your saved articles? As I wait after each article, you can say things like Save, Yes, Add it et cetera. " +
				"Otherwise, say things like 'Skip', 'No', 'Do not save it' et cetera.");
		reprompt.setOutputSpeech(repromptSpeech);

		return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
	}

	private void callMarkers(MarkersRequest request) throws SpeechletException
	{
		String serializedRequest;
		try {
			serializedRequest = objectMapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			throw new SpeechletException("Unable to save the article because of a serialization error", e);
		}
		System.out.println(serializedRequest);

		try {
			HttpResponse<JsonNode> asJson = Unirest.post("http://cloud.feedly.com/v3/markers")
					.header("Authorization", "OAuth Ay1PAqrASp7FqRCGfZr3k8XvK_NmwPFjwbG_Gk1BQ4mOJzjbW86o1RGPzDo0Hj55_tKBZ7Xp7eu0U-FpAZpltt46h7Ec1-VKIUzYJkRbNBuSalpjOStsgseIwmo6mCmvaj1eAIqiXOHdXfcAq7GQnzTOQxThVXIfXlI00q51li1zz_nq8B09spvIEiKieIa6uiV1zX2Nr2j7oGNRAHDxRXDaS8r0NQ:feedlydev")
					.body(serializedRequest)
					.asJson();
			System.out.println(asJson.getStatus() + asJson.getStatusText());
		} catch (UnirestException e) {
			throw new SpeechletException("Encountered an error from Feedly while trying to save an article", e);
		}
	}

	private SpeechletResponse handleFeedlyIntent(Session session) throws SpeechletException {
		String continuation = (session.getAttribute("continuation") == null) ? null : (String)session.getAttribute("continuation");
		if(!session.isNew() && StringUtils.isEmpty(continuation))
		{
			return renderEndOfFeeds(session);
		}

		if(session.isNew())
		{
			session.setAttribute("reviewed", 0);
			session.setAttribute("saved", 0);
		}

		StreamContents streamContents = getStreamContents(continuation);
		if(streamContents == null)
		{
			throw new SpeechletException("Unexpected error. StreamContent was null.");
		}
		List<Item> items = streamContents.getItems();
		if(null == items || items.size() == 0)
		{
			return renderEndOfFeeds(session);
		}

		Item itemToDeliver = items.get(0);
		session.setAttribute("continuation", streamContents.getContinuation());
		try {
			session.setAttribute("itemBeingDelivered", objectMapper.writeValueAsString(itemToDeliver));
		} catch (JsonProcessingException e) {
			throw new SpeechletException("Couldn't serialize");
		}

		MarkersRequest request = new MarkersRequest();
		List<String> entryIds = new ArrayList<String>();
		entryIds.add(itemToDeliver.getId());
		request.setEntryIds(entryIds);
		request.setAction(MarkersAction.markAsRead);
		try {
			callMarkers(request);
		} catch(Exception e)
		{
			logger.warn("Failed to mark the item delivered as read. Not a critical failure and so swallowing the exception.");
		}
		session.setAttribute("reviewed", (int)session.getAttribute("reviewed") + 1);

		String ssmlText = "<speak> ";
		ssmlText += "From " + StringEscapeUtils.escapeXml11(itemToDeliver.getOrigin().getTitle()) + ". " + StringEscapeUtils.escapeXml11(itemToDeliver.getTitle());
		ssmlText += " </speak>";

		SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
		outputSpeech.setSsml(ssmlText);

		Reprompt reprompt = new Reprompt();
		PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
		repromptSpeech.setText("Do you want me to add this to your saved articles? As I wait after each article, you can say things like Save, Yes, Add it et cetera. " +
				"Otherwise, say things like 'Skip', 'No', 'Do not save it' et cetera.");
		reprompt.setOutputSpeech(repromptSpeech);

		return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
	}

	private StreamContents getStreamContents(String continuation) throws SpeechletException
	{
		String url = "http://cloud.feedly.com/v3/streams/contents?streamId=user/acce4bfe-7fe1-4c5c-9598-24d8e910fa43/category/global.all&count=1&unreadOnly=true";
		if(!StringUtils.isEmpty(continuation))
		{
			url += "&continuation=" + continuation;
		}
		GetRequest getRequest = Unirest.get(url);
		getRequest.header("Authorization", "OAuth Ay1PAqrASp7FqRCGfZr3k8XvK_NmwPFjwbG_Gk1BQ4mOJzjbW86o1RGPzDo0Hj55_tKBZ7Xp7eu0U-FpAZpltt46h7Ec1-VKIUzYJkRbNBuSalpjOStsgseIwmo6mCmvaj1eAIqiXOHdXfcAq7GQnzTOQxThVXIfXlI00q51li1zz_nq8B09spvIEiKieIa6uiV1zX2Nr2j7oGNRAHDxRXDaS8r0NQ:feedlydev");

		Unirest.setObjectMapper(ObjectMapperFactory.streamContentsObjectMapper());
		try {
			HttpResponse<StreamContents> httpResponse = getRequest.asObject(StreamContents.class);
			return httpResponse.getBody();
		} catch (UnirestException e) {
			throw new SpeechletException("There was an error fetching feeds", e);
		}
	}

	@Override
	public SpeechletResponse onLaunch(LaunchRequest launchRequest, Session session) throws SpeechletException {
		logger.info("FeedlySpeechlet launched. Request: " + launchRequest + ". SessionId: " + session.getSessionId());

		return handleFeedlyIntent(session);
	}

	private SpeechletResponse renderEndOfFeeds(Session session) {
		int numberOfSavedArticles = session.getAttribute("saved") == null ? 0 : (int)session.getAttribute("saved");
		int numberOfReviewedArticles = session.getAttribute("reviewed") == null ? 0 : (int)session.getAttribute("reviewed");

		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
		outputSpeech.setText("There are no more unread articles. You reviewed " + numberOfReviewedArticles + " articles and saved " + numberOfSavedArticles + ". Goodbye");

		SimpleCard card = new SimpleCard();
		card.setTitle("Feedly review summary");
		card.setContent("You reviewed " + numberOfReviewedArticles + " articles and saved " + numberOfSavedArticles);

		return SpeechletResponse.newTellResponse(outputSpeech, card);
	}

	@Override
	public void onSessionEnded(SessionEndedRequest arg0, Session arg1) throws SpeechletException {
	}

	@Override
	public void onSessionStarted(SessionStartedRequest arg0, Session arg1) throws SpeechletException {
	}

	private static final ObjectMapper objectMapper = ObjectMapperFactory.getInstance();
	private static final Logger logger = LoggerFactory.getLogger(FeedlySpeechlet.class);
}
