package internetofeveryone.ioe.Website;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.MvpPresenter;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Website page
 */
public class WebsitePresenter extends MvpPresenter<WebsiteView> {

    private Context context;
    private RequestQueue queue; // queue for outgoing requests

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
    public void update(DataType type, String id) {}

    /**
     * Sends a website request to the network
     *
     * @param url URL of the requested Website
     */
    public void onURLPassed(String url) {

        queue = Volley.newRequestQueue(context);

        // request the content of the website
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (isViewAttached()) {
                            getView().displayWebsite(response); // Sends a request to the view to diplay the content
                        } else {
                            // ErrorHandling
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isViewAttached()) {
                    getView().displayErrorMessage(); // Sends a request to the view to diplay an error message
                }

            }
        });
        // add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Opens a new Website and saves the current Website in a FILO queue
     *
     * @param url URL of the new Website
     */
    public void linkClicked(String url) {
        if (isViewAttached()) {
            getView().displayWebsite(url);
            // TODO: speicher in FILO letzte Website um dann wieder zurückspringen zu können
        } else {
            // ErrorHandling
        }
    }

}
