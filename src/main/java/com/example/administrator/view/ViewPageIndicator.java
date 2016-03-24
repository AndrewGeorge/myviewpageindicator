package com.example.administrator.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.myviewpageindicator.R;

import org.w3c.dom.ls.LSInput;

import java.net.Proxy;
import java.util.List;

/**
 * Created by Administrator on 2016/3/20.
 */
public class ViewPageIndicator extends LinearLayout {

    private Paint mPaint;
    private Path mPath;
    private int mTriangleWith;
    private int mTriangleHeigh;
    private int mInitTranslationX;
    private int mTranslationX = 0;
    private int mTabVisableCount;
    private List<String> mTitles;
    private static final int COUNT_DEFAULT = 4;
    private static final int COLOR_TEXT_NORMAL = 0x77FFFFFF;
    private static final int COLOR_TEXT_HIGHLIGHT = 0xFFFFFFFF;
    private static final float RADIO_TRIANGLE_WIDTH = 1 / 6F;
    /**
     * s三角形的最大宽度
     */
    private final int DEMENSION_TRIAGE_WIDTH_MAX= (int) (getScreenwith()/3*RADIO_TRIANGLE_WIDTH);

    public ViewPageIndicator(Context context) {
        this(context, null);

    }

    public ViewPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取课件tab的数量
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPageIndicator);
        mTabVisableCount = a.getColor(R.styleable.ViewPageIndicator_visable_tab_count, COUNT_DEFAULT);

        if (mTabVisableCount < 0) {
            mTabVisableCount = COUNT_DEFAULT;
        }
        a.recycle();
        //初始化画笔
        mPaint = new Paint();
       // mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#eeffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTranslationX + mTranslationX, getHeight());
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWith = (int) (w / mTabVisableCount * RADIO_TRIANGLE_WIDTH);

        mTriangleWith=Math.min(mTriangleWith,DEMENSION_TRIAGE_WIDTH_MAX);
        mInitTranslationX = w / mTabVisableCount / 2 - mTriangleWith / 2;
        initTriangle();
    }

    /**
     * 初始化三角形
     */
    private void initTriangle() {
        mTriangleHeigh = mTriangleWith / 2;
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWith, 0);
       mPath.lineTo(mTriangleWith / 2, -mTriangleHeigh);
        mPath.close();
    }

    /**
     * 指示器跟随手指移动
     *
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        int tabWith = getWidth() / mTabVisableCount;
        mTranslationX = (int) (tabWith * (offset + position));

        //容器移动，当tab处于移动至最后一个时
//        if (position >= (mTabVisableCount - 2) && offset > 0 && getChildCount() > mTabVisableCount) {
//            if (mTabVisableCount != 1) {
//                this.scrollTo(((position - (mTabVisableCount - 2)) * tabWith) + (int) (tabWith * offset), 0);
//            } else {
//                this.scrollTo((int) (tabWith * (position + offset)), 0);
//            }
//
//        }
        if (position >= mTabVisableCount - 2 && offset > 0 && getChildCount() > mTabVisableCount) {
//容器移动的距离
            if(mTabVisableCount==1&&position<getChildCount()-1){//只有一个可见的tab
                this.scrollTo((int) (position * tabWith + tabWith * offset), 0);
            }else if(mTabVisableCount==2&&position<getChildCount()-2){//只有两个可见tab
                this.scrollTo((int) (position * tabWith + tabWith * offset), 0);
            }else if (position < getChildCount() - 2) {
                this.scrollTo((int) ((position - (mTabVisableCount - 2)) * tabWith + tabWith * offset), 0);
            }
        }
        invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int cCount = getChildCount();
        if (cCount == 0) return;

        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenwith() / mTabVisableCount;
            view.setLayoutParams(lp);
        }
        setItemClickEvent();
    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    private int getScreenwith() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    /**
     * 动态设置tab
     */
    public void setTabItemTittles(List<String> titles) {
        if (titles != null && titles.size() > 0) {
            mTitles = titles;
        }
        for (String title : mTitles) {
            addView(generateTextView(title));
        }
        setItemClickEvent();
    }


    /**
     * 设置可见的tab数量，需要在
     *
     * @param count
     */
    public void setmTabVisableCount(int count) {

        mTabVisableCount = count;
    }

    /**
     * 更具title创建tab
     *
     * @param title
     * @return
     */
    private View generateTextView(String title) {

        TextView mTextView = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.width = getScreenwith() / mTabVisableCount;
        mTextView.setText(title);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mTextView.setTextColor(COLOR_TEXT_NORMAL);
        mTextView.setLayoutParams(lp);
        return mTextView;
    }

    private ViewPager mViewPager;

    public interface PageOnchangeLinstener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);
    }

    public PageOnchangeLinstener mLinstener;

    public void AddOnPageChangeLinstener(PageOnchangeLinstener linstene) {

        this.mLinstener = linstene;
    }

    /***
     * 设置过眼帘的ViewPage
     *
     * @param viewPage
     * @param pos
     */
    public void setViewPage(ViewPager viewPage, int pos) {
        mViewPager = viewPage;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);
                if (mLinstener != null) {
                    mLinstener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {

                if (mLinstener != null) {
                    mLinstener.onPageSelected(position);
                }
                higtLight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mLinstener != null) {
                    mLinstener.onPageScrollStateChanged(state);
                }
            }
        });

        mViewPager.setCurrentItem(pos);
        higtLight(pos);
    }

    /**
     * 设置tab的Title高亮
     * @param pos
     */
    private  void higtLight(int pos){
        resetTextColor();
        View  view =getChildAt(pos);
        if(view instanceof  TextView){
            ((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHT);
        }
    }

    /**
     * 重置文本颜色
     */
    private void resetTextColor(){
        for (int i = 0; i < getChildCount(); i++) {
            View  view =getChildAt(i);
            if(view instanceof  TextView){
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }

    /**
     * 设置tab的点击事件
     */
    private void setItemClickEvent(){

        int cCount=getChildCount();
        for (int i = 0; i < cCount; i++) {
          final   int j =i;
            View view=getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }
}
