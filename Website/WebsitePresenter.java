package internetofeveryone.ioe.Website;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
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
    private Context context;

    /**
     * Instantiates a new Website presenter.
     *
     * @param context
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

        if (tcpClient != null) {
            tcpClient.sendMessage("WEBREQU\0" + urlOfWebsite + "\u0004");
        }
    }

    /**
     * Sends a website request to the network
     */
    public void onSearchRequest(final String engine, final String searchTerm) {

        Log.d(TAG, "Engine passed: " + engine);
        Log.d(TAG, "Searchterm passed: " + searchTerm);
        String languageParameter = "{\"language\": \"" + Locale.getDefault().getDisplayLanguage() + "\"}";

        new Connect().execute("");

        if (tcpClient != null) {
            tcpClient.sendMessage("WEBSRCH\0" + searchTerm + "\0" + engine + "\0" + languageParameter + "\u0004");
        }
    }

    /**
     * fixes links and line breaks to adapt it to MarkdownView
     * @param jsonLine
     * @return
     */
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
            return context.getString(R.string.error_message_website);
        }
    }

    public class Connect extends AsyncTask<String, String, TcpClient> {

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

            if (parsedResponse.equals(context.getString(R.string.error_message_website))) {
                if (isViewAttached()) {
                    getView().displayMessage(context.getString(R.string.not_valid_website_without_error));
                } else {
                    // error handling
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
