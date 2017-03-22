package internetofeveryone.ioe.Browser;

import android.content.Context;

import internetofeveryone.ioe.Presenter.PresenterFactory;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the creation of new BrowserPresenters
 */
public class BrowserPresenterFactory implements PresenterFactory<BrowserPresenter> {
    /**
     * The Context.
     */
    private final Context context;

    /**
     * Instantiates a new BrowserPresenterFactory.
     *
     * @param context the context
     */
    public BrowserPresenterFactory(Context context) {
        this.context = context;
    }

    /**
     * Creates a new BrowserPresenter
     * @return new BrowserPresenter
     */
    public BrowserPresenter create() {
        return new BrowserPresenter(context);
    }
}