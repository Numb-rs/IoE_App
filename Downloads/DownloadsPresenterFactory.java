package internetofeveryone.ioe.Downloads;


import android.content.Context;

import internetofeveryone.ioe.Presenter.PresenterFactory;

public class DownloadsPresenterFactory implements PresenterFactory<DownloadsPresenter> {
    Context context;
    public DownloadsPresenterFactory(Context context) {
        this.context = context;
    }

    public DownloadsPresenter create() {
        return new DownloadsPresenter(context);
    }
}
