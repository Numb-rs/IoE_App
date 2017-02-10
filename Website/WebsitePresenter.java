package internetofeveryone.ioe.Website;

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

import java.util.HashMap;
import java.util.Map;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.BrowsingPresenter;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Website page
 */
public class WebsitePresenter extends BrowsingPresenter<WebsiteView> {

    /**
     * Instantiates a new Website presenter.
     *
     * @param context
     */
    public WebsitePresenter(Context context) {
        super(context);
    }

    @Override
    public void update(DataType type) {} // doesn't have to update anything

    /**
     * Sends a website request to the network
     *
     * @param urlOfWebsite URL of the requested Website
     */
    public void onURLPassed(final String urlOfWebsite) {

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
                                getView().displayMessage("This is not a valid website");
                            } else {
                                // error handling
                            }
                            return;
                        }
                        parsedResponse = parsedResponse.replace("\\" + "n", "  \n");
                        parsedResponse = parsedResponse.replace("\\" + "\"", "\"");
                        getView().displayWebsite(parsedResponse.substring(1, parsedResponse.length()-1));
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
                params.put("url", urlOfWebsite);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    /**
     * Sends a website request to the network
     */
    public void onSearchRequest(final String engine, final String searchTerm) {

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
                                getView().displayMessage("This is not a valid website");
                            } else {
                                // error handling
                            }
                            return;
                        }
                        parsedResponse = parsedResponse.replace("\\" + "n", "  \n");
                        parsedResponse = parsedResponse.replace("\\" + "\"", "\"");
                        String content = parsedResponse.substring(1, parsedResponse.length()-1);
                        getView().displayWebsite(content);
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

}
