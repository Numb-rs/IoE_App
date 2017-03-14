package internetofeveryone.ioe.DefaultWebsitesTests;

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

import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Website;
import internetofeveryone.ioe.DefaultWebsites.DefaultWebsiteFragment;
import internetofeveryone.ioe.DefaultWebsites.DefaultWebsitePresenter;
import internetofeveryone.ioe.Model.WebsiteModel;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DefaultWebsitePresenterTest {

    private DefaultWebsitePresenter presenter;
    Context context;
    String[] defaultWebsiteURLs;
    String[] defaultWebsiteNames;
    String name, url, content;
    List<Website> websiteList;
    @Mock
    DefaultWebsiteFragment fragment;
    @Mock
    WebsiteModel model;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        MockitoAnnotations.initMocks(this);
        name = "name";
        url = "url";
        content = "url";
        Website website = new Website(name, url, content);
        websiteList = new ArrayList<>();
        websiteList.add(website);
        when(model.getAllDefaultWebsites()).thenReturn(websiteList);
        when(model.addDefaultWebsite(any(String.class), any(String.class), any(String.class))).thenReturn(true);
        defaultWebsiteURLs = new String[1];
        defaultWebsiteNames = new String[1];
        defaultWebsiteURLs[0] = url;
        defaultWebsiteNames[0] = name;
        presenter = new DefaultWebsitePresenter(context);
        presenter.setModel(model);
        presenter.attachView(fragment);
    }

    @Test
    public void getDefaultWebsiteURLs_modelGetAllDefaultWebsitesCalledAndReturnsCorrectValues() throws Exception {
        String[] returnedList = presenter.getDefaultWebsiteURLs();
        verify(model).getAllDefaultWebsites();
        assertArrayEquals(returnedList, defaultWebsiteURLs);
    }

    @Test
    public void getDefaultWebsiteNames_modelGetAllDefaultWebsitesCalledAndReturnsCorrectValues() throws Exception {
        String[] returnedList = presenter.getDefaultWebsiteNames();
        verify(model).getAllDefaultWebsites();
        assertArrayEquals(returnedList, defaultWebsiteNames);
    }

    @Test
    public void onClickAdd_viewOnAddDefaultWebsiteCalled() throws Exception {
        presenter.onClickAdd();
        verify(fragment).onAddDefaultWebsite();
    }

    @Test
    public void onClickAddWebsite_modelAddDefaultWebsiteCalledWithCorrectParameters() throws Exception {
        presenter.onClickAddWebsite(name, url);
        verify(model).addDefaultWebsite(name, url, "");
    }

    @Test
    public void onClickDefaultWebsite_viewOnEditDefaultWebsiteCalledWithCorrectParameters() throws Exception {
        presenter.onClickDefaultWebsite(0);
        verify(fragment).onEditDefaultWebsite(0);
    }

    @Test
    public void onClickSaveChange_modelDeleteAndAddDefaultWebsite() throws Exception {
        String newUrl = "newUrl";
        presenter.onClickSaveChange(url, name, newUrl);
        verify(model).deleteDefaultWebsite(url);
        verify(model).addDefaultWebsite(name, newUrl, "");
    }

    @Test
    public void onClickDelete_modelDeleteDefaultWebsiteCalledWithCorrectParameter() throws Exception {
        presenter.onClickDelete(url);
        verify(model).deleteDefaultWebsite(url);
    }

    @Test
    public void updateWithDataTypeWebsite_viewDataChanged() throws Exception {
        presenter.update(DataType.WEBSITE);
        verify(fragment).dataChanged();
    }
}
