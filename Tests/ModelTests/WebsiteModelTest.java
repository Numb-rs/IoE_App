package internetofeveryone.ioe.ModelTests;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Website;
import internetofeveryone.ioe.Model.WebsiteModel;
import internetofeveryone.ioe.Presenter.ModelObserver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class WebsiteModelTest {

    private WebsiteModel model;
    Context context;
    String name, url, content;
    @Mock
    ModelObserver o;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        MockitoAnnotations.initMocks(this);
        doNothing().when(o).update(any(DataType.class));
        name = "name";
        url = "url";
        content = "content";
        model = new WebsiteModel(context);
        model.addObserver(o);
        model.open();
    }

    @Test
    public void addDownloadedWebsite_getsAddedToDatabaseAndObserversGetNotified() throws Exception {
        model.addDownloadedWebsite(name, url, content);
        verify(o).update(DataType.WEBSITE);
        assertNotNull(model.getDownloadedWebsiteByURL(url));
    }

    @Test
    public void addDefaultWebsite_getsAddedToDatabaseAndObserversGetNotified() throws Exception {
        model.addDefaultWebsite(name, url, content);
        verify(o).update(DataType.WEBSITE);
        assertNotNull(model.getDefaultWebsiteByURL(url));
    }

    @Test
    public void deleteDefaultWebsite_getsDeletedFromDatabaseAndObserversGetNotified() throws Exception {
        model.addDefaultWebsite(name, url, content);
        model.deleteDefaultWebsite(url);
        verify(o, times(2)).update(DataType.WEBSITE);
        assertNull(model.getDefaultWebsiteByURL(url));
    }

    @Test
    public void deleteDownloadedWebsite_getsDeletedFromDatabaseAndObserversGetNotified() throws Exception {
        model.addDownloadedWebsite(name, url, content);
        model.deleteDownloadedWebsite(url);
        verify(o, times(2)).update(DataType.WEBSITE);
        assertNull(model.getDownloadedWebsiteByURL(url));
    }

    @Test
    public void updateDownloadedWebsite_getsUpdatedInDatabaseAndObserversGetNotified() throws Exception {
        String newUrl = "newUrl";
        String newName = "newName";
        String newContent = "newContent";
        model.addDownloadedWebsite(name, url, content);
        model.updateDownloadedWebsite(newName, url, newUrl, newContent);
        Website w = model.getDownloadedWebsiteByURL(newUrl);
        verify(o, times(2)).update(DataType.WEBSITE);
        assertEquals(w.getName(), newName);
        assertEquals(w.getUrl(), newUrl);
        assertEquals(w.getContent(), newContent);
    }

    @After
    public void tearDown() throws Exception {
        model.close();
    }
}