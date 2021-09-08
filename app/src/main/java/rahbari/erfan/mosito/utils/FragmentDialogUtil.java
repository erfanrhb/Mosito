package rahbari.erfan.mosito.utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.Nullable;

public abstract class FragmentDialogUtil<B extends ViewDataBinding> extends DialogFragment {
    protected B binding;
    protected Activity activity;

    public abstract void onViewCreate();

    public abstract int getLayoutId();

    public FragmentDialogUtil() {
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        onViewCreate();
        return binding.getRoot();
    }
}
