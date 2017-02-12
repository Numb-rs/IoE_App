package internetofeveryone.ioe.DefaultWebsites;

import android.content.Context;
import android.widget.Toast;

import java.util.Arrays;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Website;
import internetofeveryone.ioe.Presenter.BrowsingPresenter;
import internetofeveryone.ioe.R;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the DefaultWebsite fragment
 */
public class DefaultWebsitePresenter extends BrowsingPresenter<DefaultWebsiteView> {

    Context context;
    /**
     * Instantiates a new Default website presenter.
     *
     * @param context
     */
    public DefaultWebsitePresenter(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Gets the names of all DefaultWebsites from the model
     *
     * @return array of the names
     */
    public String[] getDefaultWebsiteNames() {
        Object[] objects = getModel().getAllDefaultWebsites().toArray();
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
        Object[] objects = getModel().getAllDefaultWebsites().toArray();
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
        if(isViewAttached()) {
            boolean success = getModel().addDefaultWebsite(name, url, "");
            if (!success) {
                Toast.makeText(context, context.getString(R.string.already_downloaded_website), Toast.LENGTH_SHORT);
            }
        } else {
            // ErrorHandling
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
        getModel().deleteDefaultWebsite(originalURL);
        getModel().addDefaultWebsite(name, url, "");
    }

    /**
     * Deletes a DefaultWebsite
     *
     * @param url url of the DefaultWebsite
     */
    public void onClickDelete(String url) {
        getModel().deleteDefaultWebsite(url);
    }

    @Override
    public void update(DataType type) {
        if (type.equals(DataType.WEBSITE)) {
            if (isViewAttached()) {
                getView().dataChanged();
            }
        }
    }
}
