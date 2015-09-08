package net.sevenoclock.mobile.customobj;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class RefreshScrollView extends ScrollView {

    private OnScrollViewListener mOnScrollViewListener;

    public RefreshScrollView(Context context) {
        super(context);
    }

    public RefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public interface OnScrollViewListener {
        void onScrollChanged( RefreshScrollView v, int l, int t, int oldl, int oldt );
    }

    public void setOnScrollViewListener(OnScrollViewListener l) {
        this.mOnScrollViewListener = l;
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        mOnScrollViewListener.onScrollChanged( this, l, t, oldl, oldt );
        super.onScrollChanged( l, t, oldl, oldt );
    }

}