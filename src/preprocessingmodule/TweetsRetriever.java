package preprocessingmodule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * 
 * @author  Lefteris Paraskevas
 * @version 2015.09.25_1415_wave1
 */
public class TweetsRetriever {

    /**
     * Method to get authorization from Twitter API
     * @return A ConfigurationBuilder object
     */
    public ConfigurationBuilder getAuthorization() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        
        Config conf;
        try {
            conf = new Config();
        } catch(IOException e) {
            return null;
        }
        
        cb.setOAuthConsumerKey(conf.getConsumerKey());
        cb.setOAuthConsumerSecret(conf.getConsumerSecret());
        cb.setOAuthAccessToken(conf.getAccessToken());
        cb.setOAuthAccessTokenSecret(conf.getAccessTokenSecret());
        
        return cb;
    }
    
    /**
     * @deprecated
     * Simple method to retrieve tweets by querying the API
     * @throws IOException 
     */
    void retrieveTweetsWithQuery() throws IOException {

        ConfigurationBuilder cb = getAuthorization();
        
        if(cb != null) {
            Twitter twitter = new TwitterFactory(cb.build()).getInstance();
            Query query = new Query("#peace");
            int numberOfTweets = 512;
            long lastID = Long.MAX_VALUE;
            ArrayList<Status> tweets = new ArrayList<>();
            while (tweets.size () < numberOfTweets) {
                if (numberOfTweets - tweets.size() > 100) {
                    query.setCount(100);
                } else { 
                    query.setCount(numberOfTweets - tweets.size());
                }
                try {
                    QueryResult result = twitter.search(query);
                    tweets.addAll(result.getTweets());
                    System.out.println("Gathered " + tweets.size() + " tweets");
                    for (Status t: tweets) 
                        if(t.getId() < lastID) {
                            lastID = t.getId();
                        }
                } catch (TwitterException te) {
                    System.out.println("Couldn't connect: " + te);
                } 
                query.setMaxId(lastID-1);
            }

            for (int i = 0; i < tweets.size(); i++) {
                Status t = (Status) tweets.get(i);

                GeoLocation loc = t.getGeoLocation();

                String user = t.getUser().getScreenName();
                String msg = t.getText();
                if (loc!=null) {
                    Double lat = t.getGeoLocation().getLatitude();
                    Double lon = t.getGeoLocation().getLongitude();
                    System.out.println(i + " USER: " + user + " wrote: " + msg + " located at " + lat + ", " + lon);
                } else 
                    System.out.println(i + " USER: " + user + " wrote: " + msg);
            }
        }
    }
    
    /**
     * Method that handles the Twitter streaming API
     * @param terminationTime Time in milliseconds for which the thread will continue to be executed
     * @param startTime Time in milliseconds that the thread started executing
     * @return A list containing the retrieved tweets, along with their other data (user, location etc)
     * @throws InterruptedException 
     */
    public List<Status> retrieveTweetsWithStreamingAPI(long terminationTime, long startTime) throws InterruptedException {
        
        List<Status> statuses = new ArrayList<>();
        
        final Object lock = new Object();
        ConfigurationBuilder cb = getAuthorization();  
        
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener listener = new StatusListener() {
        
            @Override
            public void onStatus(Status status) {
                statuses.add(status);
                if(startTime + terminationTime <= System.currentTimeMillis()) {
                    synchronized (lock) {
                        lock.notify();
                    }
                    System.out.println("\nStreaming exceeded time boundary (" + terminationTime + "), terminating thread...\n");
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }
            
            @Override
            public void onException(Exception ex) {
                ex.printStackTrace(System.out);
            }
        };
        
        FilterQuery fq = new FilterQuery();
        String keywords[] = {"Germany"};

        
        fq.track(keywords);
        
        twitterStream.addListener(listener);
        twitterStream.filter(fq);
        
        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            twitterStream.shutdown();
        }
        return statuses;
    }
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        
        long startTime = System.currentTimeMillis();
        List<Status> statuses = new TweetsRetriever().retrieveTweetsWithStreamingAPI(100, startTime);
        
        statuses.stream().forEach((status) -> {
            System.out.println( "@" + status.getUser().getName() + " : " + status.getText() +
                    " Date : " + status.getCreatedAt() + " Location : " + status.getGeoLocation());
        });
    }
    
}
