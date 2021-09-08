package rahbari.erfan.mosito.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import rahbari.erfan.mosito.interfaces.FragmentNavigation;

public abstract class FragmentUtil<B extends ViewDataBinding> extends Fragment {
    protected B binding;
    protected Activity activity;
    protected FragmentNavigation navigation;

    public abstract void onViewCreate();

    public abstract int getLayoutId();

    public FragmentUtil() {
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

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            navigation = (FragmentNavigation) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigation = null;
    }
}
