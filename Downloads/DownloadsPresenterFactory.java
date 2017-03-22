package internetofeveryone.ioe.Downloads;


import android.content.Context;

import internetofeveryone.ioe.Presenter.PresenterFactory;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the creation of new DownloadsPresenters
 */
public class DownloadsPresenterFactory implements PresenterFactory<DownloadsPresenter> {
    /**
     * The Context.
     */
    private final Context context;

    /**
     * Instantiates a new DownloadsPresenterFactory.
     *
     * @param context the context
     */
    public DownloadsPresenterFactory(Context context) {
        this.context = context;
    }

    /**
     * Creates a new DownloadsPresenter
     * @return new DownloadsPresenter
     */
    public DownloadsPresenter create() {
        return new DownloadsPresenter(context);
    }
}
