package net.sevenoclock.mobile.question;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;

public class QuestionVideoView extends Fragment {
    private Context con;
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        con = container.getContext();
        View view = inflater.inflate(R.layout.view_question_video, container, false);
        url = getArguments().getString("url");

        LinearLayout ll_question_video_fullscreen = (LinearLayout)view.findViewById(R.id.ll_question_video_fullscreen);
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        getChildFragmentManager().beginTransaction().replace(R.id.fl_question_video_fragment, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(Functions.YOUTUBE_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                    youTubePlayer.cueVideo(url);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.i("YoutubeError", "" + youTubeInitializationResult.toString());
            }
        });

        ll_question_video_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator)con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                //Intent intent = new Intent(MainActivity.activity, QuestionVideoActivity.class);
                //intent.putExtra("url", url);
                //startActivityForResult(intent, 1);
                Log.i("@@@@@@@@@@@@@@@@",""+url);
                startActivityForResult(new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse("https://www.youtube.com/watch?v="+url))
                        .setPackage("com.google.android.youtube"),1);
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
