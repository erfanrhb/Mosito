package rahbari.erfan.mosito.interfaces;

import androidx.fragment.app.Fragment;

public interface FragmentNavigation {
    void addFragment(Fragment context, Fragment fragment);

    void closeFragment(Fragment fragment);

    void replaceFragment(Fragment current, Fragment replace);
}
