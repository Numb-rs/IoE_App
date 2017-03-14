package internetofeveryone.ioe.WebsiteTests;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Model.WebsiteModel;
import internetofeveryone.ioe.Website.WebsiteActivity;
import internetofeveryone.ioe.Website.WebsitePresenter;

import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class WebsitePresenterTest {

    private WebsitePresenter presenter;
    Context context;
    @Mock
    WebsiteActivity activity;
    @Mock
    WebsiteModel model;

    String name, url, content;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        MockitoAnnotations.initMocks(this);
        presenter = new WebsitePresenter(context);
        presenter.setModel(model);
        presenter.attachView(activity);
    }

    @Test
    public void notNull() throws Exception {
        assertNotNull(presenter);
    }
}
