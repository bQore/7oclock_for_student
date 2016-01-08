package net.sevenoclock.mobile.qna;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.home.LoadingActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QnADetailWriteFragment extends Fragment {
    private Context con;

    private TryCatchJO tcjo;

    private ImageView iv_qna_detail_write_icon;
    private FontTextView tv_qna_detail_write_username;
    private EditText et_qna_detail_write_contents;
    private Button btn_qna_detail_write_done;

    private LinearLayout ll_qna_detail_write_sources;
    private LinearLayout ll_qna_detail_write_source_add;

    private ArrayList<Bitmap> src_bitmaps = new ArrayList<Bitmap>();

    private Values values;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();
        values.aq = new AQuery(con);

        try {
            tcjo = new TryCatchJO(new JSONObject(getArguments().getString("tcjo")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View view = inflater.inflate(R.layout.fragment_qna_detail_write, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        iv_qna_detail_write_icon = (ImageView) view.findViewById(R.id.iv_qna_detail_write_icon);
        tv_qna_detail_write_username = (FontTextView) view.findViewById(R.id.tv_qna_detail_write_username);
        et_qna_detail_write_contents = (EditText) view.findViewById(R.id.et_qna_detail_write_contents);
        btn_qna_detail_write_done = (Button) view.findViewById(R.id.btn_qna_detail_write_done);

        ll_qna_detail_write_sources = (LinearLayout) view.findViewById(R.id.ll_qna_detail_write_sources);
        ll_qna_detail_write_source_add = (LinearLayout) view.findViewById(R.id.ll_qna_detail_write_source_add);

        values.aq.id(iv_qna_detail_write_icon).image(Functions.borderRadius(Functions.DOMAIN + values.user_info.get("src", ""), 1000));
        tv_qna_detail_write_username.setText(values.user_info.get("first_name","-"));

        ll_qna_detail_write_source_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator)con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);

                Intent intent = new Intent(con, CameraEditor.class);
                startActivityForResult(intent, 0);
            }
        });

        btn_qna_detail_write_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator)con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("user_id",values.user_id);
                params.put("question_id",tcjo.get("id",0));
                params.put("contents",et_qna_detail_write_contents.getText().toString());
                for(int i=0;i<src_bitmaps.size();i++)
                    params.put("image", Functions.bitmapToByteArray(src_bitmaps.get(i)));

                Log.i("@@",""+params);

                values.aq.ajax(Functions.DOMAIN+"/mobile/?mode=set_qna_answer", params, String.class, new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String object, AjaxStatus status) {
                        if (status.getCode() == 200){
                            Log.i("@@",object);
                            Functions.history_back(con);
                        }
                    }
                });
            }
        });

        return view;
    }

    public static QnADetailWriteFragment newInstance(TryCatchJO tcjo) {
        QnADetailWriteFragment view = new QnADetailWriteFragment();
        Bundle args = new Bundle();
        args.putString("tcjo",tcjo.toString());
        view.setArguments(args);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            ImageView src = new ImageView(con);
            src_bitmaps.add(values.capture_bitmap);
            src.setImageBitmap(values.capture_bitmap);
            src.setLayoutParams(new LinearLayout.LayoutParams(ll_qna_detail_write_source_add.getWidth(), ll_qna_detail_write_source_add.getHeight()));
            src.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ll_qna_detail_write_sources.addView(src,0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
