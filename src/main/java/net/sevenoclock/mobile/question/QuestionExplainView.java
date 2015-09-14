package net.sevenoclock.mobile.question;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Functions;
import net.sevenoclock.mobile.settings.Values;

import java.io.File;

public class QuestionExplainView extends Fragment {

    private AQuery aq;
    private ImageView iv_question_explain_img;

    public QuestionExplainView(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_question_explain, container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        iv_question_explain_img = (ImageView)view.findViewById(R.id.iv_question_explain_img);
        aq = new AQuery(getActivity(), view);

        if(MainActivity.app_width>=800){
            try{
                aq.ajax(Functions.DOMAIN + getArguments().getString("url"), File.class, new AjaxCallback<File>() {
                    public void callback(String url, File file, AjaxStatus status) {
                        if (file != null) {
                            BitmapFactory.Options opts = new BitmapFactory.Options();
                            opts.inScaled = true;
                            opts.inDensity = DisplayMetrics.DENSITY_HIGH;
                            opts.inTargetDensity = container.getContext().getResources().getDisplayMetrics().densityDpi;
                            Bitmap buttonImages = BitmapFactory.decodeFile(file.getPath(), opts);
                            aq.id(iv_question_explain_img).image(buttonImages);
                        } else {
                            Toast.makeText(container.getContext(), "이미지 로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }catch (Exception e){
                Toast.makeText(container.getContext(), "이미지 로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        } else if(MainActivity.app_width<800){
            aq.id(iv_question_explain_img).image(Functions.DOMAIN + getArguments().getString("url"));
        }

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
