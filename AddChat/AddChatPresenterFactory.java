package internetofeveryone.ioe.AddChat;

import android.content.Context;

import internetofeveryone.ioe.Presenter.PresenterFactory;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the creation of new AddChatPresenters
 */
public class AddChatPresenterFactory implements PresenterFactory<AddChatPresenter> {

    private final Context context;

    /**
     * Instantiates a new AddChatPresenter factory.
     *
     * @param context the context
     */
    public AddChatPresenterFactory(Context context) {
        this.context = context;
    }

    /**
     * Creates a new AddChatPresenter
     * @return new AddChatPresenter
     */
    public AddChatPresenter create() {
        return new AddChatPresenter(context);
    }
}