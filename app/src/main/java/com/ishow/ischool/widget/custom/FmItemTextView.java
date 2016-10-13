package com.ishow.ischool.widget.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.commonlib.util.UIUtil;
import com.ishow.ischool.R;

/**
 * Created by MrS on 2016/8/10.
 */
public class FmItemTextView extends TextView {
    /*消息提示 文本 小红点的绘制  --------下划线画笔*/
    private Paint rightTipPaint,bottomLinePaint;
    /*下划线左侧边距*/
    private int bottom_line_Lp;
    /*文本提示类型的 应该距离右侧边距多远*/
    private int tip_Rp;
    /*下划线的颜色*/
    private int line_color = Color.parseColor("#d9dadd");//默认色
    /*是否绘制下划线*/
    private boolean drawLine;
    /*是否绘制小红点 否则绘制文本*/
    private boolean drawRPoint;
    /*右侧提示类型的颜色*/
    private int rightTipColor = Color.parseColor("#999999");//默认色


    private String tipTxt;//提示类型文本

    private int redPointRadius; //小红点半径

    private boolean hasUnread; //是否有未读消息

    private float txtHeight;//需要绘制的文本的高度
    private float txtWidth;//需要绘制的文本的宽度
    /*控件的 宽高*/
    private int measuredWidth;
    private int measuredHeight;



    public FmItemTextView(Context context) {
        super(context);initEnv(context, null);
    }

    public FmItemTextView(Context context, AttributeSet attrs) {
        super(context, attrs);initEnv(context, attrs);
    }

    public FmItemTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);initEnv(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FmItemTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initEnv(context, attrs);

    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        Parcelable superData = super.onSaveInstanceState();
        bundle.putParcelable("super_data", superData);
        bundle.putInt("bottom_line_Lp", bottom_line_Lp);
        bundle.putInt("tip_Rp", tip_Rp);
        bundle.putInt("line_color", line_color);
        bundle.putBoolean("drawLine", drawLine);
        bundle.putBoolean("drawRPoint", drawRPoint);
        bundle.putBoolean("hasUnread", hasUnread);
        bundle.putInt("rightTipColor", rightTipColor);
        bundle.putString("tipTxt", tipTxt);
        bundle.putFloat("txtHeight",txtHeight);
        bundle.putFloat("txtWidth",txtWidth);
        bundle.putInt("measuredWidth",measuredWidth);
        bundle.putInt("measuredHeight",measuredHeight);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable superData = bundle.getParcelable("super_data");
        bottom_line_Lp = bundle.getInt("bottom_line_Lp");
        tip_Rp = bundle.getInt("tip_Rp");
        line_color = bundle.getInt("line_color");
        rightTipColor = bundle.getInt("rightTipColor");
        measuredWidth = bundle.getInt("measuredWidth");
        measuredHeight = bundle.getInt("measuredHeight");
        txtHeight = bundle.getFloat("txtHeight");
        txtWidth = bundle.getFloat("txtWidth");
        drawLine = bundle.getBoolean("drawLine");
        drawRPoint = bundle.getBoolean("drawRPoint");
        hasUnread = bundle.getBoolean("hasUnread");
        tipTxt =bundle.getString("tipTxt");

        super.onRestoreInstanceState(superData);
    }

    private void initEnv(Context context, AttributeSet attrs) {
        setBackgroundResource(R.drawable.bg_item_text_ripple);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FmItemTextView);
        bottom_line_Lp = ta.getInteger(R.styleable.FmItemTextView_draw_bottom_line_padding, 0);
        line_color = ta.getColor(R.styleable.FmItemTextView_draw_bottom_line_color, line_color);
        drawLine = ta.getBoolean(R.styleable.FmItemTextView_draw_bottom_line, false);
        tipTxt = ta.getString(R.styleable.FmItemTextView_draw_right_tip_txt);

        tip_Rp = ta.getInteger(R.styleable.FmItemTextView_tip_right_padding, 0);
        drawRPoint = ta.getBoolean(R.styleable.FmItemTextView_draw_right_redPoint, false);
        rightTipColor = ta.getColor(R.styleable.FmItemTextView_tip_right_color, rightTipColor);
        ta.recycle();

        /*初始化画笔资源了 */
        rightTipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rightTipPaint.setColor(rightTipColor);
        rightTipPaint.setTextSize(UIUtil.sp2px(context,14));

        /*dp－－－－＞ｓｐ*/
        tip_Rp = UIUtil.dip2px(context,tip_Rp);

        /*如果需要绘制下划线 才初始化这个画笔*/
       if (drawLine) {
           bottomLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
           bottomLinePaint.setColor(line_color);
           bottomLinePaint.setStrokeWidth(2);
           /*dp----->sp  下划线宽度*/
           bottom_line_Lp = UIUtil.dip2px(context,bottom_line_Lp);

       }

        /*如果是绘制文本类型的 拿到文本的高度*/
        if (!drawRPoint&&tipTxt!=null&&tipTxt!="") {
            Paint.FontMetrics fontMetrics = rightTipPaint.getFontMetrics();
            txtHeight = fontMetrics.descent - fontMetrics.ascent;
            txtWidth = rightTipPaint.measureText(tipTxt);
        }else{
            /*初始化消息提示类型小圆点半径*/
            redPointRadius = UIUtil.dip2px(context,4);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         measuredWidth = getMeasuredWidth();
         measuredHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*如果需要绘制下划线*/
        if (drawLine)
            canvas.drawLine(bottom_line_Lp,measuredHeight,measuredWidth,measuredHeight,bottomLinePaint);

        /*如果绘制提示类型为消息小红点*/
        if (drawRPoint) {
            rightTipPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(measuredWidth - tip_Rp - redPointRadius, measuredHeight / 2, redPointRadius, rightTipPaint);
        }

        float v = measuredWidth - tip_Rp - txtWidth;
       // LogUtil.e(v+"--"+measuredWidth+"--"+tip_Rp+"--"+txtWidth);
         /*绘制文本提示类型*/
        if (tipTxt!=null&&tipTxt!="")
           canvas.drawText(tipTxt, v,measuredHeight/2+txtHeight/4,rightTipPaint);//+txtHeight/4

    }

    /*更新提示文本信息*/
    public void setTipTxt(String tipTxt) {
        this.tipTxt = tipTxt;
        txtWidth = rightTipPaint.measureText(tipTxt);
        Paint.FontMetrics fontMetrics = rightTipPaint.getFontMetrics();
        txtHeight = fontMetrics.descent - fontMetrics.ascent;
        invalidate();
    }
    public String getTipTxt(){
        return this.tipTxt;
    }

    /*刷新 是否有未读消息提醒 */
    public void updateHasUnreadMsg(boolean hasUnread){
        this.hasUnread = hasUnread;
        invalidate();
    }
}
