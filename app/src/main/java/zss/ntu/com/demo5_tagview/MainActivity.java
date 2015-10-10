package zss.ntu.com.demo5_tagview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import ntu.zss.tagviewlayout.DensityHelper;
import ntu.zss.tagviewlayout.TagItem;
import ntu.zss.tagviewlayout.TagView;
import ntu.zss.tagviewlayout.TagViewLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private ArrayList<TagItem> tags;
    private TagViewLayout tagViewLayout;
    private Button btnShowTags;
    private Button btnHideTags;
    private Button btnStartAnimation;
    private Button btnStopAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tagViewLayout = (TagViewLayout)findViewById(R.id.tagViewLayout);
        ViewGroup.LayoutParams lp = tagViewLayout.getLayoutParams();
        lp.width = DensityHelper.screenWidth;
        lp.height = DensityHelper.dp2px(240);
        tagViewLayout.setLayoutParams(lp);

        btnShowTags = (Button)findViewById(R.id.btnShowTags);
        btnHideTags = (Button)findViewById(R.id.btnHideTags);
        btnStartAnimation = (Button)findViewById(R.id.btnStartAnimation);
        btnStopAnimation = (Button)findViewById(R.id.btnStopAnimation);
        btnShowTags.setOnClickListener(this);
        btnHideTags.setOnClickListener(this);
        btnStartAnimation.setOnClickListener(this);
        btnStopAnimation.setOnClickListener(this);

        initTags();
        tagViewLayout.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                displayTags();
            }
        }, 500);
    }

    private void initTags()
    {
        tags = new ArrayList<>();
        // add some tags for testing purpose
        tags.add(new TagItem(DensityHelper.dp2px(28), DensityHelper.dp2px(162),
                getString(R.string.tag_text_tag1), TagItem.Direction.RIGHT_TOP));
        tags.add(new TagItem(DensityHelper.dp2px(230), DensityHelper.dp2px(50),
                getString(R.string.tag_text_tag2), TagItem.Direction.LEFT_TOP));
        tags.add(new TagItem(DensityHelper.dp2px(180), DensityHelper.dp2px(160),
                getString(R.string.tag_text_tag3), TagItem.Direction.RIGHT_BOTTOM));
    }

    private void displayTags()
    {
        tagViewLayout.setTags(tags);
        tagViewLayout.displayTags();
        tagViewLayout.setOnTagClickListener(new TagViewLayout.OnTagClickListener()
        {
            @Override
            public void onTagClick(TagView tagView)
            {
                Toast.makeText(MainActivity.this, tagView.tag.text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        if(view == btnShowTags)
        {
            tagViewLayout.showTags();
        }
        else if(view == btnHideTags)
        {
            tagViewLayout.hideTags();
        }
        else if(view == btnStartAnimation)
        {
            tagViewLayout.startAnimation();
        }
        else if(view == btnStopAnimation)
        {
            tagViewLayout.stopAnimation();
        }
    }
}
