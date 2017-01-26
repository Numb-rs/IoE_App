package internetofeveryone.ioe.Presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import internetofeveryone.ioe.Model.Model;
import internetofeveryone.ioe.View.MvpView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This abstract class represents the presenter for the whole application
 * @param <V> the view
 */
public abstract class MvpPresenter<V extends MvpView> implements ModelObserver {

    private Model model = null;
    private WeakReference<V> view;

    /**
     * Instantiates a new MvpPresenter.
     *
     * @param context
     */
    public MvpPresenter(Context context) {
        // System.out.println("Presenter wurde erstellt");
        model = Model.getModel(context);
        registerObserver(model);
    }

    /**
     * Gets model.
     *
     * @return the model
     */
    public Model getModel() {
        return model;
    }

    /**
     * Gets view.
     *
     * @return the view
     */
    public V getView() { // should always call isViewAttached before calling getView
        return this.view == null ? null : this.view.get();
    }

    /**
     * Is view attached boolean.
     *
     * @return the boolean
     */
    public boolean isViewAttached() {
        return this.view != null && this.view.get() != null;
    }

    /**
     * Attach view.
     *
     * @param view the view
     */
    public void attachView(@NonNull V view) {
        this.view = new WeakReference<>(view);
    }

    /**
     * Detach view.
     */
    public void detachView() {
        if (this.view != null) {
            this.view.clear();
            this.view = null;
        }
    }

    /**
     * On destroyed.
     */
    public void onDestroyed() {

    }

    /**
     * Registers the MvpPresenter as an observer to the Model.
     *
     * @param model the model
     */
    public void registerObserver(Model model) {
        model.addObserver(this);
    }
}
