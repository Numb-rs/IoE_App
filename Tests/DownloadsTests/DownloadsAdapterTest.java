package internetofeveryone.ioe.DownloadsTests;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Downloads.DownloadsAdapter;
import internetofeveryone.ioe.R;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21) // , manifest = "/src/main/AndroidManifest.xml"
public class DownloadsAdapterTest {

    DownloadsAdapter downloadsAdapter;
    String data1 = "data1";
    String data2 = "data2";

    @Before
    public void setUp() throws Exception {
        ArrayList<String> data = new ArrayList<>();
        data.add(data1);
        data.add(data2);
        downloadsAdapter = new DownloadsAdapter(data, RuntimeEnvironment.application);
    }

    @Test
    public void getFirstItem_returnsCorrectItem() throws Exception {
        assertEquals(downloadsAdapter.getItem(0), data1);
    }

    @Test
    public void getCount_returnsCorrectCount() throws Exception {
        assertEquals(downloadsAdapter.getCount(), 2);
    }

    @Test
    public void getItemId_returns0() throws Exception {
        assertEquals(downloadsAdapter.getItemId(0), 0);
    }

    @Test
    public void getView_returnsCorrectViewWithCorrectValues() throws Exception {
        ListView parent = new ListView(RuntimeEnvironment.application);
        View view = downloadsAdapter.getView(0, null, parent);

        TextView tvWebsiteName = (TextView) view.findViewById(R.id.downloaded_website_name);
        Button openButton = (Button) view.findViewById(R.id.button_open_downloaded_website);
        Button deleteButton = (Button) view.findViewById(R.id.button_delete_downloaded_website);

        assertNotNull("View is null. ", view);
        assertNotNull("tvWebsiteName is null. ", tvWebsiteName);
        assertNotNull("openButton is null. ", openButton);
        assertNotNull("deleteButton is null. ", deleteButton);
        assertEquals("tvWebsiteNames don't match.", data1, tvWebsiteName.getText());
    }
}
