package net.sevenoclock.mobile.testpaper;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;

public class TestpaperBookView extends LinearLayout {
    private Context con;

    private FontTextView tv_testpaper_list_book_title;
    private FontTextView tv_testpaper_list_book_date;

    public TestpaperBookView(Context context,TryCatchJO jo) {
        super(context);
        con = context;
        setLayout();

        tv_testpaper_list_book_title.setText(jo.get("title", ""));
        tv_testpaper_list_book_date.setText(jo.get("date", ""));
    }

    private void setLayout(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics = con.getResources().getDisplayMetrics();

        int book_height = (int) (90 * displayMetrics.density);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, book_height*3-80);
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.view_testpaper_list_book, null), lp);

        tv_testpaper_list_book_title = (FontTextView)findViewById(R.id.tv_testpaper_list_book_title);
        tv_testpaper_list_book_date = (FontTextView)findViewById(R.id.tv_testpaper_list_book_date);
    }
}