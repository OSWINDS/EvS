/*
 * Copyright (C) 2016 Lefteris Paraskevas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.left8.evs.edmodule.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.left8.evs.edmodule.utils.StringDateUtils;
import com.left8.evs.edmodule.utils.StopWordsHandlers;
import com.left8.evs.utilities.dsretriever.Tweet;
import com.left8.evs.utilities.Config;

/**
 *
 * @author  Lefteris Paraskevas
 * @version 2016.04.30_1826
 */
public class PeakFindingCorpus {
    
    private final StopWordsHandlers swH;
    private List<Tweet> tweets;
    private final Config config;
    private final HashMap<String, Integer> messageDistribution = new HashMap<>();
    private final HashMap<String, ArrayList<Tweet>> tweetsByWindow = new HashMap<>();
    private Date earliestDate;
    private Date latestDate;
    
    /**
     * Public constructor.
     * @param config A Config object.
     * @param tweets A List containing all relevant tweets for the analysis.
     * @param swH A StopWordsHandlers object.
     */
    public PeakFindingCorpus(Config config, List<Tweet> tweets, StopWordsHandlers swH) {
        this.config = config;
        this.swH = swH;
        this.tweets = tweets;
    }
    
    /**
     * Method to create and return the windows needed for OfflinePeakFinding 
     * algorithm to operate.
     * More formally, it creates HashMap of Strings and Integers. Every key is
     * a specific time window and its value is the corresponding summary of tweets
     * in this time interval (window). Note that only non-zero windows are created.
     * Due to the fact that a HashMap does not store its values ordered, additional
     * configuration is required to generate all the time intervals between the
     * earliest and the latest date of corpus if needed (assuming that the corpus
     * has some extend of sparseness).
     * @param window An integer indicating the time interval in which the tweets
     * should be counted. All values in minutes. <br>
     * E.g. For 1 minute interval, 1. <br>
     * For half an hour interval, 30. <br>
     * For 5 hours interval, 300.
     * @return A HashMap containing the bins.
     * @see com.left8.evs.edmodule.peakfinding.BinsCreator BinsCreator class.
     * @see com.left8.evs.edmodule.peakfinding.OfflinePeakFinding OfflinePeakFinding class.
     */
    public final HashMap<String, Integer> createCorpus(int window) {   
        //Initialize variables
        earliestDate = tweets.get(0).getDate();
        latestDate = tweets.get(0).getDate();
        Calendar cal = Calendar.getInstance();
        
        tweets.stream().forEach((tweet) -> {
            Date tweetDate = tweet.getDate();
            if(tweetDate.before(earliestDate)) {
                earliestDate = tweetDate;
            }
            if(tweetDate.after(latestDate)) {
                latestDate = tweetDate;
            }

            //Assemble the date key
            String key = StringDateUtils.getDateKey(cal, tweetDate, window);

            if(messageDistribution.containsKey(key)) {
                messageDistribution.put(key, messageDistribution.get(key) + 1);
                ArrayList<Tweet> tweetsInWindow = new ArrayList<>(tweetsByWindow.get(key));
                tweetsInWindow.add(tweet);
                tweetsByWindow.put(key, tweetsInWindow);
            } else {
                messageDistribution.put(key, 1);
                ArrayList<Tweet> tweetsInWindow = new ArrayList<>();
                tweetsInWindow.add(tweet);
                tweetsByWindow.put(key, tweetsInWindow);
            }
        });
        return messageDistribution;    
    }
    
    /**
     * Returns the earliest date a tweet was published in the corpus.
     * @return A Date object.
     */
    public final Date getEarliestDateOfCorpus() { return earliestDate; }
    
    /**
     * Returns the latest date a tweet was published in the corpus.
     * @return A Date object.
     */
    public final Date getLatestDateOfCorpus() { return latestDate; }
    
    /**
     * Returns all tweets grouped by refreshed window.
     * @return A HashMap which key is the refresh window and its value is the 
     * tweets in this window.
     */
    public final HashMap<String, ArrayList<Tweet>> getTweetsByWindow() { return tweetsByWindow; }
    
    /**
     * Return the configuration object, already stored in the Constructor.
     * @return A Configuration object.
     */
    public final Config getConfigHandler() { return config; }
    
    /**
     * Return the StopWordsHandlers object, already stored in the Constructor.
     * @return A StopWordsHandlers object.
     */
    public final StopWordsHandlers getStopWordsHandlers() { return swH; }
}
