package zss.ntu.com.demo5_tagview;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;

/**
 * Helper to add tags on a view.
 */
public class OldTagViewLayout extends RelativeLayout
{
    private static final String TAG = "TagHelper";
    private static final int ANIMATION_DURATION = 1500;

    private ArrayList<TagItem> tags;
    private ArrayList<TagView> tagViews = null;
    private AnimatorSet animatorSet = null;

    private boolean isTagsVisible = false;
    private boolean isAnimationPlaying = false;

    public OldTagViewLayout(Context context)
    {
        super(context);
    }

    public OldTagViewLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public OldTagViewLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(23)
    public OldTagViewLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setTags(ArrayList<TagItem> tags)
    {
        this.tags = tags;
    }

    public void displayTags()
    {
        /**
         * 1. init tagViews and animatorSet
         */
        // init tagViews
        if(tagViews != null)
        {
            showTags();
            return ;
        }
        tagViews = new ArrayList<>();

        // init animatorSet
        if(animatorSet != null && animatorSet.isRunning())
        {
            animatorSet.cancel();
        }
        animatorSet = new AnimatorSet();


        /**
         * 2. add tags
         */
        // measure container's size
        this.measure(0, 0);
        int containerWidth = this.getMeasuredWidth();
        int containerHeight = this.getMeasuredHeight();
        final int indicatorPointSize = DensityHelper.dp2px(12);
        for(TagItem tag: tags)
        {
            /**
             * 2.1 add point for indication
             */
            View indicatorPoint = new View(getContext());
            int resourceId = R.drawable.shape_ring_tag_point;
            if(android.os.Build.VERSION.SDK_INT < 16)
            {
                indicatorPoint.setBackgroundDrawable(getResources().getDrawable(resourceId));
            }
            else
            {
                if(android.os.Build.VERSION.SDK_INT < 21)
                {
                    indicatorPoint.setBackground(getResources().getDrawable(resourceId));
                }
                else
                {
                    indicatorPoint.setBackground(getResources().getDrawable(resourceId, null));
                }
            }
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(indicatorPointSize,
                    indicatorPointSize);
            lp.leftMargin = tag.x - lp.width/2;
            lp.topMargin = tag.y - lp.height/2;
            this.addView(indicatorPoint, lp);

            /**
             * 2.2 add point for animation
             */
            View animatorPoint = new View(getContext());
            resourceId = R.drawable.shape_ring_tag_point_animation;
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
            lp = new RelativeLayout.LayoutParams(indicatorPointSize, indicatorPointSize);
            lp.leftMargin = tag.x - lp.width/2;
            lp.topMargin = tag.y - lp.height/2;
            this.addView(animatorPoint, lp);
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(animatorPoint, "Alpha", 1, 0);
            alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
            alphaAnimator.setRepeatMode(ValueAnimator.RESTART);
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(animatorPoint, "scaleX", 1, 2.5f);
            scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
            scaleXAnimator.setRepeatMode(ValueAnimator.RESTART);
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(animatorPoint, "scaleY", 1, 2.5f);
            scaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);
            scaleYAnimator.setRepeatMode(ValueAnimator.RESTART);
            animatorSet.playTogether(alphaAnimator,
                    scaleXAnimator,
                    scaleYAnimator);


            /**
             * 2.3 add content text
             */
            // get fold line size
            final int foldLineViewWidth = DensityHelper.dp2px(30);
            final int foldLineViewHeight = DensityHelper.dp2px(25);
            final int foldLineViewCircleRadius = FoldLineView.END_CIRCLE_RADIUS;
            // init content text
            TextView tvContent = new TextView(getContext());
            tvContent.setTextColor(0xffffffff);
            tvContent.setText(String.format("   %s   ", tag.text));
            tvContent.setSingleLine(true);
            tvContent.setGravity(Gravity.CENTER);
            tvContent.setTextSize(12);
            RelativeLayout.LayoutParams tvContentLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            final int tvContentHeight = DensityHelper.dp2px(24);
            //lp.topMargin = tag.y - tvContentHeight/2;
            tvContentLp.height = tvContentHeight;
            // set direction
            tvContent.measure(0, 0);
            int tvContentWidth = tvContent.getMeasuredWidth();
            if(BuildConfig.DEBUG)
            {
                Log.e(TAG, String.format("%s width: %d", tag.text, tvContentWidth));
            }
            adjustContentDirection(tag, tvContentWidth, tvContentHeight, foldLineViewWidth, foldLineViewHeight,
                    foldLineViewCircleRadius, containerWidth, containerHeight, tvContentLp, tvContent);
            this.addView(tvContent, tvContentLp);


            /**
             * 2.4 add fold line
             */
            FoldLineView foldLineView = new FoldLineView(getContext());
            RelativeLayout.LayoutParams foldLineViewLp = new LayoutParams(DensityHelper.dp2px(30), DensityHelper.dp2px(25));
            adjustFoldLineViewDirection(tag, foldLineViewLp);
            foldLineView.setDirection(tag.contentDirection);
            Log.e(TAG, "foldLineView.direction: " + tag.contentDirection);
            this.addView(foldLineView, foldLineViewLp);

            // set animationDirection
            if(tag.y < containerHeight/2)
            {
                tag.animationDirection = TagItem.AnimationDirection.UP;
            }
            else
            {
                tag.animationDirection = TagItem.AnimationDirection.DOWN;
            }


            /**
             * 2.5 add views to tagViews
             */
            TagView tagView = new TagView(indicatorPoint, animatorPoint, tvContent, foldLineView, tag);
            tagViews.add(tagView);
            tvContent.setTag(tagView);
        }


        /**
         * 3. show tags
         */
        animatorSet.setDuration(ANIMATION_DURATION);
        showTags();
    }

    // adjust content text view's direction
    private void adjustContentDirection(TagItem tag,
                                        int tvContentWidth,
                                        int tvContentHeight,
                                        int foldLineViewWidth,
                                        int foldLineViewHeight,
                                        int foldLineViewCircleRadius,
                                        int containerWidth,
                                        int containerHeight,
                                        RelativeLayout.LayoutParams tvContentLp,
                                        TextView tvContent)
    {
        switch (tag.contentDirection)
        {
            case LEFT_TOP:
            {
                if(tag.x - tvContentWidth - foldLineViewWidth < 0)
                {
                    tvContentLp.leftMargin = tag.x + foldLineViewWidth - foldLineViewCircleRadius;
                    tvContent.setBackgroundResource(R.drawable.icon_tag_right);

                    // LEFT_TOP to RIGHT_BOTTOM
                    if(tag.y - tvContentHeight/2 - foldLineViewHeight < 0)
                    {
                        tag.contentDirection = TagItem.ContentDirection.RIGHT_BOTTOM;
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 + foldLineViewHeight - foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from LEFT_TOP to RIGHT_BOTTOM", tag.text));
                        }
                    }
                    // LEFT_TOP to RIGHT_TOP
                    else
                    {
                        tag.contentDirection = TagItem.ContentDirection.RIGHT_TOP;
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 - foldLineViewHeight + foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from LEFT_TOP to RIGHT_TOP", tag.text));
                        }
                    }
                }
                else
                {
                    tvContentLp.leftMargin = tag.x - tvContentWidth - foldLineViewWidth;
                    tvContent.setBackgroundResource(R.drawable.icon_tag_left);

                    // LEFT_TOP to LEFT_BOTTOM
                    if(tag.y - tvContentHeight/2 - foldLineViewHeight < 0)
                    {
                        tag.contentDirection = TagItem.ContentDirection.LEFT_BOTTOM;
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 + foldLineViewHeight - foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from LEFT_TOP to LEFT_BOTTOM", tag.text));
                        }
                    }
                    // LEFT_TOP
                    else
                    {
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 - foldLineViewHeight + foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("%s's direction is LEFT_TOP", tag.text));
                        }
                    }
                }
                break;
            }
            case LEFT_BOTTOM:
            {
                if(tag.x - tvContentWidth - foldLineViewWidth < 0)
                {
                    tvContentLp.leftMargin = tag.x + foldLineViewWidth - foldLineViewCircleRadius;
                    tvContent.setBackgroundResource(R.drawable.icon_tag_right);

                    // LEFT_BOTTOM to RIGHT_TOP
                    if(tag.y - tvContentHeight/2 + foldLineViewHeight > containerHeight)
                    {
                        tag.contentDirection = TagItem.ContentDirection.LEFT_TOP;
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 - foldLineViewHeight + foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from LEFT_BOTTOM to RIGHT_TOP", tag.text));
                        }
                    }
                    // LEFT_BOTTOM to RIGHT_BOTTOM
                    else
                    {
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 + foldLineViewHeight - foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from LEFT_BOTTOM to RIGHT_BOTTOM", tag.text));
                        }
                    }
                }
                else
                {
                    tvContentLp.leftMargin = tag.x - tvContentWidth - foldLineViewWidth;
                    tvContent.setBackgroundResource(R.drawable.icon_tag_left);

                    // LEFT_BOTTOM to LEFT_TOP
                    if(tag.y - tvContentHeight/2 + foldLineViewHeight > containerHeight)
                    {
                        tag.contentDirection = TagItem.ContentDirection.LEFT_TOP;
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 - foldLineViewHeight + foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from LEFT_BOTTOM to LEFT_TOP", tag.text));
                        }
                    }
                    // LEFT_BOTTOM
                    else
                    {
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 + foldLineViewHeight - foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("%s's direction is LEFT_BOTTOM", tag.text));
                        }
                    }
                }
                break;
            }
            case RIGHT_TOP:
            {
                if(tag.x + tvContentWidth + foldLineViewWidth > containerWidth)
                {
                    tvContentLp.leftMargin = tag.x - tvContentWidth - foldLineViewWidth;
                    tvContent.setBackgroundResource(R.drawable.icon_tag_left);

                    // RIGHT_TOP to LEFT_BOTTOM
                    if(tag.y - tvContentHeight/2 - foldLineViewHeight < 0)
                    {
                        tag.contentDirection = TagItem.ContentDirection.LEFT_BOTTOM;
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 + foldLineViewHeight - foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from RIGHT_TOP to LEFT_BOTTOM", tag.text));
                        }
                    }
                    // RIGHT_TOP to LEFT_TOP
                    else
                    {
                        tag.contentDirection = TagItem.ContentDirection.LEFT_TOP;
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 - foldLineViewHeight + foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from RIGHT_TOP to LEFT_TOP", tag.text));
                        }
                    }
                }
                else
                {
                    tvContentLp.leftMargin = tag.x + foldLineViewWidth - foldLineViewCircleRadius;
                    tvContent.setBackgroundResource(R.drawable.icon_tag_right);

                    // RIGHT_TOP to RIGHT_BOTTOM
                    if(tag.y - tvContentHeight/2 - foldLineViewHeight < 0)
                    {
                        tag.contentDirection = TagItem.ContentDirection.RIGHT_BOTTOM;
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 + foldLineViewHeight - foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from RIGHT_TOP to RIGHT_BOTTOM", tag.text));
                        }
                    }
                    // RIGHT_TOP
                    else
                    {
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 - foldLineViewHeight + foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("%s's direction is RIGHT_TOP", tag.text));
                        }
                    }
                }
                break;
            }
            case RIGHT_BOTTOM:
            {
                if(tag.x + tvContentWidth + foldLineViewWidth > containerWidth)
                {
                    tvContentLp.leftMargin = tag.x - tvContentWidth - foldLineViewWidth;
                    tvContent.setBackgroundResource(R.drawable.icon_tag_left);

                    // RIGHT_BOTTOM to LEFT_TOP
                    if(tag.y - tvContentHeight/2 + foldLineViewHeight > containerHeight)
                    {
                        tag.contentDirection = TagItem.ContentDirection.LEFT_TOP;
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 - foldLineViewHeight + foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from RIGHT_BOTTOM to LEFT_TOP", tag.text));
                        }
                    }
                    // RIGHT_BOTTOM to LEFT_BOTTOM
                    else
                    {
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 + foldLineViewHeight - foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from RIGHT_BOTTOM to LEFT_BOTTOM", tag.text));
                        }
                    }
                }
                else
                {
                    tvContentLp.leftMargin = tag.x + foldLineViewWidth - foldLineViewCircleRadius;
                    tvContent.setBackgroundResource(R.drawable.icon_tag_right);

                    // RIGHT_BOTTOM to RIGHT_TOP
                    if(tag.y - tvContentHeight/2 + foldLineViewHeight > containerHeight)
                    {
                        tag.contentDirection = TagItem.ContentDirection.RIGHT_TOP;
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 - foldLineViewHeight + foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from RIGHT_BOTTOM to RIGHT_TOP", tag.text));
                        }
                    }
                    // RIGHT_BOTTOM
                    else
                    {
                        tvContentLp.topMargin = tag.y - tvContentHeight/2 + foldLineViewHeight - foldLineViewCircleRadius;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("%s's direction is RIGHT_BOTTOM", tag.text));
                        }
                    }
                }
                break;
            }
        }
    }


    // adjust fold line view's direction
    private void adjustFoldLineViewDirection(TagItem tag,
                                             RelativeLayout.LayoutParams foldLineViewLp)
    {
        switch (tag.contentDirection)
        {
            case LEFT_TOP:
            {
                foldLineViewLp.leftMargin = tag.x - foldLineViewLp.width;
                foldLineViewLp.topMargin = tag.y - foldLineViewLp.height;
                break;
            }
            case LEFT_BOTTOM:
            {
                foldLineViewLp.leftMargin = tag.x - foldLineViewLp.width;
                foldLineViewLp.topMargin = tag.y;
                break;
            }
            case RIGHT_TOP:
            {
                foldLineViewLp.leftMargin = tag.x;
                foldLineViewLp.topMargin = tag.y - foldLineViewLp.height;
                break;
            }
            case RIGHT_BOTTOM:
            {
                foldLineViewLp.leftMargin = tag.x;
                foldLineViewLp.topMargin = tag.y;
                break;
            }
        }
    }

    public void showTags()
    {
        if(isTagsVisible)
        {
            return ;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        for(TagView tagView: tagViews)
        {
            ViewHelper.setAlpha(tagView.indicatorPoint, 1);
            ViewHelper.setAlpha(tagView.animatorPoint, 1);
            ViewHelper.setAlpha(tagView.foldLineView, 1);
            int appearDirection;
            if(tagView.tag.animationDirection == TagItem.AnimationDirection.UP)
            {
                appearDirection = -1;
            }
            else
            {
                appearDirection = 1;
            }
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(tagView.tvContent, "translationY", appearDirection * DensityHelper.dp2px(10), 0),
                    ObjectAnimator.ofFloat(tagView.tvContent, "alpha", 0, 1),
                    ObjectAnimator.ofFloat(tagView.foldLineView, "alpha", 0, 1));
        }
        animatorSet.setDuration(300);
        animatorSet.start();
        startAnimation();
        isTagsVisible = true;
    }

    public void hideTags()
    {
        if(!isTagsVisible)
        {
            return ;
        }
        stopAnimation();
        AnimatorSet animatorSet = new AnimatorSet();
        for(TagView tagView: tagViews)
        {
//            tagView.indicatorPoint.setVisibility(View.GONE);
//            tagView.animatorPoint.setVisibility(View.GONE);
//            tagView.tvContent.setVisibility(View.GONE);
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(tagView.indicatorPoint, "alpha", 1, 0),
                    ObjectAnimator.ofFloat(tagView.animatorPoint, "alpha", 1, 0),
                    ObjectAnimator.ofFloat(tagView.tvContent, "alpha", 1, 0),
                    ObjectAnimator.ofFloat(tagView.foldLineView, "alpha", 0, 1));
        }
        animatorSet.setDuration(300);
        animatorSet.start();
        isTagsVisible = false;
    }

    public void startAnimation()
    {
        if(isAnimationPlaying)
        {
            return ;
        }
        animatorSet.start();
        isAnimationPlaying = true;
    }

    public void stopAnimation()
    {
        if(!isAnimationPlaying)
        {
            return ;
        }
        animatorSet.cancel();
        isAnimationPlaying = false;
    }


    /**
     * Tag View
     */
    public class TagView
    {
        public View indicatorPoint;
        public View animatorPoint;
        public TextView tvContent;
        public FoldLineView foldLineView;
        public TagItem tag;

        public TagView(View indicatorPoint, View animatorPoint, TextView tvContent, FoldLineView foldLineView, TagItem tag)
        {
            this.indicatorPoint = indicatorPoint;
            this.animatorPoint = animatorPoint;
            this.tvContent = tvContent;
            this.foldLineView = foldLineView;
            this.tag = tag;
        }
    }


    /**
     * tag on click listener
     */
    public interface OnTagClickListener
    {
        void onTagClick(TagView tagView);
    }
    public void setOnTagClickListener(final OnTagClickListener onTagClickListener)
    {
        View.OnClickListener onClickListener = new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(onTagClickListener != null)
                {
                    onTagClickListener.onTagClick((TagView)(view.getTag()));
                }
            }
        };
        for(TagView tagView: tagViews)
        {
            tagView.tvContent.setOnClickListener(onClickListener);
        }
    }
}
