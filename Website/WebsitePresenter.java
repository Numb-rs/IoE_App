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

public class WebsitePresenter extends MvpPresenter<WebsiteView> {

    private Context context;
    private RequestQueue queue;

    public WebsitePresenter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void update(DataType type, String id) {

    }

    public void onURLPassed(String url) {
        // request website

        queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        if (isViewAttached()) {
                            getView().displayWebsite("Response is: " + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isViewAttached()) {
                    getView().displayErrorMessage();
                }

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void linkClicked(String url) {
        if (isViewAttached()) {
            getView().displayWebsite(url);
        }
    }

}
