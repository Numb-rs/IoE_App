package internetofeveryone.ioe.Messenger;

import android.content.Context;

import internetofeveryone.ioe.Presenter.PresenterFactory;

public class MessengerPresenterFactory implements PresenterFactory<MessengerPresenter> {
    Context context;

    public MessengerPresenterFactory(Context context) {
        this.context = context;
    }

    public MessengerPresenter create() {
        return new MessengerPresenter(context);
    }
}