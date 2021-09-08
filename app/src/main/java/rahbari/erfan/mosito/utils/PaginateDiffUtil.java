package rahbari.erfan.mosito.utils;

import androidx.recyclerview.widget.DiffUtil;

import rahbari.erfan.mosito.resources.BaseModel;

public class PaginateDiffUtil<T extends BaseModel> extends DiffUtil.ItemCallback<T> {

    @Override
    public boolean areItemsTheSame(T oldItem, T newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(T oldItem, T newItem) {
        return oldItem.getId() == newItem.getId();
    }

}
