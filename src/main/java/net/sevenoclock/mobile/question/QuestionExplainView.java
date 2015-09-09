package net.sevenoclock.mobile.question;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.androidquery.AQuery;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

public class QuestionExplainView extends Fragment {

    private AQuery aq;
    private ImageView iv_question_explain_img;

    public QuestionExplainView(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_question_explain, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        iv_question_explain_img = (ImageView)view.findViewById(R.id.iv_question_explain_img);
        aq = new AQuery(getActivity(), view);

        aq.id(iv_question_explain_img).image(Functions.DOMAIN + getArguments().getString("url"));

        return view;
    }

    public static QuestionExplainView newInstance(String url) {
        QuestionExplainView view = new QuestionExplainView();
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
