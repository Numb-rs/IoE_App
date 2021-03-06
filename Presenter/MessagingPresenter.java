package internetofeveryone.ioe.Presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.WeakReference;

import internetofeveryone.ioe.Model.MessageModel;
import internetofeveryone.ioe.View.MvpView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This abstract class represents the presenter for the part of the application that handles chatting and messaging
 */
public abstract class MessagingPresenter<V extends MvpView> extends MvpPresenter implements ModelObserver {

    private MessageModel model;
    private WeakReference<V> view;
    private static final String TAG = "MessagingPresenter";
    private boolean connectionError;

    /**
     * Instantiates a new MessagePresenter.
     *
     * @param context the context
     */
    protected MessagingPresenter(Context context) {
        super();
        model = new MessageModel(context);
        registerObserver(model);
        connectionError = false;
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
    protected boolean isViewAttached() {
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
    public MessageModel getModel() {
        return model;
    }

    public void setModel(MessageModel model) {
        this.model = model;
    }

    /**
     * starts request to fetch new messages from server
     */
    public void fetchMessagesFromServer() {
        Log.e(TAG, "fetches");
    }

    public void connectionFailed() {
        connectionError = true;
    }


}
