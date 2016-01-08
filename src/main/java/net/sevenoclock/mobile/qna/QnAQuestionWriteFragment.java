package net.sevenoclock.mobile.qna;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.androidquery.AQuery;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

public class QnAQuestionWriteFragment extends Fragment {
    private Context con;

    private ImageView iv_qna_question_write_icon;
    private FontTextView tv_qna_question_write_username;
    private EditText et_qna_question_write_contents;

    private LinearLayout ll_qna_question_write_sources;
    private LinearLayout ll_qna_question_write_source_add;

    private Values values;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();
        values.aq = new AQuery(con);
        View view = inflater.inflate(R.layout.fragment_qna_question_write, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        iv_qna_question_write_icon = (ImageView) view.findViewById(R.id.iv_qna_question_write_icon);
        tv_qna_question_write_username = (FontTextView) view.findViewById(R.id.tv_qna_question_write_username);
        et_qna_question_write_contents = (EditText) view.findViewById(R.id.et_qna_question_write_contents);

        ll_qna_question_write_sources = (LinearLayout) view.findViewById(R.id.ll_qna_question_write_sources);
        ll_qna_question_write_source_add = (LinearLayout) view.findViewById(R.id.ll_qna_question_write_source_add);

        ll_qna_question_write_source_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator)con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);

                Intent intent = new Intent(con, CameraEditor.class);
                startActivityForResult(intent, 0);
            }
        });

        values.aq.id(iv_qna_question_write_icon).image(Functions.borderRadius(Functions.DOMAIN + values.user_info.get("src", ""), 1000));
        tv_qna_question_write_username.setText(values.user_info.get("first_name","-"));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            ImageView src = new ImageView(con);
            src.setImageBitmap(values.capture_bitmap);
            src.setLayoutParams(new LinearLayout.LayoutParams(ll_qna_question_write_source_add.getWidth(), ll_qna_question_write_source_add.getHeight()));
            src.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ll_qna_question_write_sources.addView(src,0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
