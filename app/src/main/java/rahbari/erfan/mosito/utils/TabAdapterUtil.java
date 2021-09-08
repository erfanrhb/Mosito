package rahbari.erfan.mosito.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabAdapterUtil extends FragmentStatePagerAdapter {
    private int behavior;
    private Fragment[] fragments;

    public TabAdapterUtil(@NonNull FragmentManager fm, int behavior, Fragment... fragments) {
        super(fm, behavior);
        this.behavior = behavior;
        this.fragments = fragments;
    }

    public void setItem(int position, Fragment fragment) {
        this.fragments[position] = fragment;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return behavior;
    }
}
