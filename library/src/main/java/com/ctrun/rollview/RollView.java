package com.ctrun.rollview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;

/**
 * Created by ctrun on 2016/8/3.
 *
 */
public class RollView extends ViewFlipper {
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    private int textViewResourceId;

    public RollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        inflater = LayoutInflater.from(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RollView, defStyleAttr, 0);
        int animDuration = a.getInteger(R.styleable.RollView_rvAnimDuration, 0);
        textViewResourceId = a.getResourceId(R.styleable.RollView_rvTextView, 0);
        a.recycle();

        Animation animIn = AnimationUtils.loadAnimation(context, R.anim.rollview_in);
        Animation animOut = AnimationUtils.loadAnimation(context, R.anim.rollview_out);
        if (animDuration > 0) {
            animIn.setDuration(animDuration);
            animOut.setDuration(animDuration);
        }
        setInAnimation(animIn);
        setOutAnimation(animOut);
    }

    public void updateRollData(ArrayList<String> data) {
        removeAllViews();

        if (data==null || data.isEmpty()) return;

        for (int i=0; i<data.size(); i++) {
            TextView textView = makeTextView();
            textView.setText(data.get(i));
            final int position = i;

            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
            addView(textView);
        }

    }

    @Override
    public void removeAllViews() {
        //清除视图的动画效果，解决刷新滚动消息的时候容易出现重叠问题
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            child.clearAnimation();
            child.setVisibility(View.GONE);
        }

        super.removeAllViews();
    }

    // 创建ViewFlipper下的TextView
    private TextView makeTextView() {
        TextView tv = (TextView) inflater.inflate(textViewResourceId, null);
        return tv;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

}
