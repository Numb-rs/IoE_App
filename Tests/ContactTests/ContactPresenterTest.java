package internetofeveryone.ioe.ContactTests;

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
import internetofeveryone.ioe.Contact.ContactFragment;
import internetofeveryone.ioe.Contact.ContactPresenter;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Model.MessageModel;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ContactPresenterTest {

    private ContactPresenter presenter;
    Context context;
    String[] contactNames, contactUserCodes, contactKeys;
    String name, userCode, key;
    boolean openChat;
    List<Contact> contactList;
    @Mock
    ContactFragment fragment;
    @Mock
    MessageModel model;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        MockitoAnnotations.initMocks(this);
        name = "name";
        userCode = "1-1";
        key = "key";
        openChat = false;
        Contact contact = new Contact(name, userCode, key, openChat);
        contactList = new ArrayList<>();
        contactList.add(contact);
        contactNames = new String[1];
        contactUserCodes = new String[1];
        contactKeys = new String[1];
        contactNames[0] = name;
        contactUserCodes[0] = userCode;
        contactKeys[0] = key;
        when(model.getAllContacts()).thenReturn(contactList);
        when(model.getContactByID(userCode)).thenReturn(contact);
        when(model.updateContact(any(String.class), any(String.class), any(String.class), any(String.class), any(Boolean.class))).thenReturn(true);
        when(fragment.onEditContact(any(Integer.class))).thenReturn(null);
        when(fragment.onAddContact()).thenReturn(null);
        when(model.deleteContact(userCode)).thenReturn(0);
        presenter = new ContactPresenter(context);
        presenter.setModel(model);
        presenter.attachView(fragment);
    }

    @Test
    public void getContactNames_modelGetAllContactsCalledAndReturnsCorrectValues() throws Exception {
        String[] returnedList = presenter.getContactNames();
        verify(model).getAllContacts();
        assertArrayEquals(returnedList, contactNames);
    }

    @Test
    public void getContactUserCodes_modelGetAllContactsCalledAndReturnsCorrectValues() throws Exception {
        String[] returnedList = presenter.getContactUserCodes();
        verify(model).getAllContacts();
        assertArrayEquals(returnedList, contactUserCodes);
    }

    @Test
    public void getContactKeys_modelGetAllContactsCalledAndReturnsCorrectValues() throws Exception {
        String[] returnedList = presenter.getContactKeys();
        verify(model).getAllContacts();
        assertArrayEquals(returnedList, contactKeys);
    }

    @Test
    public void onClickSaveChange_modelGetContactByIDAndUpdateContactCalledWithCorrectParameters() throws Exception {
        String newUserCode = "newUserCode";
        String newName = "newName";
        String newKey = "newKey";
        presenter.onClickSaveChange(userCode, newUserCode, newName, newKey);
        verify(model).getContactByID(userCode);
        verify(model).updateContact(userCode, newName, newUserCode, newKey, openChat);
    }

    @Test
    public void addContact_modelAddContactCalledWithCorrectParameters() throws Exception {
        presenter.addContact(name, userCode, key);
        verify(model).addContact(name, userCode, key, false);
    }

    @Test
    public void onClickContact_viewOnEditContactCalledWithCorrectParameter() throws Exception {
        presenter.onClickContact(0);
        verify(fragment).onEditContact(0);
    }

    @Test
    public void onClickDelete_modelDeleteContactCalledWithCorrectParameter() throws Exception {
        presenter.onClickDelete(userCode);
        verify(model).deleteContact(userCode);
    }

    @Test
    public void onClickAdd_viewOnAddContactCalled() throws Exception {
        presenter.onClickAdd();
        verify(fragment).onAddContact();
    }

    @Test
    public void updateWithDataTypeWebsite_viewDataChanged() throws Exception {
        presenter.update(DataType.CONTACT);
        verify(fragment).dataChanged();
    }
}
