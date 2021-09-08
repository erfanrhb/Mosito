package rahbari.erfan.mosito.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PaginateUtil {
    private int lastFetchedPage = 1;
    private PaginateUtilListeners listeners;

    public PaginateUtil(RecyclerView recyclerView, int perPage, PaginateUtilListeners listeners) {
        this.listeners = listeners;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                try {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (manager == null) return;
                    int page = ((manager.findLastVisibleItemPosition() + 1) / perPage) + 1;
                    if (page > lastFetchedPage) {
                        listeners.fetchPage(page);
                        lastFetchedPage = page;
                    }
                } catch (Exception ex) {
                    Log.e("ReqView", ex.getMessage(), ex);
                }

            }
        });

        listeners.fetchPage(1);
    }

    public void refresh() {
        listeners.fetchPage(1);
        lastFetchedPage = 1;
    }

    public interface PaginateUtilListeners {
        void fetchPage(int page);
    }
}
