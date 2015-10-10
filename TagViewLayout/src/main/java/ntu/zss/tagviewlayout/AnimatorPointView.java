package ntu.zss.tagviewlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * AnimatorPointView
 */
public class AnimatorPointView extends View
{
    private static final String TAG = "AnimatorPointView";
    private Paint paint;

    public AnimatorPointView(Context context)
    {
        super(context);
        initPaint();
    }

    public AnimatorPointView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initPaint();
    }

    public AnimatorPointView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @TargetApi(23)
    public AnimatorPointView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
    }

    private void initPaint()
    {
        paint = new Paint();
        paint.setStrokeWidth(DensityHelper.dp2px(1));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DensityHelper.dp2px(1));
        paint.setColor(0xFFF94E6A);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();
        int radius = width > height ? height : width;
        canvas.drawCircle(width/2, height/2, radius, paint);
    }
}
