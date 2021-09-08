package rahbari.erfan.mosito.ui.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.Timer;
import java.util.TimerTask;

import eightbitlab.com.blurview.RenderScriptBlur;
import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.DialogToastBinding;
import rahbari.erfan.mosito.utils.FragmentDialogUtil;

public class ToastDialog extends FragmentDialogUtil<DialogToastBinding> {
    private static final String MESSAGE = "message";
    private static final String DRAWABLE = "drawable";

    private Timer timer;
    private String message;
    @DrawableRes
    private int drawableId;

    public static ToastDialog newInstance(String message) {
        ToastDialog fragment = new ToastDialog();
        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    public static ToastDialog newInstance(String message, @DrawableRes int drawableId) {
        ToastDialog fragment = new ToastDialog();
        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        args.putInt(DRAWABLE, drawableId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            message = getArguments().getString(MESSAGE);
            drawableId = getArguments().getInt(DRAWABLE);
        }
    }

    @Override
    public void onViewCreate() {
        View decorView = activity.getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();
        binding.blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(activity))
                .setBlurRadius(10f)
                .setHasFixedTransformationMatrix(true);

        if (message != null) {
            binding.tvMessage.setText(message);
        }

        if (drawableId != 0) {
            binding.tvMessage.setCompoundDrawables(null, ContextCompat.getDrawable(activity, drawableId), null, null);
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(() -> dismiss());
                timer.cancel();
                timer.purge();
                timer = null;
            }
        }, 2000, 3000);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_toast;
    }
}
