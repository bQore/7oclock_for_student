package net.sevenoclock.mobile.customobj;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.R.attr;

public class FullnameTabIndicator extends HorizontalScrollView implements PageIndicator {
    private static final CharSequence EMPTY_TITLE = "";
    private Runnable mTabSelector;
    private final OnClickListener mTabClickListener;
    private final IcsLinearLayout mTabLayout;
    private ViewPager mViewPager;
    private OnPageChangeListener mListener;
    private int mMaxTabWidth;
    private int mSelectedTabIndex;
    private FullnameTabIndicator.OnTabReselectedListener mTabReselectedListener;

    public FullnameTabIndicator(Context context) {
        this(context, (AttributeSet)null);
    }

    public FullnameTabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTabClickListener = new OnClickListener() {
            public void onClick(View view) {
                FullnameTabIndicator.TabView tabView = (FullnameTabIndicator.TabView)view;
                int oldSelected = FullnameTabIndicator.this.mViewPager.getCurrentItem();
                int newSelected = tabView.getIndex();
                FullnameTabIndicator.this.mViewPager.setCurrentItem(newSelected);
                if(oldSelected == newSelected && FullnameTabIndicator.this.mTabReselectedListener != null) {
                    FullnameTabIndicator.this.mTabReselectedListener.onTabReselected(newSelected);
                }

            }
        };
        this.setHorizontalScrollBarEnabled(false);
        this.mTabLayout = new IcsLinearLayout(context, attr.vpiTabPageIndicatorStyle);
        this.addView(this.mTabLayout, new LayoutParams(-2, -1));
    }

    public void setOnTabReselectedListener(FullnameTabIndicator.OnTabReselectedListener listener) {
        this.mTabReselectedListener = listener;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        boolean lockedExpanded = widthMode == 1073741824;
        this.setFillViewport(lockedExpanded);
        int childCount = this.mTabLayout.getChildCount();
        if(childCount <= 1 || widthMode != 1073741824 && widthMode != -2147483648) {
            this.mMaxTabWidth = -1;
        } else if(childCount > 2) {
            this.mMaxTabWidth = (int)((float)MeasureSpec.getSize(widthMeasureSpec) * 0.4F);
        } else {
            this.mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
        }
        this.mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec);

        int oldWidth = this.getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newWidth = this.getMeasuredWidth();
        if(lockedExpanded && oldWidth != newWidth) {
            this.setCurrentItem(this.mSelectedTabIndex);
        }

    }

    private void animateToTab(int position) {
        final View tabView = this.mTabLayout.getChildAt(position);
        if(this.mTabSelector != null) {
            this.removeCallbacks(this.mTabSelector);
        }

        this.mTabSelector = new Runnable() {
            public void run() {
                int scrollPos = tabView.getLeft() - (FullnameTabIndicator.this.getWidth() - tabView.getWidth()) / 2;
                FullnameTabIndicator.this.smoothScrollTo(scrollPos, 0);
                FullnameTabIndicator.this.mTabSelector = null;
            }
        };
        this.post(this.mTabSelector);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(this.mTabSelector != null) {
            this.post(this.mTabSelector);
        }

    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(this.mTabSelector != null) {
            this.removeCallbacks(this.mTabSelector);
        }

    }

    private void addTab(int index, CharSequence text, int iconResId) {
        FullnameTabIndicator.TabView tabView = new FullnameTabIndicator.TabView(this.getContext());
        tabView.mIndex = index;
        tabView.setFocusable(true);
        tabView.setOnClickListener(this.mTabClickListener);
        tabView.setText(text);
        if(iconResId != 0) {
            tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
        }

        this.mTabLayout.addView(tabView, new android.widget.LinearLayout.LayoutParams(0, -1, 1.0F));
    }

    public void onPageScrollStateChanged(int arg0) {
        if(this.mListener != null) {
            this.mListener.onPageScrollStateChanged(arg0);
        }

    }

    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if(this.mListener != null) {
            this.mListener.onPageScrolled(arg0, arg1, arg2);
        }

    }

    public void onPageSelected(int arg0) {
        this.setCurrentItem(arg0);
        if(this.mListener != null) {
            this.mListener.onPageSelected(arg0);
        }

    }

    public void setViewPager(ViewPager view) {
        if(this.mViewPager != view) {
            if(this.mViewPager != null) {
                this.mViewPager.setOnPageChangeListener((OnPageChangeListener)null);
            }

            PagerAdapter adapter = view.getAdapter();
            if(adapter == null) {
                throw new IllegalStateException("ViewPager does not have adapter instance.");
            } else {
                this.mViewPager = view;
                view.setOnPageChangeListener(this);
                this.notifyDataSetChanged();
            }
        }
    }

    public void notifyDataSetChanged() {
        this.mTabLayout.removeAllViews();
        PagerAdapter adapter = this.mViewPager.getAdapter();
        IconPagerAdapter iconAdapter = null;
        if(adapter instanceof IconPagerAdapter) {
            iconAdapter = (IconPagerAdapter)adapter;
        }

        int count = adapter.getCount();

        for(int i = 0; i < count; ++i) {
            CharSequence title = adapter.getPageTitle(i);
            if(title == null) {
                title = EMPTY_TITLE;
            }

            int iconResId = 0;
            if(iconAdapter != null) {
                iconResId = iconAdapter.getIconResId(i);
            }

            this.addTab(i, title, iconResId);
        }

        if(this.mSelectedTabIndex > count) {
            this.mSelectedTabIndex = count - 1;
        }

        this.setCurrentItem(this.mSelectedTabIndex);
        this.requestLayout();
    }

    public void setViewPager(ViewPager view, int initialPosition) {
        this.setViewPager(view);
        this.setCurrentItem(initialPosition);
    }

    public void setCurrentItem(int item) {
        if(this.mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        } else {
            this.mSelectedTabIndex = item;
            this.mViewPager.setCurrentItem(item);
            int tabCount = this.mTabLayout.getChildCount();

            for(int i = 0; i < tabCount; ++i) {
                View child = this.mTabLayout.getChildAt(i);
                boolean isSelected = i == item;
                child.setSelected(isSelected);
                if(isSelected) {
                    this.animateToTab(item);
                }
            }

        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mListener = listener;
    }

    private class TabView extends TextView {
        private int mIndex;

        public TabView(Context context) {
            super(context, (AttributeSet)null, attr.vpiTabPageIndicatorStyle);
        }

        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if(FullnameTabIndicator.this.mMaxTabWidth > 0 && this.getMeasuredWidth() > FullnameTabIndicator.this.mMaxTabWidth) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(FullnameTabIndicator.this.mMaxTabWidth, 1073741824), heightMeasureSpec);
            }

        }

        public int getIndex() {
            return this.mIndex;
        }
    }

    public interface OnTabReselectedListener {
        void onTabReselected(int var1);
    }
}
