package rahbari.erfan.mosito.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import rahbari.erfan.mosito.Application;
import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.models.ErrorHandler;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.resources.BaseRetrofit;
import okhttp3.ResponseBody;

public class Utils {
    @BindingAdapter({"loadImageXs", "placeholder"})
    public static void loadImageXs(ImageView view, String url, Drawable placeholder) {
        Glide.with(view)
                .load(resize(pictureUrl(url), "100", "100", "70"))
                .placeholder(placeholder)
                .centerCrop()
                .into(view);
    }

    @BindingAdapter({"loadImage", "placeholder"})
    public static void loadImage(ImageView view, String url, Drawable placeholder) {
        Glide.with(view)
                .load(pictureUrl(url))
                .placeholder(placeholder)
                .centerCrop()
                .into(view);
    }

    @BindingAdapter({"activate"})
    public static void activate(MaterialButton view, Boolean activate) {
        Context context = view.getContext();

        TypedValue primaryValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, primaryValue, true);
        @ColorRes int primary = primaryValue.resourceId;
        @ColorRes int transparent = R.color.transparent;
        if (activate == null) {
            view.setBackgroundTintList(colorList(context, transparent));
            view.setTextColor(colorList(context, primary));
        } else if (activate) {
            view.setBackgroundTintList(colorList(context, primary));
            view.setTextColor(colorList(context, R.color.white));
        } else {
            view.setBackgroundTintList(colorList(context, transparent));
            view.setTextColor(colorList(context, primary));
        }
    }

    @Nullable
    public static String getShare(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Application.getInstance());
        return preferences.getString(key, null);
    }

    public static String getShare(String key, String def) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Application.getInstance());
        return preferences.getString(key, def);
    }

    public static void setShare(String key, String val) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Application.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static int attrColor(Activity activity, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    public static String text(TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString() : "";
    }

    public static String text(EditText editText) {
        return editText.getText() != null ? editText.getText().toString() : "";
    }

    public static void errorHandler(TextInputEditText editText, ErrorHandler handler, String key) {
        if (handler != null && handler.getErrors() != null && handler.getErrors().containsKey(key)) {
            List<String> errors = handler.getErrors().get(key);
            if (errors != null && errors.size() > 0) {
                editText.setError(errors.get(0));
            }
        }
    }

    public static ColorStateList colorList(Context context, @ColorRes int color) {
        return new ColorStateList(
                new int[][]{new int[]{android.R.attr.state_enabled}, new int[]{-android.R.attr.state_enabled}, new int[]{},},
                new int[]{ContextCompat.getColor(context, color), ContextCompat.getColor(context, color), R.color.white});
    }

    public static String resize(String path, String width, String height) {
        return resize(path, width, height, null, null);
    }

    public static String resize(String path, String width, String height, String quality) {
        return resize(path, width, height, quality, null);
    }

    public static String resize(String path, String width, String height, String quality, String crop) {
        if (path == null) return "";
        if (path.length() == 0) return "";
        String url = BaseRetrofit.BASE_DOWN_URL + "/timthumb.php?src=" + path + "&w=" + width + "&h=" + height;
        url += crop != null ? "&zc=" + crop : "&zc=1";
        url += quality != null ? "&q=" + quality + "&s=1" : "&q=95&s=1";
        return url;
    }

    @SuppressLint("SimpleDateFormat")
    public static String today() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        return df.format(new Date());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDay(int days) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        df.setTimeZone(TimeZone.getTimeZone("gmt"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, days);
        Date newDate = calendar.getTime();

        return df.format(newDate);
    }

    public static boolean fileExist(Music music) {
        return fileExist(music.localFileName());
    }

    public static boolean fileExist(String fileName) {
        File file = new File(Application.getInstance().getExternalFilesDir(Environment.DIRECTORY_MUSIC)
                + File.separator + fileName);
        return file.exists();
    }

    public static boolean fileSave(ResponseBody body, String fileName) {
        try {
            File file = new File(Application.getInstance().getExternalFilesDir(Environment.DIRECTORY_MUSIC)
                    + File.separator + fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("Utils", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {

                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static Rect locateView(View view) {
        Rect loc = new Rect();
        int[] location = new int[2];
        if (view == null) {
            return loc;
        }
        view.getLocationOnScreen(location);

        loc.left = location[0];
        loc.top = location[1];
        loc.right = loc.left + view.getWidth();
        loc.bottom = loc.top + view.getHeight();
        return loc;
    }

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static String minute(long second) {
        return second / 60 + ":" + second % 60;
    }

    public static String pictureUrl(String url) {
        if (url == null) return null;
        if (url.contains("http")) {
            return url;
        } else {
            return BaseRetrofit.BASE_DOWN_URL + url;
        }
    }
}
