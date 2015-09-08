package net.sevenoclock.mobile.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;

public class QuestionVideoView extends Fragment {

    private YouTubePlayer ytp;
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_question_video, container, false);

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.fl_question_video_fragment, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(Functions.YOUTUBE_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    url = getArguments().getString("url");
                    ytp = youTubePlayer;
                    ytp.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {

                        @Override
                        public void onFullscreen(boolean _isFullScreen) {
                            if(_isFullScreen){
                                ytp.setFullscreen(false);
                                Intent intent = new Intent(MainActivity.activity, QuestionVideoActivity.class);
                                intent.putExtra("url", url);
                                startActivityForResult(intent, 1);
                            }
                        }
                    });
                    youTubePlayer.cueVideo(url);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.i("YoutubeError", "" + youTubeInitializationResult.toString());
            }
        });

        return view;
    }

    public static QuestionVideoView newInstance(String url) {
        QuestionVideoView view = new QuestionVideoView();
        url = url.substring(32);
        Bundle args = new Bundle();
        args.putString("url", url);
        view.setArguments(args);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
