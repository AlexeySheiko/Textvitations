package com.aviary.android.feather.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher.ViewFactory;

import com.aviary.android.feather.sdk.R;

public class AviaryNavBarViewFlipper extends ViewFlipper implements ViewFactory {
    TextSwitcher mTextSwitcher;

    Button       mButton2, mButton1;
    OnToolbarClickListener mListener;
    ProgressBar            mProgress1, mProgress2;
    boolean   mClickable;
    ViewState mStatus;
    private boolean mButtonSizeChanged;
    public AviaryNavBarViewFlipper(Context context) {
        super(context);
    }

    public AviaryNavBarViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!mButtonSizeChanged) {
            int button1Size = mButton1.getMeasuredWidth();
            int button2Size = mButton2.getMeasuredWidth();

            int maxSize = Math.max(button1Size, button2Size);

            if (button1Size != maxSize) {
                mButton1.setWidth(maxSize);
            }

            if (button2Size != maxSize) {
                mButton2.setWidth(maxSize);
            }

            mButtonSizeChanged = true;
        }
    }

    public void open() {
        if (!opened()) {
            mStatus.setCurrent(ViewState.Status.Open);
            setDisplayedChild(1);
        }
    }

    public void close() {
        if (!closed()) {
            mStatus.setCurrent(ViewState.Status.Close);
            setDisplayedChild(0);
        }
    }

    public boolean closed() {
        return mStatus.current == ViewState.Status.Close;
    }    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mStatus = new ViewState();
        mStatus.current = ViewState.Status.Close;
        mStatus.previous = ViewState.Status.Close;

        mTextSwitcher = (TextSwitcher) findViewById(R.id.navbar_text2);

        // done
        mButton1 = (Button) findViewById(R.id.navbar_button1);
        mProgress1 = (ProgressBar) findViewById(R.id.navbar_progress1);

        // apply
        mButton2 = (Button) findViewById(R.id.navbar_button2);
        mProgress2 = (ProgressBar) findViewById(R.id.navbar_progress2);

        mTextSwitcher.setFactory(this);

        mButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && isClickable() && opened()) {
                    mListener.onApplyClick();
                }
            }
        });

        mButton1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && isClickable() && !opened()) {
                    mListener.onDoneClick();
                }
            }
        });
    }

    public void setOnToolbarClickListener(OnToolbarClickListener listener) {
        mListener = listener;
    }

    public void setApplyEnabled(boolean enabled) {
        mButton2.setEnabled(enabled);
    }

    public void setApplyVisible(boolean visible) {
        mButton2.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }    public boolean opened() {
        return mStatus.current == ViewState.Status.Open;
    }

    public boolean getApplyProgressVisible() {
        return mProgress2.getVisibility() == View.VISIBLE;
    }

    public void setApplyProgressVisible(boolean visible) {
        mProgress2.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }    @Override
    public void setClickable(boolean clickable) {
        mClickable = clickable;
    }

    public void setDoneEnabled(boolean enabled) {
        mButton1.setEnabled(enabled);
    }    @Override
    @ExportedProperty
    public boolean isClickable() {
        return mClickable;
    }

    public boolean getDoneProgressVisible() {
        return mProgress1.getVisibility() == View.VISIBLE;
    }

    public void setDoneProgressVisible(boolean visible) {
        mProgress1.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public void setTitle(CharSequence text) {
        setTitle(text, true);
    }

    public void setTitle(CharSequence text, boolean animate) {
        if (!animate) {
            Animation inAnimation = mTextSwitcher.getInAnimation();
            Animation outAnimation = mTextSwitcher.getOutAnimation();
            mTextSwitcher.setInAnimation(null);
            mTextSwitcher.setOutAnimation(null);
            mTextSwitcher.setText(text);
            mTextSwitcher.setInAnimation(inAnimation);
            mTextSwitcher.setOutAnimation(outAnimation);
        } else {
            mTextSwitcher.setText(text);
        }
    }

    public void setTitle(int resourceId) {
        setTitle(resourceId, true);
    }

    public void setTitle(int resourceId, boolean animate) {
        setTitle(getContext().getResources().getString(resourceId), animate);
    }

    @Override
    public View makeView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.aviary_navbar_text, null);

        return view;
    }

    public interface OnToolbarClickListener {
        void onDoneClick();

        void onApplyClick();
    }

    private static class ViewState {
        Status current;

        Status previous;

        void setCurrent(Status newState) {
            previous = current;
            current = newState;
        }
        static enum Status {
            Open, Close
        }
    }








}
