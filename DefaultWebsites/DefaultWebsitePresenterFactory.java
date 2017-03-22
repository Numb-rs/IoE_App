package internetofeveryone.ioe.DefaultWebsites;

import android.content.Context;

import internetofeveryone.ioe.Presenter.PresenterFactory;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the creation of new DefaultWebsitePresenters
 */
public class DefaultWebsitePresenterFactory implements PresenterFactory<DefaultWebsitePresenter> {

    private final Context context;

    /**
     * Instantiates a new DefaultWebsitePresenterFactory.
     *
     * @param context the context
     */
    public DefaultWebsitePresenterFactory(Context context) {
        this.context = context;
    }

    public DefaultWebsitePresenter create() {
        return new DefaultWebsitePresenter(context);
    }
}