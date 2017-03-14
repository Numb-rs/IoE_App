package internetofeveryone.ioe.DownloadsTests;

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
import internetofeveryone.ioe.Downloads.DownloadsActivity;
import internetofeveryone.ioe.Downloads.DownloadsPresenter;
import internetofeveryone.ioe.Model.WebsiteModel;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DownloadsPresenterTest {

    private DownloadsPresenter presenter;
    Context context;
    @Mock
    DownloadsActivity activity;
    @Mock
    WebsiteModel model;

    String name, url, content;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        MockitoAnnotations.initMocks(this);

        name = "testName";
        url = "testUrl";
        content = "testContent";
        Website website = new Website(name, url, content);
        List<Website> list = new ArrayList<>();
        list.add(website);
        doReturn(list).when(model).getAllDownloadedWebsites();
        doNothing().when(activity).dataChanged();
        doNothing().when(activity).displayContent(any(String.class));

        presenter = new DownloadsPresenter(context);
        presenter.setModel(model);
        presenter.attachView(activity);
    }

    @Test
    public void deleteClicked_modelDeleteDownloadedWebsiteCalledWithCorrectParameter() throws Exception {
        presenter.deleteClicked(name);
        verify(model).deleteDownloadedWebsite(url);
    }

    @Test
    public void openClicked_viewDisplayContentCalledWithCorrectParameter() throws Exception {
        presenter.openClicked(name);
        verify(activity).displayContent(content);
    }

    @Test
    public void updateWithDataTypeWebsite_viewDataChanged() throws Exception {
        presenter.update(DataType.WEBSITE);
        verify(activity).dataChanged();
    }
}
