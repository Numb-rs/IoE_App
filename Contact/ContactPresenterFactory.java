package internetofeveryone.ioe.Contact;

import android.content.Context;

import internetofeveryone.ioe.Presenter.PresenterFactory;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the creation of new ContactPresenters
 */
public class ContactPresenterFactory implements PresenterFactory<ContactPresenter> {

    Context context;

    /**
     * Instantiates a new ContactPresenterFactory.
     *
     * @param context
     */
    public ContactPresenterFactory(Context context) {
        this.context = context;
    }

    /**
     * Creates a new ContactPresenter
     * @return new ContactPresenter
     */
    public ContactPresenter create() {
        return new ContactPresenter(context);
    }
}