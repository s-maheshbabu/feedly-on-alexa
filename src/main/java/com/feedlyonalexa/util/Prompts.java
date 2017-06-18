package com.feedlyonalexa.util;

import com.feedlyonalexa.model.Item;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by maheshba on 6/17/17.
 */
public class Prompts
{
    public static final String END_SESSION_PROMPT = "Goodbye";
    public static final String REPROMPT = "Do you want me to add this to your saved articles? As I wait after each article, you can say things like Save, Yes, Add it et cetera. " +
            "Otherwise, say things like 'Skip', 'No', 'Do not save it' et cetera.";

    public static final String ssmlForItem(Item itemToDeliver)
    {
        String ssmlText = "<speak> ";
        ssmlText += "From " + StringEscapeUtils.escapeXml11(itemToDeliver.getOrigin().getTitle()) + ". " + StringEscapeUtils.escapeXml11(itemToDeliver.getTitle());
        ssmlText += " </speak>";

        return ssmlText;
    }

    private Prompts()
    {
    }
}
