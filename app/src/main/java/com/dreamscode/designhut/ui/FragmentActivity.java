package com.dreamscode.designhut.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.dreamscode.designhut.R;
import com.dreamscode.designhut.main_fragments.AddNewFragment;
import com.dreamscode.designhut.main_fragments.HomeFragment;
import com.dreamscode.designhut.main_fragments.ProfileFragment;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class FragmentActivity extends AppCompatActivity {
    FrameLayout frame_container;
    MeowBottomNavigation bottom_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        frame_container = findViewById(R.id.frame_container);
        bottom_nav = findViewById(R.id.bottom_nav);

        bottom_nav.add(new MeowBottomNavigation.Model(1,R.drawable.ic_add));
        bottom_nav.add(new MeowBottomNavigation.Model(2,R.drawable.ic_home));
        bottom_nav.add(new MeowBottomNavigation.Model(3,R.drawable.ic_user));

        bottom_nav.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //actions

            }
        });

        bottom_nav.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //action


            }
        });

        bottom_nav.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                switch (item.getId()){
                    case 1:
                        fragment = new AddNewFragment();
                        break;
                    case  2:
                        fragment = new HomeFragment();
                        break;
                    case 3:
                        fragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).commit();
            }
        });

        bottom_nav.show(2,true);

    }
}