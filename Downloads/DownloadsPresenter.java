package internetofeveryone.ioe.Downloads;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Website;
import internetofeveryone.ioe.Presenter.BrowsingPresenter;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Downloads page
 */
public class DownloadsPresenter extends BrowsingPresenter<DownloadsView> {


    /**
     * Instantiates a new DownloadsPresenter.
     *
     * @param context the context
     */
    public DownloadsPresenter(Context context) {
        super(context);
    }

    @Override
    public void update(DataType type) {
        if (type.equals(DataType.WEBSITE)) {
            if(isViewAttached()) {
                getView().dataChanged();
            }
        }
    }

    /**
     * Gets the names of all names of downloaded Websites from the model
     *
     * @return a list of the names
     */
    public ArrayList<String> getDownloadedWebsiteNames() {
        if (getModel().getAllDownloadedWebsites() == null) {
            return new ArrayList<>();
        }
        List<Website> websites = getModel().getAllDownloadedWebsites();
        ArrayList<String> result = new ArrayList<>();
        for (Website w : websites) {
            result.add(w.getName());
        }
        return result;

    }

    /**
     * Deletes the downloaded Website with the given name
     *
     * @param name name of the downloaded Website
     */
    public void deleteClicked(String name) {
        List<Website> websites = getModel().getAllDownloadedWebsites();
        String url = null;
        for (Website w : websites) {
            if (w.getName().equals(name)) {
                url = w.getUrl();
            }
        }
        getModel().deleteDownloadedWebsite(url);
    }

    /**
     * Opens the downloaded Website with the given name
     *
     * @param name name of the downloaded Website
     */
    public void openClicked(String name) {
        List<Website> websites = getModel().getAllDownloadedWebsites();
        if(isViewAttached()) {
            String content = null;
            for (Website w : websites) {
                if (w.getName().equals(name)) {
                    content = w.getContent();
                }
            }
            getView().displayContent(content);
        }
    }
}
