package rahbari.erfan.mosito.utils;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class AfterTextChangeListener implements TextWatcher {

    protected abstract void afterChanged(Editable e);

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        afterChanged(editable);
    }
}
