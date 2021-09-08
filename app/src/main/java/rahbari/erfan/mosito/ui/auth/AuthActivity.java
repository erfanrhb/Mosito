package rahbari.erfan.mosito.ui.auth;

import android.app.Activity;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.ActivityAuthBinding;
import rahbari.erfan.mosito.resources.Repositories.UserRep;
import rahbari.erfan.mosito.utils.BaseActivity;

public class AuthActivity extends BaseActivity implements
        FragmentManager.OnBackStackChangedListener {
    String route;
    UserRep userRep;
    Activity activity;
    ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = AuthActivity.this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_auth);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setOverflowIcon(ContextCompat.getDrawable(activity, R.drawable.ic_more_vert));
        setTitle("");

        userRep = new UserRep();

        route = "";

        userRep.userLiveData().observe(this, user -> {
            if (user != null && !route.equals("profile")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.authFrameLayout, new ProfileFragment()).commit();
                route = "profile";
            } else if (!route.equals("login")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.authFrameLayout, new LoginFragment()).commit();
                route = "login";
            }
        });

        binding.imgBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackStackChanged() {
        getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1);
    }
}