package internetofeveryone.ioe.DefaultWebsites;

import android.content.Context;

import java.util.Arrays;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Website;
import internetofeveryone.ioe.Presenter.MvpPresenter;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the DefaultWebsite fragment
 */
public class DefaultWebsitePresenter extends MvpPresenter<DefaultWebsiteView> {

    /**
     * Instantiates a new Default website presenter.
     *
     * @param context
     */
    public DefaultWebsitePresenter(Context context) {
        super(context);
    }

    /**
     * Gets the names of all DefaultWebsites from the model
     *
     * @return array of the names
     */
    public String[] getDefaultWebsiteNames() {
        Object[] objects = getModel().getDefaultWebsiteList().values().toArray();
        Website[] websites = Arrays.copyOf(objects, objects.length, Website[].class);
        String[] result = new String[websites.length];
        for (int i = 0; i < websites.length; i++) {
            result[i] = websites[i].getName();
        }
        return result;
    }

    /**
     * Gets the URLs of all DefaultWebsites from the model
     *
     * @return array of the URLs
     */
    public String[] getDefaultWebsiteURLs() {
        Object[] objects = getModel().getDefaultWebsiteList().values().toArray();
        Website[] websites = Arrays.copyOf(objects, objects.length, Website[].class);
        String[] result = new String[websites.length];
        for (int i = 0; i < websites.length; i++) {
            result[i] = websites[i].getUrl();
        }
        return result;
    }


    /**
     * Sends a request to the view to open a new Dialog to allow the user to add a new DefaultWebsite
     */
    public void onClickAdd() {

        if(isViewAttached()) {
            getView().onAddDefaultWebsite();
        } else {
            // ErrorHandling
        }
    }

    public void onClickExit() {
        // may add sth here later
    }

    /**
     * Adds a new Website
     *
     * @param name name of the Website
     * @param url url of the Website
     */
    public void onClickAddWebsite(String name, String url) {
        // System.out.println("Kommt im Presenter an");
        if(isViewAttached()) {
            Website website = new Website(name, url, "content"); // TODO: "content" entfernen und mit tatsächlichem content füllen
            getModel().addDefaultWebsite(website);
        } else {
            // ErrorHandling
            // System.out.println("Presenter nicht attached");
        }
    }

    /**
     * Sends a request to the view to open a new Dialog to allow the user to edit a DefaultWebsite
     *
     * @param pos position in the list that has been clicked
     */
    public void onClickDefaultWebsite(int pos) {

        if(isViewAttached()) {
            getView().onEditDefaultWebsite(pos);
        } else {
            // ErrorHandling
        }
    }

    public void onClickCancel() {
        // may add sth here later
    }

    /**
     * Saves the user's changes
     *
     * @param originalURL old URL of the DefaultWebsite
     * @param name        new name of the DefaultWebsite
     * @param url         new URL of the DefaultWebsite
     */
    public void onClickSaveChange(String originalURL, String name, String url) {
        getModel().removeDefaultWebsite(originalURL);
        Website website = new Website(name, url, "content"); // TODO: "content" entfernen und mit tatsächlichem content füllen
        getModel().addDefaultWebsite(website);
    }

    /**
     * Deletes a DefaultWebsite
     *
     * @param url url of the DefaultWebsite
     */
    public void onClickDelete(String url) {
        getModel().removeDefaultWebsite(url);
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
