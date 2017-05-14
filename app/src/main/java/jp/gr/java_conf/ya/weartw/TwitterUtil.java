package jp.gr.java_conf.ya.weartw; // Copyright (c) 2017 YA <ya.androidapp@gmail.com> All rights reserved. This software includes the work that is distributed in the Apache License 2.0

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtil {
    private static Twitter twitter = null;

    TwitterUtil(){
        twitter = getTwitter();
    }

    public static final Twitter getTwitter(){
        // ToDo: Wear上でWebViewを使えるようになったらOAuthへ
        // ToDo: Twitter4jSetting.java
        // public static final String oAuthConsumerKey = "";
        // public static final String oAuthConsumerSecret = "";
        // public static final String oAuthAccessToken = "";
        // public static final String oAuthAccessTokenSecret = "";

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Twitter4jSetting.oAuthConsumerKey)
                .setOAuthConsumerSecret(Twitter4jSetting.oAuthConsumerSecret)
                .setOAuthAccessToken(Twitter4jSetting.oAuthAccessToken)
                .setOAuthAccessTokenSecret(Twitter4jSetting.oAuthAccessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }

    public static final String tweet(final String message){
        if(twitter==null)
            twitter = getTwitter();

        try {
            final Status status = twitter.updateStatus(message);
            return status.getText();
        } catch (TwitterException e) {
            return "";
        }
    }
}
