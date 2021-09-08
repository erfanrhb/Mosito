package rahbari.erfan.mosito.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import rahbari.erfan.mosito.resources.BaseModel;

public abstract class AdapterUtil<T extends BaseModel, B extends ViewDataBinding>
        extends RecyclerView.Adapter<AdapterUtil.CustomViewHolderUtils> {
    @LayoutRes
    private int layout;
    private List<T> array;
    public Activity activity;

    public abstract void bindViewHolder(B binding, T item, int position);

    public AdapterUtil(Activity activity, @LayoutRes int layout) {
        this.layout = layout;
        this.activity = activity;
        this.array = new ArrayList<>();
    }

    public AdapterUtil(Activity activity, @LayoutRes int layout, List<T> array) {
        this.array = array;
        this.layout = layout;
        this.activity = activity;
    }

    public void setArray(List<T> array) {
        this.array = array;
        activity.runOnUiThread(this::notifyDataSetChanged);
    }

    public void addArray(List<T> array) {
        this.array.addAll(array);
        activity.runOnUiThread(this::notifyDataSetChanged);
    }

    public void addArray(T item) {
        this.array.add(item);
        activity.runOnUiThread(this::notifyDataSetChanged);
    }

    public int find(T item) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getId() == item.getId())
                return i;
        }
        return -1;
    }

    public void updateArray(T i) {
        int pos = find(i);
        if (pos > 0) array.set(pos, i);
        else array.add(i);
        activity.runOnUiThread(this::notifyDataSetChanged);
    }

    public void updateArray(List<T> items) {
        for (T i : items) {
            int pos = find(i);
            if (pos > 0) array.set(pos, i);
            else array.add(i);
        }
        activity.runOnUiThread(this::notifyDataSetChanged);
    }

    public void delete(int position) {
        array.remove(position);
        activity.runOnUiThread(this::notifyDataSetChanged);
    }

    public void remove(int position) {
        array.remove(position);
        activity.runOnUiThread(this::notifyDataSetChanged);
    }

    public List<T> getArray() {
        return array;
    }

    public T getItem(int position) {
        return array.get(position);
    }

    @NonNull
    @Override
    public CustomViewHolderUtils onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolderUtils(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layout, parent, false));
    }

    @Override
    public int getItemCount() {
        return this.array.size();
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolderUtils holder, int position) {
        bindViewHolder((B) holder.binding, array.get(position), position);
    }

    public static class CustomViewHolderUtils<X extends ViewDataBinding> extends RecyclerView.ViewHolder {
        public X binding;

        public CustomViewHolderUtils(X binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
