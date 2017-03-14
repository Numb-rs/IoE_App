package internetofeveryone.ioe.MessengerTests;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.TreeMap;

import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Message;
import internetofeveryone.ioe.Messenger.MessengerActivity;
import internetofeveryone.ioe.Messenger.MessengerPresenter;
import internetofeveryone.ioe.Model.MessageModel;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MessengerPresenterTest {

    private MessengerPresenter presenter;
    Context context;
    @Mock
    MessengerActivity activity;
    @Mock
    MessageModel model;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        MockitoAnnotations.initMocks(this);
        presenter = new MessengerPresenter(context);
        presenter.setModel(model);
        presenter.attachView(activity);
    }

    @Test
    public void onChatClicked_viewOpenChatCalledWithCorrectParameter() throws Exception {
        Contact contact = new Contact("name", "userCode", "key", true);
        Chat chat = new Chat(contact, new TreeMap<Long, Message>(), false);
        presenter.onChatClicked(chat);
        verify(activity).openChat(chat.getContact());
    }

    @Test
    public void updateWithDataTypeMessage_viewDataChanged() throws Exception {
        presenter.update(DataType.MESSAGE);
        verify(activity).dataChanged();
    }

    @Test
    public void updateWithDataTypeChat_viewDataChanged() throws Exception {
        presenter.update(DataType.CHAT);
        verify(activity).dataChanged();
    }
}
