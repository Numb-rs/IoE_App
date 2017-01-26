package internetofeveryone.ioe.Website;

import android.content.Context;

import internetofeveryone.ioe.Presenter.PresenterFactory;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the creation of new WebsitePresenters
 */
public class WebsitePresenterFactory implements PresenterFactory<WebsitePresenter> {

    Context context;

    /**
     * Instantiates a new WebsitePresenterFactory.
     *
     * @param context
     */
    public WebsitePresenterFactory(Context context) {
        this.context = context;
    }

    /**
     * Creates a new WebsitePresenter
     * @return new WebsitePresenter
     */
    public WebsitePresenter create() {
        return new WebsitePresenter(context);
    }
}