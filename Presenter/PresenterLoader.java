package internetofeveryone.ioe.Presenter;

import android.content.Context;
import android.support.v4.content.Loader;

public class PresenterLoader<T extends MvpPresenter> extends Loader<T> {
    private final PresenterFactory<T> factory;
    private T presenter;

    // Stores away the application context associated with context.
     
    public PresenterLoader(Context context, PresenterFactory<T> factory) {
        super(context);
        this.factory = factory;
    }

    @Override
    public void onStartLoading() {
        // If we already own an instance, simply deliver it.
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }

        // Otherwise, create a new one
        forceLoad();
    }

    @Override
    public void onForceLoad() {
        // Create the Presenter using the Factory
        presenter = factory.create();

        // Deliver the result
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
