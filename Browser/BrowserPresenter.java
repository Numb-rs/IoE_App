package internetofeveryone.ioe.Browser;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.android.volley.VolleyLog;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Website;
import internetofeveryone.ioe.Presenter.BrowsingPresenter;
import internetofeveryone.ioe.R;
import internetofeveryone.ioe.Requests.TcpClient;


/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Browser page
 */
public class BrowserPresenter extends BrowsingPresenter<BrowserView> {

    private static final String GOOGLE = "Google";
    private static final String WIKI = "Wikipedia";
    private static final String WEATHER = "Weather";
    private String name; // name of the downloaded website
    private String url; // url of the downloaded website
    private static final String TAG = "BrowserPresenter";
    private final Context context;
    private TcpClient tcpClient;

    /**
     * Instantiates a new Browser presenter.
     *
     * @param context the context
     */
    public BrowserPresenter(Context context) {
        super(context);
        this.context = context;
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
        }
        return result;
    }

    /**
     * Searched for a term in a given search engine
     */
    public void onOpenClickedSearch(String name, String searchTerm) {
        switch (name) {
            case GOOGLE:
            case WEATHER:
                getView().sendSearchRequest(name, searchTerm);
                break;
            case WIKI:
                getView().sendSearchRequest("wiki", searchTerm);
                break;
            default:
                break;
        }
    }

    /**
     * Downloads the Website that has been selected through a search engine
     */
    public boolean onDownloadClickedSearch(String name, String searchTerm) {
        switch (name) {
            case GOOGLE:
            case WEATHER:
                return downloadSearch(name, searchTerm);
            case WIKI:
                return downloadSearch("wiki", searchTerm);
            default:
                return false;
        }
    }

    /**
     * Opens the favorite website that has been clicked on
     * @param name of the website
     */
    public void onOpenClickedFavorite(String name) {
        for (Website w : getModel().getAllDefaultWebsites()) {
            if (w.getName().equals(name)) {
                getView().goToURL(w.getUrl());
            }
        }
    }

    /**
     * Downloads the favorite website that has been clicked on
     * @param name of the website
     */
    public boolean onDownloadClickedFavorite(String name) {
        for (Website w : getModel().getAllDefaultWebsites()) {
            if (w.getName().equals(name)) {
                return downloadWithoutSearch(w.getUrl(), name);
            }
        }
        return false;
    }

    public void onOpenClickedURL(String url) {
        getView().goToURL(url);
    }

    public boolean onDownloadClickedURL(String url) {
        String name = url.replace("http://www.", "");
        name = name.replace("https://www.", "");
        if (name.endsWith("/")) {
            name = name.substring(0, name.length()-1);
        }
        return downloadWithoutSearch(url, name);
    }

    public void downloadWebsite(String websiteName, String content) {
        downloadWebsite(websiteName, websiteName, content);
    }

    public void downloadWebsite(String websiteName, String url, String content) {
        boolean success = getModel().addDownloadedWebsite(websiteName, url, content);
        if (!success) {
            if (isViewAttached()) {
                getView().displayMessage(context.getString(R.string.already_downloaded_website));
            } else {
                attachView(new BrowserActivity());
                downloadWebsite(websiteName, url, content);
            }
        } else {
            if (isViewAttached()) {
                getView().displayMessage(context.getString(R.string.website_download_success));
            } else {
                attachView(new BrowserActivity());
                downloadWebsite(websiteName, url, content);
            }
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
        } else {
            attachView(new BrowserActivity());
            getView().goToURL(url);
        }
    }

    /**
     * parses the response from the request and returns only the markdown value
     * @param jsonLine json response
     * @return parsed response
     */
    private String parse(String jsonLine) {
        try {
            JsonElement jelement = new JsonParser().parse(jsonLine);
            JsonObject jobject = jelement.getAsJsonObject();
            JsonObject jdataObject = jobject.getAsJsonObject("data");
            JsonObject jobject2 = jdataObject.getAsJsonObject("attributes");
            return jobject2.get("markdown").toString();
        } catch (Exception e) {
            return context.getString(R.string.not_a_valid_website);
        }
    }

    /**
     * sends a search request
     * @param engine search engine
     * @param searchTerm search term
     */
    private boolean downloadSearch(final String engine, final String searchTerm) {

        Log.d(VolleyLog.TAG, "Engine passed: " + engine);
        Log.d(VolleyLog.TAG, "Searchterm passed: " + searchTerm);

        final String languageParameter = "{\"language\": \"" + Locale.getDefault().getDisplayLanguage() + "\"}";
        name = engine + ": " + searchTerm;
        url = name;

        new Connect().execute("");

        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                if (tcpClient != null) {
                    if (!tcpClient.sendMessage(getModel().getUserCode() + "\0WEBSRCH\0" + searchTerm + "\0" + engine + "\0" + languageParameter + "\u0004")) {
                        Log.e(TAG, "download search didn't work due to connection issues");
                        getView().displayNetworkErrorMessage();
                    }
                } else {
                    Log.e(TAG, "tcpclient is null");
                }
                if (isViewAttached()) {
                    getView().closeLoader();
                }
            }
        };
        handler.postDelayed(r, 2000); // 2 seconds
        return true;

    }

    /**
     * sends a website request
     * @param urlOfWebsite url of website
     * @param name name of website
     */
    private boolean downloadWithoutSearch(final String urlOfWebsite, final String name) {

        Log.d(TAG, "Url passed: " + urlOfWebsite);

        this.name = name;
        url = urlOfWebsite;

        new Connect().execute("");

        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                if (tcpClient != null) {
                    if (!tcpClient.sendMessage(getModel().getUserCode() + "\0WEBREQU\0" + urlOfWebsite + "\u0004")) {
                        Log.e(TAG, "download without search didn't work due to connection issues");
                        getView().displayNetworkErrorMessage();
                    }
                } else {
                    Log.e(TAG, "tcpclient is null");
                }
                if (isViewAttached()) {
                    getView().closeLoader();
                }
            }
        };
        handler.postDelayed(r, 2000); // 2 seconds

        if (tcpClient != null) {
            if (!tcpClient.sendMessage(getModel().getUserCode() + "\0WEBREQU\0" + urlOfWebsite + "\u0004")) {
                Log.e(TAG, "download without search didn't work due to connection issues");
                return false;
            }
        } else {
            Log.e(TAG, "tcpclient is null");
        }
        return true;

    }

    @Override
    public void update(DataType type) {
        if (type.equals(DataType.WEBSITE)) {
            if (isViewAttached()) {
                getView().dataChanged();
            }
        }
    }

    private class Connect extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            // we create a TCPClient object
            tcpClient = new TcpClient(new TcpClient.OnMessageReceived() {

                @Override
                public void messageReceived(String message) {
                    publishProgress(message);
                }
            });

            tcpClient.run();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);

            String response = values[0];
            Log.d(TAG, "response " + response);

            String parsedResponse = parse(response); // markdown

            if (parsedResponse.equals((context.getString(R.string.not_a_valid_website)))) {
                if (isViewAttached()) {
                    getView().displayMessage(context.getString(R.string.not_valid_website_without_error));
                } else {
                    attachView(new BrowserActivity());
                }
                tcpClient.stopClient();
                return;
            }

            // change links and line breaks into the right format for MarkdownView
            parsedResponse = parsedResponse.replace("\\" + "n", "  \n");
            parsedResponse = parsedResponse.replace("\\" + "\"", "\"");
            String content = parsedResponse.substring(1, parsedResponse.length()-1);
            downloadWebsite(name, url, content);
            tcpClient.stopClient();
        }

    }
}
