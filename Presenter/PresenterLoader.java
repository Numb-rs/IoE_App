package internetofeveryone.ioe.Presenter;

import android.content.Context;
import android.support.v4.content.Loader;

public class PresenterLoader<T extends MvpPresenter> extends Loader<T> {
    private final PresenterFactory<T> factory;
    private T presenter;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public PresenterLoader(Context context, PresenterFactory<T> factory) {
        super(context);
        this.factory = factory;
    }

    @Override
    public void onStartLoading() {
        // System.out.println("onStartLoading. Presenter = " + (presenter == null ? null : "Pres."));
        // If we already own an instance, simply deliver it.
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }

        // Otherwise, force a load
        forceLoad();
    }

    @Override
    public void onForceLoad() {
        // System.out.println("onForceLoad. Presenter = " + (presenter == null ? null : "Pres."));
        // Create the Presenter using the Factory
        presenter = factory.create();

        // Deliver the result
        deliverResult(presenter);
    }

    @Override
    public void onReset() {
        // System.out.println("onReset. Presenter = " + (presenter == null ? null : "Pres."));
        if (presenter != null) {
            presenter.onDestroyed();
            presenter = null;
        }
    }
}
