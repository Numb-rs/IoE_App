package internetofeveryone.ioe.Browser;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Website;
import internetofeveryone.ioe.Presenter.BrowsingPresenter;
import internetofeveryone.ioe.Website.AppController;

import static internetofeveryone.ioe.Website.AppController.TAG;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Browser page
 */
public class BrowserPresenter extends BrowsingPresenter<BrowserView> {

    private static final String GOOGLE = "Google";
    private static final String WIKI = "Wikipedia";
    private static final String WEATHER = "Weather";

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
                // Error Handling
                break;
        }
    }

    /**
     * Downloads the Website that has been selected through a search engine
     */
    public void onDownloadClickedSearch(String name, String searchTerm) {
        switch (name) {
            case GOOGLE:
            case WEATHER:
                downloadSearch(name, searchTerm);
                break;
            case WIKI:
                downloadSearch("wiki", searchTerm);
                break;
            default:
                // Error Handling
                break;
        }
    }

    // TODO: javadoc
    public void onOpenClickedFavorite(String name) {
        for (Website w : getModel().getAllDefaultWebsites()) {
            if (w.getName().equals(name)) {
                getView().goToURL(w.getUrl());
            }
        }
    }

    public void onDownloadClickedFavorite(String name) {
        for (Website w : getModel().getAllDefaultWebsites()) {
            if (w.getName().equals(name)) {
                downloadWithoutSearch(w.getUrl(), name);
            }
        }
    }

    public void onOpenClickedURL(String url) {
        getView().goToURL(url);
    }

    public void onDownloadClickedURL(String url) {
        String name = url.replace("http://www.", "");
        name = name.replace("https://www.", "");
        if (name.endsWith("/")) {
            name = name.substring(0, name.length()-1);
        }
        downloadWithoutSearch(url, name);
    }

    public void downloadWebsite(String websiteName, String content) {
        boolean success = getModel().addDownloadedWebsite(websiteName, websiteName, content);
        if (!success) {
            if (isViewAttached()) {
                getView().displayMessage("You've already downloaded this website"); // TODO: auslagern
            } else {
                // error handling
            }
        } else {
            if (isViewAttached()) {
                getView().displayMessage("Your Website has been downloaded"); // TODO: auslagern
            } else {
                // error handling
            }
        }
    }

    public void downloadWebsite(String websiteName, String url, String content) {
        boolean success = getModel().addDownloadedWebsite(websiteName, url, content);
        if (!success) {
            if (isViewAttached()) {
                getView().displayMessage("You've already downloaded this website"); // TODO: auslagern
            } else {
                // error handling
            }
        } else {
            if (isViewAttached()) {
                getView().displayMessage("Your Website has been downloaded"); // TODO: auslagern
            } else {
                // error handling
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
        }
    }

        // TODO: javadoc
    public String parse(String jsonLine) {
        try {
            JsonElement jelement = new JsonParser().parse(jsonLine);
            JsonObject jobject = jelement.getAsJsonObject();
            JsonArray jarray = jobject.getAsJsonArray("data");
            jobject = jarray.get(0).getAsJsonObject();
            JsonObject jobject2 = jobject.getAsJsonObject("attributes");
            String result = jobject2.get("markdown").toString();
            return result;
        } catch (Exception e) {
            return "Error: This is not a valid website"; // TODO: errormessage auslagern in strings.xml
        }
    }

    public void downloadSearch(final String engine, final String searchTerm) {

        Log.d(TAG, "Engine passed: " + engine);
        Log.d(TAG, "Searchterm passed: " + searchTerm);

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String serverURL = "http://jintzo.me/api/request/search"; // TODO: in extra Datei und als Konstante

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                serverURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        String parsedResponse = parse(response); // markdown
                        if (parsedResponse.equals("Error: This is not a valid website")) { // TODO: text auslagern, zeile drunter auch
                            if (isViewAttached()) {
                                getView().displayMessage("This is not a valid website"); // TODO: auslagern
                            }
                            return;
                        }
                        parsedResponse = parsedResponse.replace("\\" + "n", "  \n");
                        parsedResponse = parsedResponse.replace("\\" + "\"", "\"");
                        String content = parsedResponse.substring(1, parsedResponse.length()-1);
                        downloadWebsite(engine + ": " + searchTerm, content);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("searchTerm", searchTerm);
                params.put("engine", engine);
                params.put("parameters", "{\"language\": \"en\"}"); // TODO: abhängig von telefonsprache
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    public void downloadWithoutSearch(final String urlOfWebsite, final String name) {

        Log.d(TAG, "Url passed: " + urlOfWebsite);

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String serverURL = "http://jintzo.me/api/request/page"; // TODO: in extra Datei und als Konstante

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                serverURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        String parsedResponse = parse(response); // markdown
                        if (parsedResponse.equals("Error: This is not a valid website")) { // TODO: text auslagern, zeile drunter auch
                            if (isViewAttached()) {
                                getView().displayMessage("This is not a valid website"); // TODO: auslagern
                            }
                            return;
                        }
                        parsedResponse = parsedResponse.replace("\\" + "n", "  \n");
                        parsedResponse = parsedResponse.replace("\\" + "\"", "\"");
                        String content = parsedResponse.substring(1, parsedResponse.length()-1);
                        downloadWebsite(name, urlOfWebsite, content);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("url", urlOfWebsite); // TODO: auslagern
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
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
