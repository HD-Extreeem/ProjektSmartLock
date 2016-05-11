package com.hellomicke89gmail.projektsmartlock;

        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;
        import android.support.v4.app.FragmentStatePagerAdapter;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by HadiDeknache on 16-04-25.
 */

 public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragments = new ArrayList<>();
    private final ArrayList<String> fragmentTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        fragmentTitles.add(title);
    }

    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }
}





    /*
     * Created by kundan on 10/16/2015.

    public class ViewPagerAdapter  extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment frag=null;
            switch (position){
                case 0:
                    frag=new idFragment();
                    break;
                case 1:
                    frag=new loggFragment();
                    break;
            }
            return frag;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title=" ";
            switch (position){
                case 0:
                    title="ID";
                    break;
                case 1:
                    title="LoggLista";
                    break;


            }

            return title;
        }
    }
*/