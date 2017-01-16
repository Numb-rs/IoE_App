package internetofeveryone.ioe.Main;

import android.content.Context;

import internetofeveryone.ioe.Presenter.PresenterFactory;

public class MainPresenterFactory implements PresenterFactory<MainPresenter> {
    Context context;
    public MainPresenterFactory(Context context) {
        this.context = context;
    }
    public MainPresenter create() {
        return new MainPresenter(context);
    }
}
