package net.sevenoclock.mobile.main;

import android.content.Context;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.IconTextView;

public class ActionbarSearchView extends FrameLayout {

    private Context con;

    public IconTextView tv_main_actionbar_search_backbtn;
    public EditText et_main_actionbar_search_form;
    public FontTextView tv_main_actionbar_search_searchbtn;

    public ActionbarSearchView(Context context) {
        super(context);
        con = context;

        inflate(getContext(), R.layout.view_main_actionbar_search, this);

        tv_main_actionbar_search_backbtn = (IconTextView)findViewById(R.id.tv_main_actionbar_search_backbtn);
        tv_main_actionbar_search_backbtn.setOnClickListener((OnClickListener)context);

        et_main_actionbar_search_form = (EditText)findViewById(R.id.et_main_actionbar_search_form);

        tv_main_actionbar_search_searchbtn = (FontTextView)findViewById(R.id.tv_main_actionbar_search_searchbtn);

        et_main_actionbar_search_form.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Vibrator Vibe = (Vibrator)con.getSystemService(con.VIBRATOR_SERVICE);
                    Vibe.vibrate(30);
                    MainActivity.view_main_search.search(et_main_actionbar_search_form.getText().toString());
                    return true;
                }
                return false;
            }
        });

        tv_main_actionbar_search_searchbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator Vibe = (Vibrator)con.getSystemService(con.VIBRATOR_SERVICE);
                Vibe.vibrate(30);
                MainActivity.view_main_search.search(et_main_actionbar_search_form.getText().toString());
            }
        });
    }
}
