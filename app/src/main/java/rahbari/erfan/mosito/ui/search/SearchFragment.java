package rahbari.erfan.mosito.ui.search;

import android.text.Editable;

import java.util.Timer;
import java.util.TimerTask;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentSearchBinding;
import rahbari.erfan.mosito.utils.AfterTextChangeListener;
import rahbari.erfan.mosito.utils.FragmentUtil;
import rahbari.erfan.mosito.utils.Utils;

public class SearchFragment extends FragmentUtil<FragmentSearchBinding> {
    private Timer timer;
    private int duration = 0;
    private SearchOnlineFragment onlineFragment;

    @Override
    public void onViewCreate() {
        binding.edtSearch.setOnFocusChangeListener((v, hasFocus) -> toggleHelper(hasFocus));

        onlineFragment = new SearchOnlineFragment();

        getChildFragmentManager().beginTransaction().replace(R.id.frameSearch, onlineFragment).commit();

        binding.edtSearch.addTextChangedListener(new AfterTextChangeListener() {
            @Override
            protected void afterChanged(Editable e) {
                startTimer();
                duration = 0;
                toggleHelper(false);
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
    }

    private void toggleHelper(boolean hasFocus) {
//        if (hasFocus || Utils.text(binding.edtSearch).length() > 0) Utils.collapse(binding.lnlHelp);
//        else Utils.expand(binding.lnlHelp);
    }

    class searchTimeout extends TimerTask {
        @Override
        public void run() {
            duration += 1;

            if (duration >= 3) {
                activity.runOnUiThread(() -> onlineFragment.setSearchQuery(Utils.text(binding.edtSearch)));
                clearTimer();
            }
        }
    }

    private void startTimer() {
        if (timer != null) return;
        timer = new Timer();
        timer.schedule(new searchTimeout(), 0, 1000);
    }

    private void clearTimer() {
        if (timer == null) return;
        timer.cancel();
        timer.purge();
        timer = null;
    }
}
