package com.jhonlopera.nerd30;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class ConfigActivity extends PrincipalActivity implements NavigationView.OnNavigationItemSelectedListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_config);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.frameprincipal);
        getLayoutInflater().inflate(R.layout.activity_config, contentFrameLayout);

        ft.remove(fragment1); //se remueve el fragment que se inicia por defecto en el oncreate de principal
        getSupportActionBar().setTitle("Configuración");

        mSectionsPagerAdapter = new ConfigActivity.SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return (new ConfigSoundFragment());
                case 1:
                    return (new ConfigProfileFragment());
                /*case 2:
                    return (new CreditosCPULFragment());*/
                default:return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Activar sonido";
                case 1:
                    return "Modificar datos perfil";
                /*case 2:
                    return "4 palabras 1 letra";*/
            }
            return null;
        }
    }
}
