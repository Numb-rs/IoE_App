package internetofeveryone.ioe.Presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import internetofeveryone.ioe.Model.WebsiteModel;
import internetofeveryone.ioe.View.MvpView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This abstract class represents the presenter for the part of the application that handles browsing and Websites
 */
public abstract class BrowsingPresenter<V extends MvpView> extends MvpPresenter implements ModelObserver {

    private WebsiteModel model;
    private WeakReference<V> view;

    /**
     * Instantiates a new WebsitePresenter.
     *
     * @param context
     */
    public BrowsingPresenter(Context context) {
        model = new WebsiteModel(context);
        registerObserver(model);
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
     * Gets model.
     *
     * @return the model
     */
    public WebsiteModel getModel() {
        return model;
    }

}
