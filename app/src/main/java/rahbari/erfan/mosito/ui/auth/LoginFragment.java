package rahbari.erfan.mosito.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentLoginBinding;
import rahbari.erfan.mosito.resources.Repositories.UserRep;
import rahbari.erfan.mosito.utils.FragmentUtil;
import rahbari.erfan.mosito.utils.Utils;

public class LoginFragment extends FragmentUtil<FragmentLoginBinding> {
    private UserRep userRep;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userRep = new UserRep();
    }

    @Override
    public void onViewCreate() {
        binding.btnRegister.setOnClickListener(v -> navigation.addFragment(this, new RegisterFragment()));
        binding.btnForgetPassword.setOnClickListener(v -> navigation.addFragment(this, new ForgetPasswordFragment()));

        binding.btnLogin.setOnClickListener(v -> {
            loading(true);
            userRep.store("", Utils.text(binding.edtEmail), Utils.text(binding.edtPassword), 0, response -> Toast.makeText(activity, "Login Successful!", Toast.LENGTH_LONG).show(), response -> activity.runOnUiThread(() -> {
                Toast.makeText(activity, response.getMessage(), Toast.LENGTH_LONG).show();
                Utils.errorHandler(binding.edtEmail, response, "email");
                Utils.errorHandler(binding.edtPassword, response, "password");
            }), response -> activity.runOnUiThread(() -> loading(false)));
        });
    }

    private void loading(boolean turn) {
        if (turn) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnLogin.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }
}
