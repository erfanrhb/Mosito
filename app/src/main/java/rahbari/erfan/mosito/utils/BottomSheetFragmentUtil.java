package rahbari.erfan.mosito.utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public abstract class BottomSheetFragmentUtil<B extends ViewDataBinding> extends BottomSheetDialogFragment {
    protected B binding;
    protected Activity activity;

    public abstract void onViewCreate();

    public abstract int getLayoutId();

    public BottomSheetFragmentUtil() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
