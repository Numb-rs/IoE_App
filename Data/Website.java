package internetofeveryone.ioe.Data;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class represents the custom data type Website
 */
public class Website {

    private String name; // name of the website
    private String url; // URL of the website
    private String content; // content of the website

    /**
     * Instantiates a new Website.
     *
     * @param name
     * @param url
     * @param content
     */
    public Website(String name, String url, String content) {
        this.name = name;
        this.url = url;
        this.content = content;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }
}
