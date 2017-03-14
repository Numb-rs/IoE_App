package internetofeveryone.ioe.BrowserTests;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import internetofeveryone.ioe.Browser.BrowserActivity;
import internetofeveryone.ioe.Browser.BrowserPresenter;
import internetofeveryone.ioe.Browser.BrowserView;
import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Website;
import internetofeveryone.ioe.Model.WebsiteModel;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BrowserPresenterTest {

    private BrowserPresenter presenter;
    Context context;
    String name, url, content;
    List<String> nameList;
    @Mock
    BrowserActivity activity;
    @Mock
    WebsiteModel model;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        MockitoAnnotations.initMocks(this);
        name = "name";
        url = "url";
        content = "content";
        Website website = new Website(name, url, content);
        List<Website> websiteList = new ArrayList<>();
        websiteList.add(website);
        nameList = new ArrayList<>();
        nameList.add(name);
        when(model.getAllDefaultWebsites()).thenReturn(websiteList);
        when(model.addDownloadedWebsite(any(String.class), any(String.class), any(String.class))).thenReturn(true);
        presenter = new BrowserPresenter(context);
        presenter.setModel(model);
        presenter.attachView(activity);
    }

    @Test
    public void getDefaultWebsiteNames_modelGetAllDefaultWebsitesCalledWithCorrectParameter() throws Exception {
        List<String> returnedList = presenter.getDefaultWebsiteNames();
        verify(model).getAllDefaultWebsites();
        assertEquals(returnedList, nameList);
    }

    @Test
    public void onOpenClickedSearch_viewSendSearchRequestCalledWithCorrectParameters() throws Exception {
        String google = "Google";
        String wikipedia = "Wikipedia";
        String weather = "Weather";
        String searchTerm = "testSearchTerm";
        presenter.onOpenClickedSearch(google, searchTerm);
        verify(activity).sendSearchRequest(google, searchTerm);
        presenter.onOpenClickedSearch(weather, searchTerm);
        verify(activity).sendSearchRequest(weather, searchTerm);
        presenter.onOpenClickedSearch(wikipedia, searchTerm);
        verify(activity).sendSearchRequest("wiki", searchTerm);
    }

    @Test
    public void onOpenClickedFavorite_modelGetAllDefaultWebsiteCalledWithCorrectParameter() throws Exception {
        presenter.onOpenClickedFavorite(name);
        verify(model).getAllDefaultWebsites();
        verify(activity).goToURL(url);
    }

    @Test
    public void onOpenClickedURL_viewGoToURLCalledWithCorrectParameters() throws Exception {
        presenter.onOpenClickedURL(url);
        verify(activity).goToURL(url);
    }

    @Test
    public void downloadWebsiteWith3Parameters_modelAddDownloadedWebsiteCalledWithCorrectParameters() throws Exception {
        presenter.detachView();
        presenter.downloadWebsite(name, url, content);
        verify(model).addDownloadedWebsite(name, url, content);
    }

    @Test
    public void enterURL_viewGoToURLCalledWithCorrectParameter() throws Exception {
        presenter.enterURL(url);
        verify(activity).goToURL(url);
    }

    @Test
    public void detachView_isNull() throws Exception {
        BrowserView old = presenter.getView();
        presenter.detachView();
        assertNotEquals(old, presenter.getView());
        assertNull(presenter.getView());
    }

    @Test
    public void updateWithDataTypeWebsite_viewDataChanged() throws Exception {
        presenter.update(DataType.WEBSITE);
        verify(activity).dataChanged();
    }
}
