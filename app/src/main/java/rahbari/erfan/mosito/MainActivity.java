package rahbari.erfan.mosito;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rahbari.erfan.mosito.databinding.ActivityMainBinding;
import rahbari.erfan.mosito.resources.Repositories.UserRep;
import rahbari.erfan.mosito.services.DownloadService;
import rahbari.erfan.mosito.services.NotificationService;
import rahbari.erfan.mosito.services.PlayService;
import rahbari.erfan.mosito.services.SyncService;
import rahbari.erfan.mosito.ui.auth.AuthActivity;
import rahbari.erfan.mosito.ui.browse.BrowseFragment;
import rahbari.erfan.mosito.ui.dialogs.DownloadFragment;
import rahbari.erfan.mosito.ui.dialogs.ToastDialog;
import rahbari.erfan.mosito.ui.library.LibraryFragment;
import rahbari.erfan.mosito.ui.player.PlayerFragment;
import rahbari.erfan.mosito.ui.search.SearchFragment;
import rahbari.erfan.mosito.utils.BaseActivity;
import rahbari.erfan.mosito.utils.Utils;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements
        FragmentManager.OnBackStackChangedListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        EasyPermissions.PermissionCallbacks {

    String[] perms;
    UserRep userRep;
    private static final int REQUEST_CODE = 396;
    Activity activity;
    ActivityMainBinding binding;
    DownloadFragment downloadFragment;
    BroadcastReceiver toastDialogReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = MainActivity.this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setOverflowIcon(ContextCompat.getDrawable(activity, R.drawable.ic_more_vert));
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
        setTitle("");


        this.perms = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        this.userRep = new UserRep();

        getSupportFragmentManager().beginTransaction().replace(R.id.framePlayer, new PlayerFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLibrary, new LibraryFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameBrowse, new BrowseFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameSearch, new SearchFragment()).commit();

        toastDialogReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                int drawableId = intent.getIntExtra("drawableId", 0);
                ToastDialog dialog = ToastDialog.newInstance(message, drawableId);
                dialog.show(getSupportFragmentManager(), "");
            }
        };

        startService(new Intent(activity, DownloadService.class));
        startService(new Intent(activity, PlayService.class));
        startService(new Intent(activity, SyncService.class));
        startService(new Intent(activity, NotificationService.class));

        binding.imgProfile.setOnClickListener(v -> startActivity(new Intent(activity, AuthActivity.class)));

        userRep.userLiveData().observe(this, user -> {
            if (user != null && user.getPicture() != null) {
                binding.setUserPicture(user.getPicture());
            }
        });

        downloadFragment = new DownloadFragment();
        binding.imgDownload.setOnClickListener(v -> downloadFragment.show(getSupportFragmentManager(), ""));

        registerRemoteConfig();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_night:
                toggleTheme();
                return true;
            case R.id.action_logout:
                userRep.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_library:
                binding.viewFlipper.setDisplayedChild(0);
                return true;
            case R.id.action_browse:
                binding.viewFlipper.setDisplayedChild(1);
                return true;
//            case R.id.action_for_you:
//                binding.viewFlipper.setDisplayedChild(2);
//                return true;
            case R.id.action_search:
                binding.viewFlipper.setDisplayedChild(3);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackStackChanged() {
        Fragment fragment = getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1);

        if (fragment == null || fragment.getView() == null) return;
        switch (((ViewGroup) fragment.getView().getParent()).getId()) {
            case R.id.frameBrowse:
                binding.viewFlipper.setDisplayedChild(1);
                binding.bottomNavigation.setSelectedItemId(R.id.action_browse);
                return;
//            case R.id.frameForYou:
//                binding.viewFlipper.setDisplayedChild(2);
//                binding.bottomNavigation.setSelectedItemId(R.id.action_for_you);
//                return;
            case R.id.frameSearch:
                binding.viewFlipper.setDisplayedChild(3);
                binding.bottomNavigation.setSelectedItemId(R.id.action_search);
                return;
            case R.id.frameLibrary:
                binding.bottomNavigation.setSelectedItemId(R.id.action_library);
                binding.viewFlipper.setDisplayedChild(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(toastDialogReceiver, new IntentFilter("TOAST_DIALOG"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(toastDialogReceiver);
    }

    public void registerRemoteConfig() {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600).build();

        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        Map<String, Object> defaults = new HashMap<>();

        defaults.put("mosito_domain", "mosito.ir");
        defaults.put("mosito_storage", "s1.mosito.ir");

        mFirebaseRemoteConfig.setDefaultsAsync(defaults);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String mosito_domain = mFirebaseRemoteConfig.getString("mosito_domain");
                        String mosito_storage = mFirebaseRemoteConfig.getString("mosito_storage");

                        Log.e("MainActivity", "mosito_domain: " + mosito_domain);
                        Utils.setShare("mosito_domain", mosito_domain);
                        Application.mosito_domain = mosito_domain;

                        Log.e("MainActivity", "mosito_storage: " + mosito_storage);
                        Utils.setShare("mosito_storage", mosito_storage);
                        Application.mosito_storage = mosito_storage;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MainActivity", "OnFailureListener: " + e.getMessage(), e);
                    }
                });
    }

    private void requirePermission() {
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "mosito need external storage permission", REQUEST_CODE, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, Arrays.asList(perms))) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
