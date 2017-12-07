
package com.cloud.tao.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.cloud.tao.R;

/**
 * 描述:带消息提醒的radiobutton
 * 
 * @author zhj
 * @since 2015-6-8 上午10:34:35
 */
public class CustomRadioButton extends RadioButton {

    private Drawable drawableTop, drawableBottom, drawableLeft, drawableRight;
    private int mTopWith, mTopHeight, mBottomWith, mBottomHeight, mRightWith, mRightHeight, mLeftWith, mLeftHeight;

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CustomRadioButton(Context context) {
        super(context);
        initView(context, null);
    }

    private void initView(Context context, AttributeSet attrs) {
        if (attrs != null) {
            float scale = context.getResources().getDisplayMetrics().density;
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PengRadioButton);
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = a.getIndex(i);
                switch (attr) {
                    case R.styleable.PengRadioButton_peng_drawableBottom:
                        drawableBottom = a.getDrawable(attr);
                        break;
                    case R.styleable.PengRadioButton_peng_drawableTop:
                        drawableTop = a.getDrawable(attr);
                        break;
                    case R.styleable.PengRadioButton_peng_drawableLeft:
                        drawableLeft = a.getDrawable(attr);
                        break;
                    case R.styleable.PengRadioButton_peng_drawableRight:
                        drawableRight = a.getDrawable(attr);
                        break;
                    case R.styleable.PengRadioButton_peng_drawableTopWith:
                        mTopWith = (int) (a.getDimension(attr, 20) * scale + 0.5f);
                        break;
                    case R.styleable.PengRadioButton_peng_drawableTopHeight:
                        mTopHeight = (int) (a.getDimension(attr, 20) * scale + 0.5f);
                        break;
                    case R.styleable.PengRadioButton_peng_drawableBottomWith:
                        mBottomWith = (int) (a.getDimension(attr, 20) * scale + 0.5f);
                        break;
                    case R.styleable.PengRadioButton_peng_drawableBottomHeight:
                        mBottomHeight = (int) (a.getDimension(attr, 20) * scale + 0.5f);
                        break;
                    case R.styleable.PengRadioButton_peng_drawableRightWith:
                        mRightWith = (int) (a.getDimension(attr, 20) * scale + 0.5f);
                        break;
                    case R.styleable.PengRadioButton_peng_drawableRightHeight:
                        mRightHeight = (int) (a.getDimension(attr, 20) * scale + 0.5f);
                        break;
                    case R.styleable.PengRadioButton_peng_drawableLeftWith:
                        mLeftWith = (int) (a.getDimension(attr, 20) * scale + 0.5f);
                        break;
                    case R.styleable.PengRadioButton_peng_drawableLeftHeight:
                        mLeftHeight = (int) (a.getDimension(attr, 20) * scale + 0.5f);
                        break;

                    default:
                        break;
                }
            }
            a.recycle();
            setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
        }
    }

    // 设置Drawable定义的大小
    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if (left != null) {
            left.setBounds(0, 0, mLeftWith <= 0 ? left.getIntrinsicWidth() : mLeftWith, mLeftHeight <= 0 ? left.getMinimumHeight() : mLeftHeight);
        }
        if (right != null) {
            right.setBounds(0, 0, mRightWith <= 0 ? right.getIntrinsicWidth() : mRightWith, mRightHeight <= 0 ? right.getMinimumHeight() : mRightHeight);
        }
        if (top != null) {
            top.setBounds(0, 0, mTopWith <= 0 ? top.getIntrinsicWidth() : mTopWith, mTopHeight <= 0 ? top.getMinimumHeight() : mTopHeight);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mBottomWith <= 0 ? bottom.getIntrinsicWidth() : mBottomWith, mBottomHeight <= 0 ? bottom.getMinimumHeight()
                    : mBottomHeight);
        }
        setCompoundDrawables(left, top, right, bottom);
    }

   /* private int radius = 10 ;

    private Paint paint ; //, paintText;

    private boolean firstCreate = true;

    private boolean showRedpoint = false;

    Rect t = new Rect();

    int height;

    int width;

    DisplayMetrics dm = getResources().getDisplayMetrics();

    float value = dm.scaledDensity;

    int textSize = 8;

    public CustomRadioButton(Context context) {
        this(context, null);
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
    }

    private void setupPaint() {
        paint = new Paint();
        // 设置抗锯齿效果
        paint.setAntiAlias(true);
        // 设置画刷的颜色
        paint.setColor(Color.RED);

//        paintText = new Paint() ;
//        paintText.setAntiAlias(true);
//        paintText.setColor(Color.WHITE);
//        paintText.setTextSize(textSize);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setTextSize(textSize * value);
        if (firstCreate) {
            canvas.getClipBounds(t);
            firstCreate = false;
            height = getHeight();
            width = getWidth();
        }

        if (showRedpoint) {
            Drawable[] drawables = getCompoundDrawables();
            Rect rect = new Rect();
            for (int i = 0; i < drawables.length; i++) {
                if (drawables[i] != null) {
                    rect = drawables[i].getBounds();
                    break;
                }
            }
            int offset = (getWidth() - rect.right) / 2;
            if(offset < radius) {
                offset = radius ;
            }

            int offsetTop =  (getHeight() - rect.bottom)/2 ;
            if(offsetTop < radius){
                offsetTop = radius ;
            }
            canvas.drawCircle(t.right - offset, t.top + offsetTop, radius, paint);
            //canvas.drawText("12" ,t.right - offset - radius, t.top + offsetTop, paintText);
        }

    }

    public void setShowRedPoint(boolean bool) {
        showRedpoint = bool;
        invalidate();
    }

    *//**
     * 现在是否显示着红点
     * @return
     *//*
    public boolean redPointIsShowing() {
        return showRedpoint;
    }*/

}
