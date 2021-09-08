package rahbari.erfan.mosito.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentRegisterBinding;
import rahbari.erfan.mosito.resources.Repositories.UserRep;
import rahbari.erfan.mosito.utils.FragmentUtil;
import rahbari.erfan.mosito.utils.Utils;

public class RegisterFragment extends FragmentUtil<FragmentRegisterBinding> {
    private UserRep rep;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rep = new UserRep();
    }

    @Override
    public void onViewCreate() {
        binding.btnLogin.setOnClickListener(v -> navigation.addFragment(this, new LoginFragment()));

        binding.btnRegister.setOnClickListener(v -> {
            loading(true);
            rep.store(
                    Utils.text(binding.edtName), Utils.text(binding.edtEmail), Utils.text(binding.edtPassword), 1
                    , response -> Toast.makeText(activity, "Registered successfully", Toast.LENGTH_LONG).show(), response -> activity.runOnUiThread(() -> {
                        Toast.makeText(activity, response.getMessage(), Toast.LENGTH_LONG).show();
                        Utils.errorHandler(binding.edtName, response, "name");
                        Utils.errorHandler(binding.edtEmail, response, "email");
                        Utils.errorHandler(binding.edtPassword, response, "password");
                    }), response -> activity.runOnUiThread(() -> loading(false)));
        });
    }

    private void loading(boolean turn) {
        if (turn) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnRegister.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnRegister.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_register;
    }
}
