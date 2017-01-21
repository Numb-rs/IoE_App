package internetofeveryone.ioe.DefaultWebsites;

import android.content.Context;

import java.util.Arrays;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Website;
import internetofeveryone.ioe.Presenter.MvpPresenter;

public class DefaultWebsitePresenter extends MvpPresenter<DefaultWebsiteView> {

    public DefaultWebsitePresenter(Context context) {
        super(context);
    }

    public String[] getDefaultWebsiteNames() {
        Object[] objects = getModel().getDefaultWebsiteList().values().toArray();
        Website[] websites = Arrays.copyOf(objects, objects.length, Website[].class);
        String[] result = new String[websites.length];
        for (int i = 0; i < websites.length; i++) {
            result[i] = websites[i].getName();
        }
        return result;
    }

    public String[] getDefaultWebsiteURLs() {
        Object[] objects = getModel().getDefaultWebsiteList().values().toArray();
        Website[] websites = Arrays.copyOf(objects, objects.length, Website[].class);
        String[] result = new String[websites.length];
        for (int i = 0; i < websites.length; i++) {
            result[i] = websites[i].getUrl();
        }
        return result;
    }


    public void onClickAdd() {
        if(isViewAttached()) {
            getView().onAddDefaultWebsite();
        }
    }

    public void onClickExit() {
        // may add sth here later
    }

    public void onClickAddWebsite(String name, String url) {
        System.out.println("Kommt im Presenter an");
        if(isViewAttached()) {
            Website website = new Website(name, url, "content");
            getModel().addDefaultWebsite(website);
        } else {
            System.out.println("Presenter nicht attached");
        }
    }

    public void onClickDefaultWebsite(int pos) {
        if(isViewAttached()) {
            getView().onEditDefaultWebsite(pos);
        }
    }

    public void onClickCancel() {
        // may add sth here later
    }

    public void onClickSaveChange(String originalURL, String name, String url) {
        getModel().removeDefaultWebsite(originalURL);
        Website website = new Website(name, url, "content");
        getModel().addDefaultWebsite(website);
    }

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
