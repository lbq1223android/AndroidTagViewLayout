package zss.ntu.com.demo5_tagview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Fold line view.
 */
public class FoldLineView extends View
{
    private static final String TAG = "FoldLineView";
    private Paint paint;
    private TagItem.Direction direction = TagItem.Direction.UNDEFINED;
    public static final int END_CIRCLE_RADIUS = DensityHelper.dp2px(2);
    public static final int OUTER_INDICATOR_POINT_RADIUS = DensityHelper.dp2px(4);
    public static final int INNER_INDICATOR_POINT_RADIUS = DensityHelper.dp2px(3);

    public FoldLineView(Context context)
    {
        super(context);
        initPaint();
        initAttrs(null);
    }

    public FoldLineView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initPaint();
        initAttrs(attrs);
    }

    public FoldLineView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initPaint();
        initAttrs(attrs);
    }

    @TargetApi(23)
    public FoldLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
        initAttrs(attrs);
    }

    public void setDirection(TagItem.Direction direction)
    {
        this.direction = direction;
        invalidate();
    }

    private void initPaint()
    {
        paint = new Paint();
        paint.setStrokeWidth(DensityHelper.dp2px(1));
        paint.setAntiAlias(true);
    }

    private void initAttrs(AttributeSet attrs)
    {
        if(attrs == null)
        {
            this.direction = TagItem.Direction.LEFT_TOP;
            return ;
        }
        // get and set direction
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FoldLineViewAttrs, 0, 0);
        try
        {
            int direction = ta.getInt(R.styleable.FoldLineViewAttrs_direction, -1);
            switch (direction)
            {
                case -1:
                case 0:
                {
                    this.direction = TagItem.Direction.LEFT_TOP;
                    break;
                }
                case 1:
                {
                    this.direction = TagItem.Direction.LEFT_BOTTOM;
                    break;
                }
                case 2:
                {
                    this.direction = TagItem.Direction.RIGHT_TOP;
                    break;
                }
                case 3:
                {
                    this.direction = TagItem.Direction.RIGHT_BOTTOM;
                    break;
                }
            }
        }
        finally
        {
            ta.recycle();
        }
    }

    private void drawLine(Canvas canvas)
    {
        // create and init path
        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();
        int width1 = (int)(0.55*(width - END_CIRCLE_RADIUS));

        // draw line and circle
        Path path = new Path();
        //measure = new PathMeasure(path, false);
        //pathLength = measure.getLength();
        switch (direction)
        {
            case UNDEFINED:
            {
                direction = TagItem.Direction.LEFT_TOP;
            }
            case LEFT_TOP:
            {
                // draw circles
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(0xFFF94E6A);
                canvas.drawCircle(width - OUTER_INDICATOR_POINT_RADIUS, height - OUTER_INDICATOR_POINT_RADIUS, OUTER_INDICATOR_POINT_RADIUS, paint);
                paint.setColor(0xFFFFFFFF);
                canvas.drawCircle(width - OUTER_INDICATOR_POINT_RADIUS, height - OUTER_INDICATOR_POINT_RADIUS, INNER_INDICATOR_POINT_RADIUS, paint);
                canvas.drawCircle(END_CIRCLE_RADIUS, END_CIRCLE_RADIUS, END_CIRCLE_RADIUS, paint);

                // draw line
                path.moveTo(width - OUTER_INDICATOR_POINT_RADIUS, height - OUTER_INDICATOR_POINT_RADIUS);
                path.lineTo(width - width1, height - OUTER_INDICATOR_POINT_RADIUS);
                path.lineTo(END_CIRCLE_RADIUS, END_CIRCLE_RADIUS);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawPath(path, paint);
                break;
            }
            case LEFT_BOTTOM:
            {
                // draw circles
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(0xFFF94E6A);
                canvas.drawCircle(width - OUTER_INDICATOR_POINT_RADIUS, OUTER_INDICATOR_POINT_RADIUS, OUTER_INDICATOR_POINT_RADIUS, paint);
                paint.setColor(0xFFFFFFFF);
                canvas.drawCircle(width - OUTER_INDICATOR_POINT_RADIUS, OUTER_INDICATOR_POINT_RADIUS, INNER_INDICATOR_POINT_RADIUS, paint);
                canvas.drawCircle(END_CIRCLE_RADIUS, height - END_CIRCLE_RADIUS, END_CIRCLE_RADIUS, paint);

                // draw line
                path.moveTo(width - OUTER_INDICATOR_POINT_RADIUS, OUTER_INDICATOR_POINT_RADIUS);
                path.lineTo(width - width1, OUTER_INDICATOR_POINT_RADIUS);
                path.lineTo(END_CIRCLE_RADIUS, height - END_CIRCLE_RADIUS);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawPath(path, paint);
                break;
            }
            case RIGHT_TOP:
            {
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(0xFFF94E6A);
                canvas.drawCircle(OUTER_INDICATOR_POINT_RADIUS, height - OUTER_INDICATOR_POINT_RADIUS, OUTER_INDICATOR_POINT_RADIUS, paint);
                paint.setColor(0xFFFFFFFF);
                canvas.drawCircle(OUTER_INDICATOR_POINT_RADIUS, height - OUTER_INDICATOR_POINT_RADIUS, INNER_INDICATOR_POINT_RADIUS, paint);
                canvas.drawCircle(width - END_CIRCLE_RADIUS, END_CIRCLE_RADIUS, END_CIRCLE_RADIUS, paint);

                path.moveTo(OUTER_INDICATOR_POINT_RADIUS, height - OUTER_INDICATOR_POINT_RADIUS);
                path.lineTo(width1, height - OUTER_INDICATOR_POINT_RADIUS);
                path.lineTo(width - END_CIRCLE_RADIUS, END_CIRCLE_RADIUS);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawPath(path, paint);
                break;
            }
            case RIGHT_BOTTOM:
            {
                // draw circles
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(0xFFF94E6A);
                canvas.drawCircle(OUTER_INDICATOR_POINT_RADIUS, OUTER_INDICATOR_POINT_RADIUS, OUTER_INDICATOR_POINT_RADIUS, paint);
                paint.setColor(0xFFFFFFFF);
                canvas.drawCircle(OUTER_INDICATOR_POINT_RADIUS, OUTER_INDICATOR_POINT_RADIUS, INNER_INDICATOR_POINT_RADIUS, paint);
                canvas.drawCircle(width - END_CIRCLE_RADIUS, height - END_CIRCLE_RADIUS, END_CIRCLE_RADIUS, paint);

                // draw line
                path.moveTo(OUTER_INDICATOR_POINT_RADIUS, OUTER_INDICATOR_POINT_RADIUS);
                path.lineTo(width1, OUTER_INDICATOR_POINT_RADIUS);
                path.lineTo(width - END_CIRCLE_RADIUS, height - END_CIRCLE_RADIUS);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawPath(path, paint);
                break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        drawLine(canvas);
    }
}

