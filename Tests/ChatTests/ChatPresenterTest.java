package internetofeveryone.ioe.ChatTests;

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
import internetofeveryone.ioe.Chat.ChatActivity;
import internetofeveryone.ioe.Chat.ChatPresenter;
import internetofeveryone.ioe.Chat.ChatView;
import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Message;
import internetofeveryone.ioe.Model.MessageModel;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChatPresenterTest {

    private ChatPresenter presenter;
    Context context;
    TreeMap<Long, Message> msgList;
    String userCode;
    Contact contact;
    @Mock
    ChatActivity activity;
    @Mock
    MessageModel model;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        MockitoAnnotations.initMocks(this);
        String userCode = "5-5";
        msgList = new TreeMap<>();
        Message msg = new Message(3, "1-1", "2-2", "content", false, "2-2");
        msgList.put(3L, msg);
        contact = new Contact("name", userCode, "key", true);
        Chat chat = new Chat(contact, msgList, false);
        when(model.getChatByID(any(String.class))).thenReturn(chat);
        when(model.getContactByID(any(String.class))).thenReturn(contact);
        presenter = new ChatPresenter(context);
        presenter.setModel(model);
        presenter.attachView(activity);
    }

    @Test
    public void getContact_modelGetContactByIDCalledWithCorrectParameter() throws Exception {
        presenter.getContact(userCode);
        verify(model).getContactByID(userCode);
    }

    @Test
    public void getContactName_returnsCorrectName() throws Exception {
        String returnedName = presenter.getContactName(userCode);
        assertEquals(returnedName, contact.getName());
    }

    @Test
    public void encryptChanged_modelUpdateChatCalledWithCorrectParameter() throws Exception {
        boolean isChecked = false;
        presenter.encryptChanged(userCode, isChecked);
        verify(model).updateChat(userCode, isChecked);
    }

    @Test
    public void isChatEncrypted_modelGetChatByIDCalledWithCorrectParameter() throws Exception {
        presenter.isChatEncrypted(userCode);
        verify(model).getChatByID(userCode);
    }

    @Test
    public void updateWithDataTypeMessage_viewDataChanged() throws Exception {
        presenter.update(DataType.MESSAGE);
        verify(activity).dataChanged();
    }

    @Test
    public void detachView_isNull() throws Exception {
        ChatView old = presenter.getView();
        presenter.detachView();
        assertNotEquals(old, presenter.getView());
        assertNull(presenter.getView());
    }

    @Test
    public void getMessageList_returnsCorrectMessageList() throws Exception {
        TreeMap<Long, Message> returnedList = presenter.getMessageList(userCode);
        verify(model).getChatByID(userCode);
        assertEquals(returnedList, msgList);
    }
}
