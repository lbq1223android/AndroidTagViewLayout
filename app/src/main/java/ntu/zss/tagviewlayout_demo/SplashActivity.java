package ntu.zss.tagviewlayout_demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import ntu.zss.tagviewlayout.DensityHelper;
import zss.ntu.com.demo5_tagview.R;

public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DensityHelper.init(this);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                if(android.os.Build.VERSION.SDK_INT >= 11)
                {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
                startActivity(intent);
            }
        }, 500);
    }
}
