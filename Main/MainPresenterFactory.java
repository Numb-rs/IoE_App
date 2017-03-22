package internetofeveryone.ioe.Main;

import android.content.Context;

import internetofeveryone.ioe.Presenter.PresenterFactory;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the creation of new MainPresenters
 */
public class MainPresenterFactory implements PresenterFactory<MainPresenter> {

    private final Context context;

    /**
     * Instantiates a new MainPresenterFactory.
     *
     * @param context the context
     */
    public MainPresenterFactory(Context context) {
        this.context = context;
    }

    /**
     * Creates a new MainPresenter
     * @return new MainPresenter
     */
    public MainPresenter create() {
        return new MainPresenter(context);
    }
}
