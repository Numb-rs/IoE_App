package internetofeveryone.ioe.Browser;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;

import internetofeveryone.ioe.Presenter.MvpPresenter;
import internetofeveryone.ioe.Data.Website;

public class BrowserPresenter extends MvpPresenter<BrowserView> {

    String selectedWebsiteName;
    String selectedWebsiteURL;
    String latest;

    public BrowserPresenter(Context context) {
        super(context);
    }

    public ArrayList<String> getDefaultWebsiteNames() {
        Collection<Website> websites = getModel().getDefaultWebsiteList().values();
        ArrayList<String> result = new ArrayList<>();
        for (Website w : websites) {
            result.add(w.getName());
            System.out.println("Name of Def.Website: " + w.getName());
        }
        return result;
    }


    public void websiteSelectedbyName(String name) {
        selectedWebsiteName = name;
        latest = "name";
    }

    public void websiteSelectedbyURL(String url) {
        selectedWebsiteURL = url;
        latest = "url";
    }

    public void onOpenClicked() {
        switch (latest) {
            case "name":
                for(Website w : getModel().getDefaultWebsiteList().values()) {
                    if (w.getName() == selectedWebsiteName) {
                        enterURL(w.getUrl());
                    }
                }
            break;
            case "url":
                enterURL(selectedWebsiteURL);
            break;
            default:
            break;
        }

    }

    public void onDownloadClicked() {
        switch (latest) {
            case "name":
                for(Website w : getModel().getDefaultWebsiteList().values()) {
                    if (w.getName() == selectedWebsiteName) {
                        getModel().addDownloadedWebsite(getModel().getDefaultWebsite(w.getUrl()));
                    }
                }
                break;
            case "url":
                Website website = new Website(selectedWebsiteName, selectedWebsiteURL, "content");
                getModel().addDownloadedWebsite(website);
                break;
            default:
                break;
        }

    }



    public void enterURL(String url) {
        if(isViewAttached()) {
            getView().goToURL(url);
        }
    }


    @Override
    public void update(String type, String id) {
        if (type.equals("website")) {
            if (isViewAttached()) {
                getView().dataChanged();
            }
        }
    }
}
