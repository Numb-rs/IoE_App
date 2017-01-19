package internetofeveryone.ioe.Main;

import android.content.Context;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.MvpPresenter;

public class MainPresenter extends MvpPresenter<MainView> {

    public MainPresenter(Context context) {
        super(context);
    }

    public void onMessengerClicked() {
        if (isViewAttached()) {
            getView().goToMessenger();
        }
    }

    public void onDownloadsClicked() {
        if (isViewAttached()) {
            getView().goToDownloads();
        }
    }

    public void onBrowserClicked() {
        if (isViewAttached()) {
            getView().goToBrowser();
        }
    }



    @Override
    public void update(DataType type, String id) {
    }

}
