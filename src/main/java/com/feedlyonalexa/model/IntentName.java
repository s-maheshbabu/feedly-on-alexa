package com.feedlyonalexa.model;

/**
 * Created by maheshba on 6/17/17.
 */
public enum IntentName
{
    CANCEL_INTENT("AMAZON.CancelIntent"),
    STOP_INTENT("AMAZON.StopIntent"),
    YES_INTENT("AMAZON.YesIntent"),
    NO_INTENT("AMAZON.NoIntent"),
    FEEDLY_INTENT("FeedlyIntent"),
    REPEAT_INTENT("RepeatIntent"),
    SAVE_INTENT("SaveIntent"),
    SKIP_INTENT("SkipIntent");

    private final String intentName;

    private IntentName(String intentName)
    {
        this.intentName = intentName;
    }

    @Override
    public String toString()
    {
        return intentName;
    }
}
