package net.sevenoclock.mobile.question;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.androidquery.AQuery;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

public class QuestionDetailView extends Fragment {

    private Context con;
    private String url;
    private ImageView iv_question_detail_img;

    public QuestionDetailView(Context con, String url){
        super();
        this.con = con;
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_question_detail, container, false);
        iv_question_detail_img = (ImageView)view.findViewById(R.id.iv_question_detail_img);

        AQuery aq = new AQuery(con);
        aq.id(iv_question_detail_img).image(Functions.DOMAIN + url);

        return view;
    }
}
