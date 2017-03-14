package internetofeveryone.ioe.AddChatTests;

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

import internetofeveryone.ioe.AddChat.AddChatFragment;
import internetofeveryone.ioe.AddChat.AddChatPresenter;
import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Model.MessageModel;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AddChatPresenterTest {

    private AddChatPresenter presenter;
    Context context;
    ArrayList<Contact> contactList;
    ArrayList<String> contactNames;
    String name, userCode, key;
    @Mock
    AddChatFragment fragment;
    @Mock
    MessageModel model;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        MockitoAnnotations.initMocks(this);
        name = "name";
        userCode = "3-3";
        key = "key";
        contactList = new ArrayList<>();
        Contact contact = new Contact(name, userCode, key, false);
        contactList.add(contact);
        contactNames = new ArrayList<>();
        contactNames.add(contact.getName());
        when(model.getAllContacts()).thenReturn(contactList);
        presenter = new AddChatPresenter(context);
        presenter.setModel(model);
        presenter.attachView(fragment);
    }

    @Test
    public void getContactNames_modelGetAllContactsCalledAndReturnsCorrectValue() throws Exception {
        ArrayList<String> returnedList = presenter.getContactNames();
        verify(model).getAllContacts();
        assertEquals(returnedList, contactNames);
    }

    @Test
    public void addChat_modelGetAllContactsCalledAndUpdateContactAndAddChatOnValidContactsCalled() throws Exception {
        presenter.addChat(name);
        verify(model).getAllContacts();
        verify(model).updateContact(userCode, name, userCode, key, true);
        verify(model).addChat(userCode, false);
    }

    @Test
    public void updateWithDataTypeContact_viewDataChanged() throws Exception {
        presenter.update(DataType.CONTACT);
        verify(fragment).dataChanged();
    }
}
