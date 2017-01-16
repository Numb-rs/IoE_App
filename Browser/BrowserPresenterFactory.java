package internetofeveryone.ioe.Browser;

import android.content.Context;

import internetofeveryone.ioe.Presenter.PresenterFactory;

public class BrowserPresenterFactory implements PresenterFactory<BrowserPresenter> {
    Context context;

    public BrowserPresenterFactory(Context context) {
        this.context = context;
    }

    public BrowserPresenter create() {
        return new BrowserPresenter(context);
    }
}