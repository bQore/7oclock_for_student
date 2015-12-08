package net.sevenoclock.mobile.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.main.MainActivity;
import net.sevenoclock.mobile.settings.Values;

public class DashboardFragment extends Fragment {

    private Context con;

    Values values;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con = container.getContext();
        values = (Values) con.getApplicationContext();

        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        MainActivity.setTitle("대쉬보드");
        MainActivity.setSubtitle("\""+values.union_info.get("title","")+"\"의 대쉬보드입니다.");

        return v;
    }

    public static DashboardFragment newInstance() {
        DashboardFragment view = new DashboardFragment();
        return view;
    }

    public void reflesh(){
        //new AddBookTask().execute(null, null, null);
    }

}