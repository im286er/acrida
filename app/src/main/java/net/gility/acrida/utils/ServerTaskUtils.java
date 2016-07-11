package net.gility.acrida.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.android.ServerTaskService;
import net.gility.acrida.content.Tweet;

public class ServerTaskUtils {

    public static void pubTweet(Context context, Tweet tweet) {
        Intent intent = new Intent(ServerTaskService.ACTION_PUB_TWEET);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ServerTaskService.BUNDLE_PUB_TWEET_TASK, tweet);
        intent.putExtras(bundle);
        intent.setPackage(ApplicationLoader.getInstance().getPackageName());
        context.startService(intent);
    }

    public static void pubSoftWareTweet(Context context, Tweet tweet, int softid) {
        Intent intent = new Intent(ServerTaskService.ACTION_PUB_SOFTWARE_TWEET);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ServerTaskService.BUNDLE_PUB_SOFTWARE_TWEET_TASK,
                tweet);
        bundle.putInt(ServerTaskService.KEY_SOFTID, softid);
        intent.putExtras(bundle);
        intent.setPackage(ApplicationLoader.getInstance().getPackageName());
        context.startService(intent);
    }
}
