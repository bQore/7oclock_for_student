package net.sevenoclock.mobile.question;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.androidquery.AQuery;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

public class QuestionExplainView extends Fragment {

    private Context con;
    private String url;
    private ImageView iv_question_explain_img;

    public QuestionExplainView(Context con, String url){
        super();
        this.con = con;
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_question_explain, container, false);
        iv_question_explain_img = (ImageView)view.findViewById(R.id.iv_question_explain_img);

        AQuery aq = new AQuery(con);
        aq.id(iv_question_explain_img).image(Functions.DOMAIN + url);

        return view;
    }

}
