package zss.ntu.com.demo5_tagview;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Tag View
 */
public class TagView extends FrameLayout
{
    private static final String TAG = "TagView";

    private int tagViewWidth;
    private int tagViewHeight;

    public View animatorPoint = null;
    public TextView tvContent = null;
    public FoldLineView foldLineView = null;

    public TagItem tag;

    private static final int indicatorPointSize = FoldLineView.OUTER_INDICATOR_POINT_RADIUS*2;
    private static final int tvContentHeight = DensityHelper.dp2px(24);
    private static final int foldLineViewWidth = DensityHelper.dp2px(32);
    private static final int foldLineViewHeight = DensityHelper.dp2px(28);
    private static final int foldLineViewCircleRadius = DensityHelper.dp2px(FoldLineView.END_CIRCLE_RADIUS);

    public TagView(Context context)
    {
        super(context);
        this.setClipChildren(false);
    }

    public TagView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.setClipChildren(false);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.setClipChildren(false);
    }

    @TargetApi(23)
    public TagView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public int getTagViewWidth()
    {
        return tagViewWidth;
    }

    public int getTagViewHeight()
    {
        return tagViewHeight;
    }

    public void setTag(TagItem tag)
    {
        this.tag = tag;
        //this.setBackgroundColor(0xff00ff00);
        //displayTag();
    }

    public void displayTag()
    {
        /**
         * init tagView
         */
        if(tag == null)
        {
            return ;
        }
        if(animatorPoint != null)
        {
            this.removeAllViews();
        }


        /**
         * add point for animation
         */
        MarginLayoutParams animatorPointLp;
        if(animatorPoint == null)
        {
            animatorPoint = new ImageView(getContext());
            animatorPointLp = new MarginLayoutParams(indicatorPointSize, indicatorPointSize);
            int resourceId = R.drawable.shape_ring_tag_point_animation;
            if(android.os.Build.VERSION.SDK_INT < 16)
            {
                animatorPoint.setBackgroundDrawable(this.getResources().getDrawable(resourceId));
            }
            else
            {
                if(android.os.Build.VERSION.SDK_INT < 21)
                {
                    animatorPoint.setBackground(this.getResources().getDrawable(resourceId));
                }
                else
                {
                    animatorPoint.setBackground(this.getResources().getDrawable(resourceId, null));
                }
            }
        }
        else
        {
            animatorPointLp = (MarginLayoutParams)animatorPoint.getLayoutParams();
        }
        //animatorPoint.setBackgroundColor(0xff333333);
        this.addView(animatorPoint, animatorPointLp);


        /**
         * add fold line
         */
        MarginLayoutParams foldLineViewLp;
        if(foldLineView == null)
        {
            foldLineView = new FoldLineView(getContext());
            foldLineViewLp = new MarginLayoutParams(foldLineViewWidth, foldLineViewHeight);
        }
        else
        {
            foldLineViewLp = (MarginLayoutParams)foldLineView.getLayoutParams();
        }
        //adjustFoldLineViewDirection(tag, foldLineViewLp);
        //foldLineView.setBackgroundColor(0xff0000ff);
        this.addView(foldLineView, foldLineViewLp);


        /**
         * add content text
         */
        // init content text
        MarginLayoutParams tvContentLp;
        if(tvContent == null)
        {
            tvContent = new TextView(getContext());
            tvContent.setTextColor(0xffffffff);
            tvContent.setSingleLine(true);
            tvContent.setGravity(Gravity.CENTER);
            tvContent.setTextSize(12);
            tvContentLp = new MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            tvContentLp.height = tvContentHeight;
            // set tag for the onClickListener
            tvContent.setTag(this);
        }
        else
        {
            tvContentLp = (MarginLayoutParams)tvContent.getLayoutParams();
        }
        tvContent.setText(String.format("   %s   ", tag.text));
        tvContent.measure(0, 0);
        int tvContentWidth = tvContent.getMeasuredWidth();

        // get tagViewWidth and tagViewHeight
        this.tagViewWidth = foldLineViewWidth + tvContentWidth - foldLineViewCircleRadius/2;
        this.tagViewHeight = foldLineViewHeight + tvContentHeight/2 - foldLineViewCircleRadius/2;

        this.addView(tvContent, tvContentLp);
    }


    public void adjustDirection()
    {
        MarginLayoutParams animatorPointLp = (MarginLayoutParams)animatorPoint.getLayoutParams();
        MarginLayoutParams tvContentLp = (MarginLayoutParams)tvContent.getLayoutParams();
        MarginLayoutParams foldLineViewLp = (MarginLayoutParams)foldLineView.getLayoutParams();
        foldLineView.setDirection(tag.direction);
        switch (tag.direction)
        {
            case UNDEFINED:
            {
                tag.direction = TagItem.Direction.LEFT_TOP;
            }
            case LEFT_TOP:
            {
                animatorPointLp.leftMargin = tagViewWidth - indicatorPointSize;
                animatorPointLp.topMargin = tagViewHeight - indicatorPointSize;
                tvContent.setBackgroundResource(R.drawable.icon_tag_left);
                tvContentLp.topMargin = 0;
                tvContentLp.leftMargin = 0;
                foldLineViewLp.leftMargin = tagViewWidth - foldLineViewWidth;
                foldLineViewLp.topMargin = tvContentHeight/2 - foldLineViewCircleRadius/2;
                break;
            }
            case LEFT_BOTTOM:
            {
                animatorPointLp.leftMargin = tagViewWidth - indicatorPointSize;
                animatorPointLp.topMargin = 0;
                tvContent.setBackgroundResource(R.drawable.icon_tag_left);
                tvContentLp.topMargin = tagViewHeight - tvContentHeight;
                tvContentLp.leftMargin = 0;
                foldLineViewLp.leftMargin = tagViewWidth - foldLineViewWidth;
                foldLineViewLp.topMargin = 0;
                break;
            }
            case RIGHT_TOP:
            {
                animatorPointLp.leftMargin = 0;
                animatorPointLp.topMargin = tagViewHeight - indicatorPointSize;
                tvContent.setBackgroundResource(R.drawable.icon_tag_right);
                tvContentLp.leftMargin = foldLineViewWidth - foldLineViewCircleRadius/2;
                tvContentLp.topMargin = 0;
                foldLineViewLp.leftMargin = 0;
                foldLineViewLp.topMargin = tagViewHeight - foldLineViewHeight;
                break;
            }
            case RIGHT_BOTTOM:
            {
                animatorPointLp.leftMargin = 0;
                animatorPointLp.topMargin = 0;
                tvContent.setBackgroundResource(R.drawable.icon_tag_right);
                tvContentLp.leftMargin = foldLineViewWidth - foldLineViewCircleRadius/2;
                tvContentLp.topMargin = tagViewHeight - tvContentHeight;
                foldLineViewLp.leftMargin = 0;
                foldLineViewLp.topMargin = 0;
                break;
            }
        }
        animatorPoint.setLayoutParams(animatorPointLp);
        tvContent.setLayoutParams(tvContentLp);
        foldLineView.setLayoutParams(foldLineViewLp);
    }
}
