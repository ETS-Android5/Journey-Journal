package ws.billy.app.Code.Utility;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.sql.Time;

public class Journal {

    /*
    Setters and getters below are for firebase to use when it uses the recycler view
    And creates a load of journals, not having these confuses it and breaks it.
     */

    public void setId(Long id) {
        this.id = id;
    }

    public String getProurl() {
        return prourl;
    }

    public void setProurl(String prourl) {
        this.prourl = prourl;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public GeoPoint getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(GeoPoint geolocation) {
        this.geolocation = geolocation;
    }


    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /*
    Private variables that belong to each Journal entry.
     */

    private Long id;
    private String prourl;
    private String url;
    private String text;
    private String userid;
    private Timestamp timestamp;
    private GeoPoint geolocation;

    public Journal(){
        // Add to the list of journals
        JournalManager.journals.add(this);
    }


    // get the string version of an entry, mainly for debugging
    @Override
    public String toString() {
       return id + " " + prourl + " " + text + " " + userid + " " + timestamp + " " + geolocation;
    }


}
