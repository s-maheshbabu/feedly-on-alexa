package com.feedlyonalexa.util;

/**
 * Created by maheshba on 6/17/17.
 */
public class CardMessages
{
    public static final String SUMMARY_TITLE = "Feedly review summary";

    public static final String buildSummaryMessage(int numberOfArticlesSaved, int numberOfArticlesReviewed)
    {
        return "You reviewed " + numberOfArticlesReviewed + " articles and saved " + numberOfArticlesSaved;
    }
}
