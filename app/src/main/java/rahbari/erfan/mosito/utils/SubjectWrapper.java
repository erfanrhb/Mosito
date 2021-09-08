package rahbari.erfan.mosito.utils;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;

public abstract class SubjectWrapper<T> extends Subject<T> {

    public abstract void onChange(T val);

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T t) {
        onChange(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public boolean hasObservers() {
        return false;
    }

    @Override
    public boolean hasThrowable() {
        return false;
    }

    @Override
    public boolean hasComplete() {
        return false;
    }

    @Nullable
    @Override
    public Throwable getThrowable() {
        return null;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {

    }
}
