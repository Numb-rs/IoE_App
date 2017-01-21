package internetofeveryone.ioe.Downloads;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Website;
import internetofeveryone.ioe.Presenter.MvpPresenter;

public class DownloadsPresenter extends MvpPresenter<DownloadsView> {


    public DownloadsPresenter(Context context) {
        super(context);
    }

    @Override
    public void update(DataType type, String id) {
        if (type.equals(DataType.WEBSITE)) {
            if(isViewAttached()) {
                getView().dataChanged();
            }
        }
    }

    public ArrayList<String> getDownloadedWebsiteNames() {
        if (getModel().getDownloadedWebsiteList() == null) {
            return new ArrayList<String>(); // just momentarily
        }
        Collection<Website> websites = getModel().getDownloadedWebsiteList().values();
        ArrayList<String> result = new ArrayList<>();
        for (Website w : websites) {
            result.add(w.getName());
        }
        return result;

    }

    public void deleteClicked(String name) {
        Collection<Website> websites = getModel().getDownloadedWebsiteList().values();
        String url = null;
        for (Website w : websites) {
            if (w.getName().equals(name)) {
                url = w.getUrl();
            }
        }
        getModel().removeDownloadedWebsite(url);
    }

    public void openClicked(String name) {
        Collection<Website> websites = getModel().getDownloadedWebsiteList().values();
        if(isViewAttached()) {
            String url = null;
            for (Website w : websites) {
                if (w.getName().equals(name)) {
                    url = w.getUrl();
                }
            }
            getView().openWebsite(url);
        }
    }
}
