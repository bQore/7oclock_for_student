package net.sevenoclock.mobile.qna;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class QnAQuestionWriteFragment extends Fragment {
    private Context con;

    private ImageView iv_qna_question_write_icon;
    private FontTextView tv_qna_question_write_username;
    private EditText et_qna_question_write_contents;
    private Button btn_qna_question_write;
    private FontTextView ftv_qna_set_unit;

    private LinearLayout ll_qna_question_write_sources;
    private LinearLayout ll_qna_question_write_source_add;

    private Values values;

    private ArrayList<Bitmap> src_array;
    private ArrayList<UnitInfo> list;

    // id는 테스트인 경우 9, 실제로는 0을 줘야됨.
    private int id = 9;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();
        values.aq = new AQuery(con);

        src_array = new ArrayList<Bitmap>();
        list = new ArrayList<UnitInfo>();

        values.aq.ajax(Functions.DOMAIN+"/mobile/?mode=get_unit_info_all",null, JSONArray.class, new AjaxCallback<JSONArray>(){
            @Override
            public void callback(String url, JSONArray ja, AjaxStatus status){
                try{
                    TryCatchJO tcjo = new TryCatchJO(ja.getJSONObject(0));
                    JSONArray grade = tcjo.getJSONArray("grade");
                    JSONArray unit = tcjo.getJSONArray("unit2_info");
                    for(int i=0;i<unit.length();i++){
                        int id=unit.getJSONObject(i).getInt("id");
                        String grade_name = grade.getJSONObject(2).getString("title");
                        String title = unit.getJSONObject(i).getString("title");
                        UnitInfo aa = new UnitInfo(id,grade_name, title);
                        Log.i("info",aa.toString());
                        list.add(aa);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        View view = inflater.inflate(R.layout.fragment_qna_question_write, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        iv_qna_question_write_icon = (ImageView) view.findViewById(R.id.iv_qna_question_write_icon);
        tv_qna_question_write_username = (FontTextView) view.findViewById(R.id.tv_qna_question_write_username);
        et_qna_question_write_contents = (EditText) view.findViewById(R.id.et_qna_question_write_contents);
        btn_qna_question_write = (Button) view.findViewById(R.id.btn_qna_question_write_done);
        ftv_qna_set_unit = (FontTextView) view.findViewById(R.id.ftv_qna_set_unit);

        ll_qna_question_write_sources = (LinearLayout) view.findViewById(R.id.ll_qna_question_write_sources);
        ll_qna_question_write_source_add = (LinearLayout) view.findViewById(R.id.ll_qna_question_write_source_add);

        ftv_qna_set_unit.setText("중학교 3학년 > 중 3 수학");
        // 아래의 주석을 풀면 단원 선택 팝업이 뜸. 위의 setText 는 지우고 쓸것.
//        ftv_qna_set_unit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i("info",list.size()+"...");
//                final String[] arr = new String[list.size()];
//                for(int i=0;i<arr.length;i++){
//                    Log.i("info", list.get(i).toString());
//                    arr[i] = list.get(i).toString();
//                }
//                AlertDialog.Builder dialog = new AlertDialog.Builder(con);
//                dialog.setItems(arr, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        id = list.get(which).id;
//                        ftv_qna_set_unit.setText(arr[which]);
//                    }
//                });
//                dialog.show();
//            }
//        });
        btn_qna_question_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibe = (Vibrator) con.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(30);

                if(id == 0){
                    Toast.makeText(con,"단원을 선택해주세요!", Toast.LENGTH_SHORT).show();
                }
                else {
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("user_id", values.user_id);
                    params.put("contents", et_qna_question_write_contents.getText().toString());
                    params.put("unit_id", id);
                    for (int i = 0; i < src_array.size(); i++)
                        params.put("image", Functions.bitmapToByteArray(src_array.get(i)));

                    Log.i("@@info", "" + params);

                    values.aq.ajax(Functions.DOMAIN + "/mobile/?mode=set_qna_question", params, String.class, new AjaxCallback<String>() {
                        @Override
                        public void callback(String url, String object, AjaxStatus status) {
                            if (status.getCode() == 200) {
                                Log.i("@@", object);
                                Functions.history_back(con);
                            } else if (status.getCode() == 400) {
                                Log.i("@@", object);
                                Toast.makeText(con, "오류가 발생하였습니다!", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(con, "Code :" + status.getCode() + "obj : " + object, Toast.LENGTH_SHORT).show();
                            Log.i("@@info","url:"+url+" status:"+status.getCode()+" obj:"+object);
                        }
                    });
                }
            }
        });
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

    public static QnAQuestionWriteFragment newInstance(){
        QnAQuestionWriteFragment view = new QnAQuestionWriteFragment();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            ImageView src = new ImageView(con);
            src_array.add(values.capture_bitmap);
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

    protected class UnitInfo{
        public int id;
        public String title;
        public String grade;

        public UnitInfo(int id, String title){
            this.id = id;
            this.title = title;
        }
        public UnitInfo(int id, String grade, String title){
            this.id = id;
            this.grade = grade;
            this.title = title;
        }

        public String toString(){
            return grade+" > "+title;
        }
    }
}
