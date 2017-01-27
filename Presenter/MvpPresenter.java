package internetofeveryone.ioe.Presenter;

import java.lang.ref.WeakReference;

import internetofeveryone.ioe.Model.MessageModel;
import internetofeveryone.ioe.Model.Model;
import internetofeveryone.ioe.View.MvpView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This abstract class represents the presenter for the whole application
 * @param <V> the view
 */
public abstract class MvpPresenter<V extends MvpView> implements ModelObserver {

    private MessageModel model;
    private WeakReference<V> view;

    /**
     * Gets model.
     *
     * @return the model
     */
    public Model getModel() {
        return model;
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
