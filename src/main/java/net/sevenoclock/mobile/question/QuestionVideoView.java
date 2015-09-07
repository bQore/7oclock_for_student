package net.sevenoclock.mobile.question;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

public class QuestionVideoView extends YouTubePlayerSupportFragment {

    YouTubePlayer youTubeView;

    public static QuestionVideoView newInstance(String url) {

        QuestionVideoView playerYouTubeFrag = new QuestionVideoView();

        Bundle bundle = new Bundle();
        bundle.putString("url", url);

        playerYouTubeFrag.setArguments(bundle);

        Log.i("@@@@@@@@@@@@@@@@", "" + url);

        return playerYouTubeFrag;
    }

    private void init() {

        initialize(Functions.YOUTUBE_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
            }

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                youTubeView = player;
                youTubeView.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                if (!wasRestored) {
                    youTubeView.loadVideo(getArguments().getString("url"), 0);

                }
            }
        });
    }

}
