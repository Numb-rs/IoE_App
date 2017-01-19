package internetofeveryone.ioe.Website;

import android.content.Context;

import internetofeveryone.ioe.Presenter.PresenterFactory;

public class WebsitePresenterFactory implements PresenterFactory<WebsitePresenter> {
    Context context;

    public WebsitePresenterFactory(Context context) {
        this.context = context;
    }

    public WebsitePresenter create() {
        return new WebsitePresenter(context);
    }
}