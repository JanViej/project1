/*
package com.example.ggmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.IMainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class NavigateMain extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private InforFragment inforFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate_main);

        viewPager = findViewById(R.id.view_paper);
        tabLayout = findViewById(R.id.tab_layout);

        MainActivity.AuthenticationPagerAdapter pagerAdapter = new NavigateMain().AuthenticationPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragmet(new InforFragment());
        pagerAdapter.addFragmet(new MapFragment());
        viewPager.setAdapter(pagerAdapter);
    }

    private MainActivity.AuthenticationPagerAdapter AuthenticationPagerAdapter(FragmentManager supportFragmentManager, int behaviorResumeOnlyCurrentFragment) {
        ArrayList<Fragment> fragmentList = new ArrayList<>();

        public AuthenticationPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragmet(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }
}*/

package com.example.ggmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class NavigateMain extends AppCompatActivity{

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private InforFragment inforFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate_main);

        ViewPager viewPager = findViewById(R.id.view_paper);
        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(viewPager);

        AuthenticationPagerAdapter pagerAdapter = new AuthenticationPagerAdapter(getSupportFragmentManager(),0);
        pagerAdapter.addFragmet(new MapFragment(),"Get Direction");
        pagerAdapter.addFragmet(new InforFragment(), "My profile");
        viewPager.setAdapter(pagerAdapter);


        tabLayout.getTabAt(0).setIcon(R.drawable.marker24);
        tabLayout.getTabAt(1).setIcon(R.drawable.profile24);
    }

    class AuthenticationPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();
        private ArrayList<String> fragmentTitle = new ArrayList<>();

        public AuthenticationPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragmet(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitle.add(title);
        }
    }
}

