package com.vx.dyvide.controller.onboarding;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.vx.dyvide.R;
import com.vx.dyvide.model.DB.ObjectBox;
import com.vx.dyvide.model.DB.SavedConfig;


public class OnboardingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private OnboardingAdapter onboardingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        makeStatusbarTransparent();

        viewPager = findViewById(R.id.onboarding_view_pager);

        onboardingAdapter = new OnboardingAdapter(this);
        viewPager.setAdapter(onboardingAdapter);
        viewPager.setPageTransformer(false, new OnboardingPageTransformer());


    }


    public void nextPage(View view) {
        if (view.getId() == R.id.button2) {
            if (viewPager.getCurrentItem() < onboardingAdapter.getCount() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }else if(((Button) view).getText().equals("Finish")){
                SavedConfig c = ObjectBox.get().boxFor(SavedConfig.class).get(1);
                c.setOnboard(1);
                ObjectBox.get().boxFor(SavedConfig.class).put(c);
                finish();
            }
        }
    }

    private void makeStatusbarTransparent() {

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
