package internetofeveryone.ioe.Chat;

import android.content.Context;

import internetofeveryone.ioe.Presenter.PresenterFactory;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the creation of new ChatPresenters
 */
public class ChatPresenterFactory implements PresenterFactory<ChatPresenter> {

    Context context;

    /**
     * Instantiates a new ChatPresenterFactory.
     *
     * @param context
     */
    public ChatPresenterFactory(Context context) {
        this.context = context;
    }

    /**
     * Creates a new ChatPresenter
     * @return new ChatPresenter
     */
    public ChatPresenter create() {
        return new ChatPresenter(context);
    }
}