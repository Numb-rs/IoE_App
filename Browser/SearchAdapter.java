package internetofeveryone.ioe.Browser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import internetofeveryone.ioe.R;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class represents the custom adapter for the Search-ListView in BrowserActivity
 */
public class SearchAdapter extends BaseAdapter {
    private final ArrayList<String> data;
    private Context context;

    /**
     * Instantiates a new DownloadsAdapter.
     *
     * @param searchEngines data
     * @param context
     */
    public SearchAdapter(ArrayList<String> searchEngines, Context context) {
        this.context = context;
        data = searchEngines;
    }

    private static class ViewHolder { // holds the elements that make up one item of the list

        private TextView tvWebsiteName; // name of the Website
        private Button openButton; // button to open the Website
        private Button downloadButton; // button to download the Website
        private EditText searchTerm;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String name = getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.row_item_browser_search, parent, false);

            viewHolder.tvWebsiteName = (TextView) convertView.findViewById(R.id.search_website_name);
            viewHolder.downloadButton = (Button) convertView.findViewById(R.id.button_download_website_search);
            viewHolder.openButton = (Button) convertView.findViewById(R.id.button_open_website_search);
            viewHolder.searchTerm = (EditText) convertView.findViewById(R.id.editText_searchterm);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvWebsiteName.setText(name);
        viewHolder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof BrowserView){
                    // ((BrowserView)context).onClickDownloadSearch(name, viewHolder.searchTerm.getText().toString());
                } else {
                    // ErrorHandling
                }
            }
        });viewHolder.openButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(context instanceof BrowserView){
                    // ((BrowserView)context).onClickOpenSearch(name, viewHolder.searchTerm.getText().toString());
                } else {
                    // ErrorHandling
                }
            }
        });

        return convertView;
    }
}