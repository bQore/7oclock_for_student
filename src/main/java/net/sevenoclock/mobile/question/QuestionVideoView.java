package net.sevenoclock.mobile.question;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;

import java.util.List;

public class QuestionVideoView extends Fragment {
    private Context con;
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        con = container.getContext();
        View view = inflater.inflate(R.layout.view_question_video, container, false);
        url = getArguments().getString("url");

        Button btn_question_detail_error = (Button)view.findViewById(R.id.btn_question_detail_error);
        btn_question_detail_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"eaeao@naver.com","storm0812@hanmail.net","tellme0218@naver.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "[모두를위한수학]오류신고합니다!");
                email.putExtra(Intent.EXTRA_TEXT, "제목 : "+getArguments().getInt("qid")+"번 동영상 오류신고합니다!\n\n내용 : ");
                email.setType("text/plain");
                final PackageManager pm = getActivity().getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(email, 0);
                ResolveInfo best = null;
                for(final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                        best = info;
                if (best != null)
                    email.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                getActivity().startActivity(email);
            }
        });

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
                MainActivity.ll_main_main_loading.setVisibility(View.VISIBLE);
                Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(con, url, true, false);
                startActivity(intent);
                Handler handler = new Handler();
                Runnable r = new Runnable() {
                    public void run() {
                        MainActivity.ll_main_main_loading.setVisibility(View.GONE);
                    }
                };
                handler.postDelayed(r, 5000);
            }
        });

        return view;
    }

    public static QuestionVideoView newInstance(int qid, String url) {
        QuestionVideoView view = new QuestionVideoView();
        url = url.substring(32);
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putInt("qid", qid);
        view.setArguments(args);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
