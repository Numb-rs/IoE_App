package internetofeveryone.ioe.Presenter;

import android.content.Context;
import android.support.v4.content.Loader;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class represents the Loader that manages Presenters during activity lifecycles
 * @param <T> the presenter
 */
public class PresenterLoader<T extends MvpPresenter> extends Loader<T> {
    private final PresenterFactory<T> factory;
    private T presenter;

    /**
     * Stores away the application context associated with context.
     * Always use {@link #getContext()} to retrieve the Loader's Context
     *
     * @param context used to retrieve the application context.
     * @param factory the PresenterFactory
     */
    public PresenterLoader(Context context, PresenterFactory<T> factory) {
        super(context);
        this.factory = factory;
    }

    @Override
    public void onStartLoading() {

        // if there already exists a presenter, deliver it.
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }
        // otherwise, force a load
        forceLoad();
    }

    @Override
    public void onForceLoad() {

        presenter = factory.create();
        deliverResult(presenter);
    }

    @Override
    public void onReset() {
        if (presenter != null) {
            presenter.onDestroyed();
            presenter = null;
        }
    }
}
