package rahbari.erfan.mosito.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import rahbari.erfan.mosito.Application;
import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.interfaces.FragmentNavigation;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements FragmentNavigation {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(Application.theme);
        AppCompatDelegate.setDefaultNightMode(Application.theme == R.style.AppTheme_Dark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
    }

    protected void toggleTheme() {
        Application.theme = Application.theme == R.style.AppTheme_Dark ? R.style.AppTheme_Light : R.style.AppTheme_Dark;
        setTheme(Application.theme);
        AppCompatDelegate.setDefaultNightMode(Application.theme == R.style.AppTheme_Dark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        Utils.setShare("theme", String.valueOf(Application.theme));
    }

    @Override
    public void replaceFragment(Fragment current, Fragment replace)
    {
        if (current.getView() == null) return;
        getSupportFragmentManager().beginTransaction().replace(((ViewGroup) current.getView().getParent()).getId(), replace).addToBackStack("").commit();
    }

    @Override
    public void addFragment(Fragment context, Fragment fragment) {
        if (context.getView() == null) return;
        getSupportFragmentManager().beginTransaction().add(((ViewGroup) context.getView().getParent()).getId(), fragment).addToBackStack("").commit();
    }

    @Override
    public void closeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}