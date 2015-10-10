package ntu.zss.tagviewlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;

/**
 * Helper to add tags on a view.
 */
public class TagViewLayout extends RelativeLayout
{
    private static final String TAG = "TagViewLayout";
    private static final int ANIMATION_DURATION = 1500;
    private static final float ANIMATION_SCALE_RATIO = 3f;

    private ArrayList<TagItem> tags;
    private ArrayList<TagView> tagViews = null;
    private boolean isTagsChanged = false;
    private AnimatorSet animatorSet = null;

    private boolean isTagsVisible = false;
    private boolean isAnimationPlaying = false;


    public TagViewLayout(Context context)
    {
        super(context);
        this.setClipChildren(false);
    }

    public TagViewLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.setClipChildren(false);
    }

    public TagViewLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.setClipChildren(false);
    }

    @TargetApi(23)
    public TagViewLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setClipChildren(false);
    }

    public boolean isTagsVisible()
    {
        return isTagsVisible;
    }

    public void setTags(ArrayList<TagItem> tags)
    {
        this.tags = tags;
        if(tagViews != null && tagViews.size() != 0)
        {
            removeTagViews();
        }
        isTagsChanged = true;
        //displayTags();
    }

    public void displayTags()
    {
        if(!isTagsChanged)
        {
            return ;
        }
        isTagsChanged = false;
        /**
         * 1. init tagViews and animatorSet
         */
        // init tagViews
        if(tagViews == null)
        {
            tagViews = new ArrayList<>();
        }

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
        int tagCount = tags.size();
        int tagViewCount = tagViews.size();
        for(int i = 0; i < tagCount; i++)
        {
            TagItem tag = tags.get(i);
            TagView tagView;
            if(i >= tagViewCount)
            {
                // create a tagView
                tagView = new TagView(getContext());
                tagViews.add(tagView);
            }
            else
            {
                tagView = tagViews.get(i);
            }
            tagView.setTag(tag);
            tagView.displayTag();
            LayoutParams tagViewLp = new LayoutParams(tagView.getTagViewWidth(), tagView.getTagViewHeight());
            adjustDirection(tag, tagViewLp);
            tagView.adjustDirection();
            this.addView(tagView, tagViewLp);

            // add animations
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(tagView.animatorPoint, "Alpha", 1, 0);
            alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
            alphaAnimator.setRepeatMode(ValueAnimator.RESTART);
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(tagView.animatorPoint, "scaleX", 1, ANIMATION_SCALE_RATIO);
            scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
            scaleXAnimator.setRepeatMode(ValueAnimator.RESTART);
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(tagView.animatorPoint, "scaleY", 1, ANIMATION_SCALE_RATIO);
            scaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);
            scaleYAnimator.setRepeatMode(ValueAnimator.RESTART);
            animatorSet.playTogether(alphaAnimator,
                    scaleXAnimator,
                    scaleYAnimator);

//            View testView = new View(getContext());
//            testView.setBackgroundColor(0xffff0000);
//            LayoutParams testViewLp = new LayoutParams(50, 50);
//            testViewLp.leftMargin = tag.x;
//            testViewLp.topMargin = tag.y;
//            this.addView(testView, testViewLp);
        }

        /**
         * 3. show tags
         */
        animatorSet.setDuration(ANIMATION_DURATION);
        showTags();
    }

    // adjust tag view's direction
    private void adjustDirection(TagItem tag, LayoutParams tagViewLp)
    {
        int tagViewWidth = tagViewLp.width;
        int tagViewHeight = tagViewLp.height;
        int containerWidth = this.getMeasuredWidth();
        int containerHeight = this.getMeasuredHeight();
        final int outerIndicatorPointRadius = FoldLineView.OUTER_INDICATOR_POINT_RADIUS;
//        if(BuildConfig.DEBUG)
//        {
//            Log.e(TAG, String.format("tagView: (%d, %d)", tagViewWidth, tagViewHeight));
//        }

        // bound detection
        boolean willClipLeftBound = tag.x - tagViewWidth + outerIndicatorPointRadius < 0;
        boolean willClipRightBound = tag.x + tagViewWidth - outerIndicatorPointRadius > containerWidth;
        boolean willClipTopBound = tag.y - tagViewHeight + outerIndicatorPointRadius < 0;
        boolean willClipBottomBound = tag.y + tagViewHeight - outerIndicatorPointRadius > containerHeight;

        int rightDirectionLeftMargin =  tag.x - outerIndicatorPointRadius;
        int leftDirectionLeftMargin =  tag.x - tagViewWidth + outerIndicatorPointRadius;
        int bottomDirectionTopMargin = tag.y - outerIndicatorPointRadius;
        int topDirectionTopMargin = tag.y - tagViewHeight + outerIndicatorPointRadius;

        switch (tag.direction)
        {
            case UNDEFINED:
            {
                // define direction
                if(tag.x - outerIndicatorPointRadius < containerWidth/2)
                {
                    tag.direction = TagItem.Direction.LEFT_TOP;
                }
                else
                {
                    tag.direction = TagItem.Direction.RIGHT_BOTTOM;
                }
            }
            // origin is LEFT_TOP
            case LEFT_TOP:
            {
                if(willClipLeftBound)
                {
                    tagViewLp.leftMargin = rightDirectionLeftMargin;
                    // LEFT_TOP to RIGHT_BOTTOM
                    if(willClipTopBound)
                    {
                        tagViewLp.topMargin = bottomDirectionTopMargin;
                        tag.direction = TagItem.Direction.RIGHT_BOTTOM;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from LEFT_TOP to RIGHT_BOTTOM", tag.text));
                        }
                    }
                    // LEFT_TOP to RIGHT_TOP
                    else
                    {
                        tagViewLp.topMargin = topDirectionTopMargin;
                        tag.direction = TagItem.Direction.RIGHT_TOP;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from LEFT_TOP to RIGHT_TOP", tag.text));
                        }
                    }
                }
                else
                {
                    tagViewLp.leftMargin = leftDirectionLeftMargin;
                    if(willClipTopBound)
                    {
                        tagViewLp.topMargin = bottomDirectionTopMargin;
                        tag.direction = TagItem.Direction.LEFT_BOTTOM;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from LEFT_TOP to LEFT_BOTTOM", tag.text));
                        }
                    }
                    else
                    {
                        tagViewLp.topMargin = tag.y- tagViewHeight;
                        tag.direction = TagItem.Direction.LEFT_TOP;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("%s's direction is LEFT_TOP", tag.text));
                        }
                    }
                }
                break;
            }

            // origin is LEFT_BOTTOM
            case LEFT_BOTTOM:
            {
                if(willClipLeftBound)
                {
                    tagViewLp.leftMargin = rightDirectionLeftMargin;
                    // LEFT_BOTTOM to RIGHT_TOP
                    if(willClipBottomBound)
                    {
                        tagViewLp.topMargin = topDirectionTopMargin;
                        tag.direction = TagItem.Direction.RIGHT_TOP;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from LEFT_BOTTOM to RIGHT_TOP", tag.text));
                        }
                    }
                    // LEFT_TOP to RIGHT_BOTTOM
                    else
                    {
                        tagViewLp.topMargin = bottomDirectionTopMargin;
                        tag.direction = TagItem.Direction.RIGHT_BOTTOM;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from LEFT_BOTTOM to RIGHT_BOTTOM", tag.text));
                        }
                    }
                }
                else
                {
                    tagViewLp.leftMargin = leftDirectionLeftMargin;
                    // LEFT_BOTTOM to LEFT_TOP
                    if(willClipBottomBound)
                    {
                        tagViewLp.topMargin = topDirectionTopMargin;
                        tag.direction = TagItem.Direction.LEFT_TOP;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from LEFT_BOTTOM to LEFT_TOP", tag.text));
                        }
                    }
                    // LEFT_BOTTOM
                    else
                    {
                        tagViewLp.topMargin = bottomDirectionTopMargin;
                        tag.direction = TagItem.Direction.LEFT_BOTTOM;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("%s's direction is LEFT_BOTTOM", tag.text));
                        }
                    }
                }
                break;
            }

            // origin is RIGHT_TOP
            case RIGHT_TOP:
            {
                if(willClipRightBound)
                {
                    tagViewLp.leftMargin = leftDirectionLeftMargin;
                    // RIGHT_TOP to LEFT_BOTTOM
                    if(willClipTopBound)
                    {
                        tagViewLp.topMargin = bottomDirectionTopMargin;
                        tag.direction = TagItem.Direction.LEFT_BOTTOM;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from RIGHT_TOP to LEFT_BOTTOM", tag.text));
                        }
                    }
                    // RIGHT_TOP to LEFT_TOP
                    else
                    {
                        tagViewLp.topMargin = tag.y- tagViewHeight;
                        tag.direction = TagItem.Direction.LEFT_TOP;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from RIGHT_TOP to LEFT_TOP", tag.text));
                        }
                    }
                }
                else
                {
                    tagViewLp.leftMargin = rightDirectionLeftMargin;
                    if(willClipTopBound)
                    {
                        tagViewLp.topMargin = bottomDirectionTopMargin;
                        tag.direction = TagItem.Direction.RIGHT_BOTTOM;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from RIGHT_TOP to RIGHT_BOTTOM", tag.text));
                        }
                    }
                    else
                    {
                        tagViewLp.topMargin = tag.y- tagViewHeight;
                        tag.direction = TagItem.Direction.RIGHT_TOP;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("%s's direction is RIGHT_TOP", tag.text));
                        }
                    }
                }
                break;
            }

            // origin is RIGHT_BOTTOM
            case RIGHT_BOTTOM:
            {
                if(willClipRightBound)
                {
                    tagViewLp.leftMargin = leftDirectionLeftMargin;
                    // RIGHT_BOTTOM to LEFT_TOP
                    if(willClipTopBound)
                    {
                        tagViewLp.topMargin = topDirectionTopMargin;
                        tag.direction = TagItem.Direction.LEFT_TOP;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from RIGHT_BOTTOM to LEFT_TOP", tag.text));
                        }
                    }
                    // RIGHT_BOTTOM to LEFT_BOTTOM
                    else
                    {
                        tagViewLp.topMargin = bottomDirectionTopMargin;
                        tag.direction = TagItem.Direction.LEFT_BOTTOM;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from RIGHT_BOTTOM to LEFT_BOTTOM", tag.text));
                        }
                    }
                }
                else
                {
                    tagViewLp.leftMargin = rightDirectionLeftMargin;
                    // RIGHT_BOTTOM to RIGHT_TOP
                    if(willClipBottomBound)
                    {
                        tagViewLp.topMargin = topDirectionTopMargin;
                        tag.direction = TagItem.Direction.RIGHT_TOP;
                        if(BuildConfig.DEBUG)
                        {
                            Log.e(TAG, String.format("change %s's direction from RIGHT_BOTTOM to RIGHT_TOP", tag.text));
                        }
                    }
                    else
                    {
                        tagViewLp.topMargin = bottomDirectionTopMargin;
                        tag.direction = TagItem.Direction.RIGHT_BOTTOM;
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

    private void removeTagViews()
    {
        this.removeAllViews();
        isAnimationPlaying = false;
        isTagsVisible = false;
    }

    public void showTags()
    {
        if(isTagsVisible)
        {
            return ;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        int tagCount = tags.size();
        for(int i = 0; i < tagCount; i++)
        {
            TagView tagView = tagViews.get(i);
            ViewHelper.setAlpha(tagView, 1);
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(tagView, "alpha", 0, 1));
        }
        animatorSet.setDuration(500);
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
        int tagCount = tags.size();
        for(int i = 0; i < tagCount; i++)
        {
            TagView tagView = tagViews.get(i);
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(tagView, "alpha", 1, 0));
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
     * tag on click listener
     */
    public interface OnTagClickListener
    {
        void onTagClick(TagView tagView);
    }
    public void setOnTagClickListener(final OnTagClickListener onTagClickListener)
    {
        OnClickListener onClickListener = new OnClickListener()
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
        int tagCount = tags.size();
        for(int i = 0; i < tagCount; i++)
        {
            TagView tagView = tagViews.get(i);
            if(tagView != null && tagView.tvContent != null)
            {
                tagView.tvContent.setOnClickListener(onClickListener);
            }
        }
    }
}
