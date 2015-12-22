package com.sgen.tayobell;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
    // Fragment TabHost as mTabHost
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        final View tabIndicator1 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, mTabHost.getTabWidget(), false);
        ((TextView) tabIndicator1.findViewById(R.id.tabtitle)).setText("주변정류장");
        ((ImageView) tabIndicator1.findViewById(R.id.tabicon)).setImageResource(R.drawable.ic_pause_light);

        final View tabIndicator2 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, mTabHost.getTabWidget(), false);
        ((TextView) tabIndicator2.findViewById(R.id.tabtitle)).setText("검색");
        ((ImageView) tabIndicator2.findViewById(R.id.tabicon)).setImageResource(R.drawable.ic_setting_light);

        final View tabIndicator3 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, mTabHost.getTabWidget(), false);
        ((TextView) tabIndicator3.findViewById(R.id.tabtitle)).setText("즐겨찾기");
        ((ImageView) tabIndicator3.findViewById(R.id.tabicon)).setImageResource(R.drawable.ic_setting_light);

        final View tabIndicator4 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, mTabHost.getTabWidget(), false);
        ((TextView) tabIndicator4.findViewById(R.id.tabtitle)).setText("설정");
        ((ImageView) tabIndicator4.findViewById(R.id.tabicon)).setImageResource(R.drawable.ic_setting_light);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(tabIndicator1),
                FirstFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(tabIndicator2),
                SecondFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator(tabIndicator3),
                ThirdFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab4").setIndicator(tabIndicator4),
                SettingFragment.class, null);

        for (int tab = 0; tab < mTabHost.getTabWidget().getChildCount(); ++tab) {
            mTabHost.getTabWidget().getChildAt(tab).getLayoutParams().width = (this.getWindowManager().getDefaultDisplay().getWidth())/4;
        }

        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                ((ImageView) tabIndicator1.findViewById(R.id.tabicon)).setImageResource(R.drawable.ic_setting_light);
                ((ImageView) tabIndicator2.findViewById(R.id.tabicon)).setImageResource(R.drawable.ic_setting_light);
                ((ImageView) tabIndicator3.findViewById(R.id.tabicon)).setImageResource(R.drawable.ic_setting_light);
                ((ImageView) tabIndicator4.findViewById(R.id.tabicon)).setImageResource(R.drawable.ic_setting_light);
                switch (tabId){
                    case "tab1":
                        ((ImageView)tabIndicator1.findViewById(R.id.tabicon)).setImageResource(R.drawable.ic_pause_light);
                        break;
                    case "tab2":
                        ((ImageView)tabIndicator2.findViewById(R.id.tabicon)).setImageResource(R.drawable.ic_pause_light);
                        break;
                    case "tab3":
                        ((ImageView)tabIndicator3.findViewById(R.id.tabicon)).setImageResource(R.drawable.ic_pause_light);
                        break;
                    case "tab4":
                        ((ImageView)tabIndicator4.findViewById(R.id.tabicon)).setImageResource(R.drawable.ic_pause_light);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflater함수를 이용해서 menu 리소스를 menu로 변환.
        // 두 줄 코드
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}