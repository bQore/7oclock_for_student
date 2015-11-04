package net.sevenoclock.mobile.quiz;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

import java.io.File;
import java.util.List;

public class QuizFinalFragment extends Fragment {

    private Values values;

    private ListView lv_quiz_final_list;

    public QuizFinalListAdapter qfla;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        values = (Values)container.getContext().getApplicationContext();

        View view = inflater.inflate(R.layout.fragment_quiz_final, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        lv_quiz_final_list = (ListView) view.findViewById(R.id.lv_quiz_final_list);

        qfla = new QuizFinalListAdapter();
        lv_quiz_final_list.setAdapter(qfla);

        return view;
    }

    public static QuizFinalFragment newInstance() {
        QuizFinalFragment view = new QuizFinalFragment();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
