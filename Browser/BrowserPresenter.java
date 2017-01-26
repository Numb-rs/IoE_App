package internetofeveryone.ioe.Browser;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.MvpPresenter;
import internetofeveryone.ioe.Data.Website;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Browser page
 */
public class BrowserPresenter extends MvpPresenter<BrowserView> {

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
        Collection<Website> websites = getModel().getDefaultWebsiteList().values();
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
                for(Website w : getModel().getDefaultWebsiteList().values()) {
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
                for(Website w : getModel().getDefaultWebsiteList().values()) {
                    if (w.getName() == selectedWebsiteName) {
                        getModel().addDownloadedWebsite(getModel().getDefaultWebsite(w.getUrl()));
                    }
                }
                break;
            case URL:
                Website website = new Website(selectedWebsiteName, selectedWebsiteURL, "content");
                getModel().addDownloadedWebsite(website);
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
