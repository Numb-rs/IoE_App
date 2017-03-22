package internetofeveryone.ioe.Messenger;

import android.content.Context;

import internetofeveryone.ioe.Presenter.PresenterFactory;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the creation of new MessengerPresenters
 */
public class MessengerPresenterFactory implements PresenterFactory<MessengerPresenter> {
    /**
     * The Context.
     */
    private final Context context;

    /**
     * Instantiates a new Messenger presenter factory.
     *
     * @param context the context
     */
    public MessengerPresenterFactory(Context context) {
        this.context = context;
    }

    /**
     * Creates a new MessengerPresenter
     * @return new MessengerPresenter
     */
    public MessengerPresenter create() {
        return new MessengerPresenter(context);
    }
}