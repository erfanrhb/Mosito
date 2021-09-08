package rahbari.erfan.mosito;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import rahbari.erfan.mosito.models.Playing;
import rahbari.erfan.mosito.utils.Utils;

public class Application extends MultiDexApplication {

    private static Application instance;
    public static String api_token;
    public static int theme;
    public static String mosito_domain;
    public static String mosito_storage;

    public static Playing playing;

    static {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public static Application getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        api_token = Utils.getShare("token");
        theme = Integer.parseInt(Utils.getShare("theme", String.valueOf(R.style.AppTheme_Dark)));

        playing = new Playing();

//        mosito_domain = Utils.getShare("mosito_domain", "mosito.ir");
//        mosito_storage = Utils.getShare("mosito_storage", "s1.mosito.ir");

        mosito_domain = Utils.getShare("mosito_domain", "podito.ir");
        mosito_storage = Utils.getShare("mosito_storage", "s1.podito.ir");
    }
    
}