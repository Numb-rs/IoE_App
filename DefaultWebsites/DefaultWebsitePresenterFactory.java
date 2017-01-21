package internetofeveryone.ioe.DefaultWebsites;

import android.content.Context;

import internetofeveryone.ioe.Presenter.PresenterFactory;

public class DefaultWebsitePresenterFactory implements PresenterFactory<DefaultWebsitePresenter> {
    Context context;

    public DefaultWebsitePresenterFactory(Context context) {
        this.context = context;
    }

    public DefaultWebsitePresenter create() {
        return new DefaultWebsitePresenter(context);
    }
}