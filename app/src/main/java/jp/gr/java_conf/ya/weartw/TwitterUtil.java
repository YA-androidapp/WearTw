package jp.gr.java_conf.ya.weartw; // Copyright (c) 2017 YA <ya.androidapp@gmail.com> All rights reserved. This software includes the work that is distributed in the Apache License 2.0

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtil {
    public static final String webIntentUrlLike = "https://twitter.com/intent/like?tweet_id=";

    private static Twitter twitter = null;

    TwitterUtil(){
        twitter = getTwitter();
    }

    public static final Twitter getTwitter(){
        // ToDo: Wear上でWebViewを使えるようになったらOAuthへ

        // ToDo: Twitter4jSetting.java を作成して設定値を記述
        /*
package jp.gr.java_conf.ya.weartw;

public class Twitter4jSetting {
    public static final String oAuthConsumerKey = "*************************";
    public static final String oAuthConsumerSecret = "**************************************************";
    public static final String oAuthAccessToken = "*********-****************************************";
    public static final String oAuthAccessTokenSecret = "*********************************************";

    public static final String ownerScreenName = "リスト作成者のScreenName";
    public static final String slug = "リスト名";
}
         */

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Twitter4jSetting.oAuthConsumerKey)
                .setOAuthConsumerSecret(Twitter4jSetting.oAuthConsumerSecret)
                .setOAuthAccessToken(Twitter4jSetting.oAuthAccessToken)
                .setOAuthAccessTokenSecret(Twitter4jSetting.oAuthAccessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }

    // お気に入りに登録
    public static final String like(final long id){
        if(twitter==null)
            twitter = getTwitter();

        try {
            final Status status = twitter.createFavorite(id);
            return status.getText();
        } catch (TwitterException e) {
            return "";
        }
    }

    // ツイートを投稿
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

    // リスト内のツイート一覧を取得
    public static final ResponseList<Status> getListStatuses(final String ownerScreenName, final String slug, final int page){
        if(twitter==null)
            twitter = getTwitter();

        try {
            return twitter.getUserListStatuses(ownerScreenName, slug, new Paging(page, 200));
        } catch (TwitterException e) {
            return null;
        }
    }
}
