package rahbari.erfan.mosito.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.paging.PagedListAdapter;

import rahbari.erfan.mosito.resources.BaseModel;

public abstract class PaginateAdapterUtil<T extends BaseModel, B extends ViewDataBinding>
        extends PagedListAdapter<T, PaginateViewHolder<B>> {

    @LayoutRes
    private int layout;
    protected Activity activity;


    public PaginateAdapterUtil(Activity activity, @LayoutRes int layout) {
        super(new PaginateDiffUtil<>());
        this.layout = layout;
        this.activity = activity;
    }

    public abstract void bindViewHolder(B binding, T item, int position);

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public PaginateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaginateViewHolder<>(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layout, parent, false));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull PaginateViewHolder holder, int position) {
        bindViewHolder((B) holder.binding, getItem(position), position);
    }
}

