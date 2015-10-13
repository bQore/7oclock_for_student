package net.sevenoclock.mobile.inventory;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.customobj.FontTextView;
import net.sevenoclock.mobile.customobj.TryCatchJO;

public class InventoryBookView extends LinearLayout {
    private Context con;

    TryCatchJO tcjo;

    private FontTextView tv_testpaper_list_book_title;
    private FontTextView tv_testpaper_list_book_count;

    public InventoryBookView(Context context, TryCatchJO jo) {
        super(context);
        con = context;
        tcjo = jo;
        setLayout();

        tv_testpaper_list_book_title.setText(tcjo.get("title", ""));
        tv_testpaper_list_book_count.setText("총 " + tcjo.get("count", "") + "개 문제");
    }

    private void setLayout(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics = con.getResources().getDisplayMetrics();

        int book_height = (int) (90 * displayMetrics.density);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, book_height*3-80);
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.view_inventory_list_book, null), lp);

        tv_testpaper_list_book_title = (FontTextView)findViewById(R.id.tv_inventory_list_book_title);
        tv_testpaper_list_book_count = (FontTextView)findViewById(R.id.tv_testpaper_list_book_count);
    }
}