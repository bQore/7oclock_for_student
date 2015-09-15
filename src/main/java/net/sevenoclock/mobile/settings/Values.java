package net.sevenoclock.mobile.settings;

import android.app.Application;
import android.view.View;
import com.androidquery.AQuery;
import net.sevenoclock.mobile.customobj.TryCatchJO;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-09-03.
 */
public class Values extends Application {
    public int user_id = 0;
    public TryCatchJO user_info = null;
    public ArrayList<View> view_history = new ArrayList<View>();

    public int book_height = 0;
    public AQuery aq = null;
}