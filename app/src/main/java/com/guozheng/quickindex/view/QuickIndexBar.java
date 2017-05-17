package com.guozheng.quickindex.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by fighting on 2017/5/16.
 */
public class QuickIndexBar extends View {

    //显示的所有字母符号
    private static final String[] LETTERS = new String[]{
            "↑", "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z", "#"};
    private int mCellWidth;//一个字母所在小格子的宽度
    private float mCellHeight;//一个字母所在小格子的高度
    private int paddingTop = 0;//顶部间距
    private int paddingBottom = 0;//底部间距
    private int letterTextSize = 15;//索引字母的字体大小

    private int mPressedBackColor = getResources().getColor(android.R.color.darker_gray);//view按下时的颜色
    private int mNormalBackColor = getResources().getColor(android.R.color.transparent);//view抬起时的颜色

    private Paint mPaint;//画笔
    private int mPreIndex = -1;//上次索引
    private int mCurIndex = -1;//当前索引
    private TextView mTextDialog;//显示在屏幕中间的textView
    private Context mContext;

    public QuickIndexBar(Context context) {
        super(context);
        init(context);
    }
    public QuickIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 对画笔进行初始化
     */
    private void init(Context context) {
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCellWidth = getMeasuredWidth();//得到每个字母所在小格子的宽度
        int mHeight = getMeasuredHeight() - paddingTop - paddingBottom;
        mCellHeight = mHeight * 1.0f / LETTERS.length;//得到每个字母所在小格子的高度
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < LETTERS.length; i++) {
            String text = LETTERS[i];
            Rect bounds = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), bounds);//得到文字的宽高，并放入bounds对象中
            int x = (int) (mCellWidth / 2.0f - bounds.width() / 2.0f);
            int y = (int) (mCellHeight / 2.0f + bounds.height() / 2.0f + i * mCellHeight + paddingTop);
            if(i == mCurIndex){
                mPaint.setColor(Color.GRAY);//当选中时字母的字体颜色设为灰色
                mPaint.setTextSize(pxFromSp(mContext, letterTextSize + 5 ));//当选中时字母的字体大小加5
            }else {
                mPaint.setColor(Color.BLACK);
                mPaint.setTextSize(pxFromSp(mContext, letterTextSize));
            }
            canvas.drawText(text, x, y, mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                setBackgroundColor(mPressedBackColor);//设置按下的背景颜色

                mCurIndex = (int) ((event.getY() - paddingTop) / mCellHeight);
                if (mCurIndex >= 0 && mCurIndex < LETTERS.length && mCurIndex != mPreIndex) {
                    if (mLetterChangedListener != null) {
                        mLetterChangedListener.onLetterChanged(LETTERS[mCurIndex]);//当字母改变时调用
                        mPreIndex = mCurIndex;
                    }
                    showTextDialog(LETTERS[mCurIndex]);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setBackgroundColor(mNormalBackColor);
                hideTextDialog();
                mCurIndex = -1;
                mPreIndex = -1;
                if (mLetterChangedListener != null) {
                    mLetterChangedListener.onLetterGone();//当手指抬起时调用
                }
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }


    /**设置字母的字体大小*/
    public void setLetterTextSize(int size){
        letterTextSize = size;
    }

    /**设置按下时的背景颜色*/
    public void setPressedBackColor(int color){
        mPressedBackColor = color;
    }
    /**设置正常时的背景颜色*/
    public void setNormalBackColor(int color){
        mNormalBackColor = color;
    }

    /**设置顶部的padding**/
    public void setPaddingTop(int padding){
        paddingTop = padding;
    }
    /**设置底部的padding*/
    public void setPaddingBottom(int padding){
        paddingBottom = padding;
    }

    /**
     * 设置TextView，当选中某个字母时如果在屏幕中间需要放大字母进行显示时需要设置一个TextView对象。
     */
    public void setTextView(TextView textView){
        mTextDialog = textView;
    }


    private void showTextDialog(String text){
        if (mTextDialog != null) {
            mTextDialog.setText(text);
            mTextDialog.setVisibility(View.VISIBLE);
        }
    }
    private void hideTextDialog(){
        if (mTextDialog != null) {
            mTextDialog.setVisibility(View.GONE);
        }
    }

    /**
     * 字母改变监听器
     */
    public interface OnLetterChangedListener {
        /**
         * 当字母改变后调用，参数letter为改变后的字母
         */
        void onLetterChanged(String letter);

        /**
         * 当手指抬起时调用
         */
        void onLetterGone();
    }
    private OnLetterChangedListener mLetterChangedListener;
    public void setOnLetterChangedListener(OnLetterChangedListener listener) {
        mLetterChangedListener = listener;
    }

    /**
     * 将字体大小的单位转换为sp
     */
    public static float pxFromSp(Context context, float spSize) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spSize, context.getResources().getDisplayMetrics());
    }
}
