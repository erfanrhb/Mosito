package rahbari.erfan.mosito.utils;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class PaginateViewHolder<X extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public X binding;

    public PaginateViewHolder(X binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
