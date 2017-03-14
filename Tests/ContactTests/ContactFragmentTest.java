package internetofeveryone.ioe.ContactTests;

import android.widget.ArrayAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Contact.ContactFragment;
import internetofeveryone.ioe.Contact.ContactPresenter;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ContactFragmentTest {

    ContactFragment fragment;
    @Mock
    ArrayAdapter<String> adapter;
    @Mock
    ContactPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        fragment = new ContactFragment();
    }

    @Test
    public void notNull() throws Exception {
        startFragment( fragment );
        assertNotNull( fragment );
    }

    @Test
    public void setAdapter_adapterChanged() throws Exception {
        ArrayAdapter<String> oldAdapter = fragment.getAdapter();
        fragment.setAdapter(adapter);
        ArrayAdapter<String> newAdapter = fragment.getAdapter();
        assertNotEquals(oldAdapter, newAdapter);
    }

}
