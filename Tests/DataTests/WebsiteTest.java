package internetofeveryone.ioe.DataTests;

import org.junit.Before;
import org.junit.Test;

import internetofeveryone.ioe.Data.Website;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class WebsiteTest {

    String name;
    String url;
    String content;

    @Before
    public void setUp() throws Exception {
        name = "name";
        url = "url";
        content = "content";
    }

    @Test
    public void createWebsite_NotEqualsNull() throws Exception {
        Website website = new Website(name, url, content);
        assertNotNull(website);
    }

    @Test
    public void createWebsite_correctGetName() throws Exception {
        Website website = new Website(name, url, content);
        assertEquals(website.getName(), name);
    }

    @Test
    public void createWebsite_correctGetUrl() throws Exception {
        Website website = new Website(name, url, content);
        assertEquals(website.getUrl(), url);
    }

    @Test
    public void createWebsite_correctGetContent() throws Exception {
        Website website = new Website(name, url, content);
        assertEquals(website.getContent(), content);
    }

    @Test
    public void setNameAndGetName_NameChanged() throws Exception {
        String newName = "differentName";
        Website website = new Website(name, url, content);
        website.setName(newName);
        assertEquals(website.getName(), newName);
    }

    @Test
    public void setUrlAndGetUrl_UrlChanged() throws Exception {
        String newUrl = "differentUrl";
        Website website = new Website(name, url, content);
        website.setUrl(newUrl);
        assertEquals(website.getUrl(), newUrl);
    }
    @Test
    public void setWebsiteAndGetWebsite_WebsiteChanged() throws Exception {
        String newContent = "differentContent";
        Website website = new Website(name, url, content);
        website.setContent(newContent);
        assertEquals(website.getContent(), newContent);
    }

}
