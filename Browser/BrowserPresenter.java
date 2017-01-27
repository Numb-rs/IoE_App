package internetofeveryone.ioe.Browser;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Website;
import internetofeveryone.ioe.Presenter.BrowsingPresenter;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Browser page
 */
public class BrowserPresenter extends BrowsingPresenter<BrowserView> {

    private String selectedWebsiteName; // name of the selected Website
    private String selectedWebsiteURL; // URL of the selected Website

    /**
     * Keeps track which method of selecting a Website has been used most recently
     * Possible values: NAME, URL
     */
    private String latest;
    private static final String NAME = "name";
    private static final String URL = "url";



    /**
     * Instantiates a new Browser presenter.
     *
     * @param context
     */
    public BrowserPresenter(Context context) {
        super(context);
    }

    /**
     * Gets the names of all DefaultWebsites from the model
     *
     * @return a list of the names
     */
    public ArrayList<String> getDefaultWebsiteNames() {
        List<Website> websites = getModel().getAllDefaultWebsites();
        ArrayList<String> result = new ArrayList<>();
        for (Website w : websites) {
            result.add(w.getName());
            // System.out.println("Name of Def.Website: " + w.getName());
        }
        return result;
    }


    /**
     * Stores the name that has been selected in the dropdown menu and sets name as the most recently used
     * method of selecting a Website
     *
     * @param name name of the selected Website
     */
    public void websiteSelectedByName(String name) {

        selectedWebsiteName = name;
        latest = NAME;
    }

    /**
     * Stores the URL that has been entered by the user and sets URL as the most recently used
     * method of selecting a Website
     *
     * @param url URL as entered by the user
     */
    public void websiteSelectedByURL(String url) {

        selectedWebsiteURL = url;
        latest = URL;
    }

    /**
     * Opens the Website that has been selected the most recently
     */
    public void onOpenClicked() {
        switch (latest) {
            case NAME:
                for(Website w : getModel().getAllDefaultWebsites()) {
                    if (w.getName() == selectedWebsiteName) {
                        enterURL(w.getUrl());
                    }
                }
            break;
            case URL:
                enterURL(selectedWebsiteURL);
            break;
            default:
            break;
        }

    }

    /**
     * Downloads the Website that has been selected the most recently
     */
    public void onDownloadClicked() {
        switch (latest) {
            case NAME:
                for(Website w : getModel().getAllDefaultWebsites()) {
                    if (w.getName() == selectedWebsiteName) {
                        Website website = getModel().getDefaultWebsiteByURL(w.getUrl());
                        getModel().addDownloadedWebsite(website.getName(), website.getUrl(), website.getContent());
                    }
                }
                break;
            case URL:
                // TODO: "content" mit tatsächliem content ersetzen
                getModel().addDownloadedWebsite(selectedWebsiteName, selectedWebsiteURL, "content");
                break;
            default:
                break;
        }

    }


    /**
     * Sends a request to the view to open the Website with the given URL
     *
     * @param url URL of the selected Website
     */
    public void enterURL(String url) {

        if(isViewAttached()) {
            getView().goToURL(url);
        }
    }


    @Override
    public void update(DataType type, String id) {
        if (type.equals(DataType.WEBSITE)) {
            if (isViewAttached()) {
                getView().dataChanged();
            }
        }
    }
}
