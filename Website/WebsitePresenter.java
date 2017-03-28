package internetofeveryone.ioe.Website;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Locale;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.BrowsingPresenter;
import internetofeveryone.ioe.R;
import internetofeveryone.ioe.Requests.TcpClient;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Website page
 */
public class WebsitePresenter extends BrowsingPresenter<WebsiteView> {

    private TcpClient tcpClient;
    private static final String TAG = "WebsitePresenter";
    private final Context context;

    /**
     * Instantiates a new Website presenter.
     *
     * @param context the context
     */
    public WebsitePresenter(Context context) {
        super(context);
        this.context = context;
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

        new Connect().execute("");

        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                if (tcpClient != null) {
                    if (!tcpClient.sendMessage(getModel().getUserCode() + "\0WEBREQU\0" + urlOfWebsite + "\u0004")) {
                        Log.e(TAG, "url request didn't work due to connection issues");
                        getView().displayNetworkErrorMessage();
                    }
                } else {
                    Log.e(TAG, "TcpClient is null");
                }
            }
        };
        handler.postDelayed(r, 2000); // 2 seconds
        return;
    }

    /**
     * Sends a website request to the network
     */
    public void onSearchRequest(final String engine, final String searchTerm) {

        Log.d(TAG, "Engine passed: " + engine);
        Log.d(TAG, "Searchterm passed: " + searchTerm);
        final String languageParameter = "{\"language\": \"" + Locale.getDefault().getDisplayLanguage() + "\"}";

        new Connect().execute("");

        getView().displayLoader();

        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                if (tcpClient != null) {
                    if (!tcpClient.sendMessage(getModel().getUserCode() + "\0WEBSRCH\0" + searchTerm + "\0" + engine + "\0" + languageParameter + "\u0004")) {
                        Log.e(TAG, "search didn't work due to connection issues");
                        getView().displayNetworkErrorMessage();
                    }
                } else {
                    Log.e(TAG, "TcpClient is null");
                }

            }
        };
        handler.postDelayed(r, 2000); // 2 seconds
        getView().closeLoader();

        return;
    }

    /**
     * fixes links and line breaks to adapt it to MarkdownView
     * @param jsonLine the json response
     * @return the parsed response
     */
    private String parse(String jsonLine) {
        try {
            JsonElement jelement = new JsonParser().parse(jsonLine);
            JsonObject jobject = jelement.getAsJsonObject();
            JsonObject jdataObject = jobject.getAsJsonObject("data");
            JsonObject jobject2 = jdataObject.getAsJsonObject("attributes");
            return jobject2.get("markdown").toString();
        } catch (Exception e) {
            return context.getString(R.string.error_message_website);
        }
    }

    private class Connect extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {
            Log.d(TAG, "started background task");

            // we create a TCPClient object
            tcpClient = new TcpClient(new TcpClient.OnMessageReceived() {

                @Override
                public void messageReceived(String message) {
                    publishProgress(message);
                }
            });
            if (!tcpClient.run()) {
                Log.e(TAG, "network offline");
                getView().displayNetworkErrorMessage();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);

            String response = values[0];
            Log.d(TAG, "response " + response);

            String parsedResponse = parse(response); // markdown

            if (parsedResponse.equals(context.getString(R.string.error_message_website))) {
                if (isViewAttached()) {
                    getView().displayMessage(context.getString(R.string.not_valid_website_without_error));
                }
                tcpClient.stopClient();
                return;
            }

            // change links and line breaks into the right format for MarkdownView
            parsedResponse = parsedResponse.replace("\\" + "n", "  \n");
            parsedResponse = parsedResponse.replace("\\" + "\"", "\"");
            String content = parsedResponse.substring(1, parsedResponse.length()-1);
            getView().displayWebsite(content);
            tcpClient.stopClient();
        }
    }

}
