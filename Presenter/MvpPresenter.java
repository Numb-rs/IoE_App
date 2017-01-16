package internetofeveryone.ioe.Presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import internetofeveryone.ioe.Model.Model;
import internetofeveryone.ioe.View.MvpView;

public abstract class MvpPresenter<V extends MvpView> implements ModelObserver {

    // private ModelInterface model = null;
    private Model model = null;
    private WeakReference<V> view;

    public MvpPresenter(Context context) {
        System.out.println("Presenter wurde erstellt");
        model = Model.getModel(context);
        registerObserver(model);
    }

    public Model getModel() {
        return model;
    }

    public V getView() { // should always call isViewAttached before calling getView
        return this.view == null ? null : this.view.get();
    }

    public boolean isViewAttached() {
        return this.view != null && this.view.get() != null;
    }

    public void attachView(@NonNull V view) {
        this.view = new WeakReference<>(view);
    }

    public void detachView() {
        if (this.view != null) {
            this.view.clear();
            this.view = null;
        }
    }

    public void onDestroyed() {

    }

    protected boolean setupDone() {
        return isViewAttached() && model != null;
    }

    public void registerObserver(Model model) {
        model.addObserver(this);
    }
}
