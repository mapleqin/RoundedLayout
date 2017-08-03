package org.amphiaraus.roundedlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * author: EwenQin
 * since : 2017/8/2 下午5:46.
 */
@SuppressWarnings("unused")
public class RoundedLayout extends FrameLayout {

    private Path mClipPath = new Path();
    private final Paint mPaint = new Paint();
    private final RectF mLayoutBounds = new RectF();
    private final float[] mRoundCornerRadii = new float[8];
    private boolean mIsLaidOut = false;

    private int mRoundedCornerRadius;
    private boolean mRoundAsCircle;
    private boolean mRoundTopLeft;
    private boolean mRoundTopRight;
    private boolean mRoundBottomLeft;
    private boolean mRoundBottomRight;

    private int mRoundingBorderWidth;
    private int mRoundingBorderColor;

    public RoundedLayout(Context context) {
        super(context);
        initLayouts(context, null, 0, 0);
    }

    public RoundedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayouts(context, attrs, 0, 0);
    }

    public RoundedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayouts(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoundedLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initLayouts(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initLayouts(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (isInEditMode()) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedLayout, defStyleAttr, defStyleRes);
        mRoundedCornerRadius = a.getDimensionPixelSize(R.styleable.RoundedLayout_rlRoundedCornerRadius, 0);
        mRoundAsCircle = a.getBoolean(R.styleable.RoundedLayout_rlRoundAsCircle, false);
        mRoundTopLeft = a.getBoolean(R.styleable.RoundedLayout_rlRoundTopLeft, true);
        mRoundTopRight = a.getBoolean(R.styleable.RoundedLayout_rlRoundTopRight, true);
        mRoundBottomLeft = a.getBoolean(R.styleable.RoundedLayout_rlRoundBottomLeft, true);
        mRoundBottomRight = a.getBoolean(R.styleable.RoundedLayout_rlRoundBottomRight, true);
        mRoundingBorderWidth = a.getDimensionPixelSize(R.styleable.RoundedLayout_rlRoundingBorderWidth, 0);
        mRoundingBorderColor = a.getColor(R.styleable.RoundedLayout_rlRoundingBorderColor, 0);
        a.recycle();

        mPaint.setAntiAlias(true);
        mPaint.setColor(mRoundingBorderColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRoundingBorderWidth * 2);
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        adjustClipPathBounds();
    }

    @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mLayoutBounds.set(0, 0, right - left, bottom - top);
        if (!mIsLaidOut) {
            mIsLaidOut = true;
            adjustClipPathBounds();
        }
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mIsLaidOut = false;
    }

    private void adjustClipPathBounds() {
        if (!mIsLaidOut) {
            return;
        }
        float cornerRadius = mRoundedCornerRadius;
        if (mRoundAsCircle) {
            cornerRadius = Math.max(mLayoutBounds.width(), mLayoutBounds.height()) / 2f;
        }
        mClipPath.reset();
        mClipPath.addRoundRect(mLayoutBounds, buildRoundCornerRadii(cornerRadius), Path.Direction.CW);
        mClipPath.close();
    }

    private float[] buildRoundCornerRadii(float cornerRadius) {
        mRoundCornerRadii[0] = mRoundCornerRadii[1] = mRoundTopLeft ? cornerRadius : 0;
        mRoundCornerRadii[2] = mRoundCornerRadii[3] = mRoundTopRight ? cornerRadius : 0;
        mRoundCornerRadii[4] = mRoundCornerRadii[5] = mRoundBottomRight ? cornerRadius : 0;
        mRoundCornerRadii[6] = mRoundCornerRadii[7] = mRoundBottomLeft ? cornerRadius : 0;
        return mRoundCornerRadii;
    }

    @Override protected void onDraw(Canvas canvas) {
        canvas.clipPath(mClipPath);
        super.onDraw(canvas);
        if (mRoundingBorderWidth > 0 && mRoundingBorderColor != 0) {
            canvas.drawPath(mClipPath, mPaint);
        }
    }

    @Override protected void dispatchDraw(Canvas canvas) {
        canvas.clipPath(mClipPath);
        super.dispatchDraw(canvas);
        if (mRoundingBorderWidth > 0 && mRoundingBorderColor != 0) {
            canvas.drawPath(mClipPath, mPaint);
        }
    }

    /**
     * Setts the width of the View rounded border.
     *
     * @param width View the width of the rounded border . Unit px
     */
    public void setRoundingBorderWidth(int width) {
        if (width != mRoundingBorderWidth) {
            mRoundingBorderWidth = width;
            mPaint.setStrokeWidth(mRoundingBorderWidth * 2);
            postInvalidate();
        }
    }

    /**
     * Setts the color of the View rounded border.
     *
     * @param color View the color of the rounded border
     */
    public void setRoundingBorderColor(int color) {
        if (color != mRoundingBorderColor) {
            mRoundingBorderColor = color;
            mPaint.setColor(mRoundingBorderColor);
            postInvalidate();
        }
    }

    /**
     * Set's whether to round as circle
     *
     * @param asCircle Whether to round as circle
     */
    public void setRoundAsCircle(boolean asCircle) {
        if (asCircle != mRoundAsCircle) {
            mRoundAsCircle = asCircle;
            adjustClipPathBounds();
            postInvalidate();
        }
    }

    /**
     * Set the radius of the corner angle and whether the edges are enabled
     *
     * @param cornerRadius The radius of the angle of the circle. Unit px
     */
    public void setRoundedCornerRadius(int cornerRadius) {
        setRoundedCornerRadius(cornerRadius, true, true, true, true);
    }

    /**
     * Set the radius of the corner angle and whether the edges are enabled
     *
     * @param cornerRadius The radius of the angle of the circle. Unit px
     * @param topLeft      Whether to enable top left fillet
     * @param topRight     Whether to enable top right fillet
     * @param bottomLeft   Whether to enable bottom left fillet
     * @param bottomRight  Whether to enable bottom right fillet
     */
    public void setRoundedCornerRadius(int cornerRadius, boolean topLeft, boolean topRight,
                                       boolean bottomRight, boolean bottomLeft) {
        if (mRoundedCornerRadius != cornerRadius
                || mRoundTopLeft != topLeft || mRoundTopRight != topRight
                || mRoundBottomLeft != bottomLeft || mRoundBottomRight != bottomRight) {
            mRoundedCornerRadius = cornerRadius;
            mRoundTopLeft = topLeft;
            mRoundTopRight = topRight;
            mRoundBottomLeft = bottomLeft;
            mRoundBottomRight = bottomRight;
            adjustClipPathBounds();
            postInvalidate();
        }
    }

    /**
     * Gets the angle of the corner of the View fillet.
     *
     * @return Returns the current angle of the corner of the View fillet
     */
    public int getRoundedCornerRadius() {
        return mRoundedCornerRadius;
    }

    /**
     * Gets whether to round as circle
     *
     * @return Returns whether to round as circle
     */
    public boolean isRoundAsCircle() {
        return mRoundAsCircle;
    }

    /**
     * Gets whether to enable top left fillet
     *
     * @return Returns whether to enable top left fillet
     */
    public boolean isRoundTopLeft() {
        return mRoundTopLeft;
    }

    /**
     * Gets whether to enable top right fillet
     *
     * @return Returns whether to enable top right fillet
     */
    public boolean isRoundTopRight() {
        return mRoundTopRight;
    }

    /**
     * Gets whether to enable bottom left fillet
     *
     * @return Returns whether to enable bottom left fillet
     */
    public boolean isRoundBottomLeft() {
        return mRoundBottomLeft;
    }

    /**
     * Gets whether to enable bottom right fillet
     *
     * @return Returns whether to enable bottom right fillet
     */
    public boolean isRoundBottomRight() {
        return mRoundBottomRight;
    }

    /**
     * Gets the width of the View rounded border. Unit px
     *
     * @return Returns the current width of the View Rounded Border
     */
    public int getRoundingBorderWidth() {
        return mRoundingBorderWidth;
    }

    /**
     * Gets the color of the View rounded border.
     *
     * @return Returns the current color of the View Rounded Border
     */
    public int getRoundingBorderColor() {
        return mRoundingBorderColor;
    }
}
