package rahbari.erfan.mosito.ui.auth;

import android.os.Bundle;
import android.text.InputType;

import androidx.annotation.Nullable;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentProfileBinding;
import rahbari.erfan.mosito.resources.Repositories.UserRep;
import rahbari.erfan.mosito.utils.FragmentUtil;

public class ProfileFragment extends FragmentUtil<FragmentProfileBinding> {
    private UserRep userRep;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userRep = new UserRep();
    }

    @Override
    public void onViewCreate() {
        userRep.userLiveData().observe(this, user -> activity.runOnUiThread(() -> {

            binding.setUser(user);

            binding.edtEmail.setInputType(user.getEmail_verified_at() != null ? InputType.TYPE_NULL : InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile;
    }
}
