package ws.billy.app.Code.Utility;

// I ended up getting really annoyed making like 5 functions in different classes, so
// here's a centralized journal manager.


import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JournalManager {

    // static list of journals, used for identifying which is clicked
    public static ArrayList<Journal> journals;

    // create a new journal in db (not locally)
    public static void newJournal(String imageUrl, String text, FirebaseUser fbuser, GeoPoint geolocation) {

        // get the db instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // get the collection of journals
        CollectionReference journals = db.collection("journals");

        // create a post id
        Long id = Math.round(Math.random()*10000);

        // create a new database entry (with hashmaps, nosql best)
        Map<String, Object> journal = new HashMap<>();
        journal.put("id", id);
        journal.put("prourl", fbuser.getPhotoUrl()); // this one should be removed, would be easier to save id and pull from db
        journal.put("url", imageUrl);
        journal.put("text", text);
        journal.put("userid", fbuser.getUid());
        journal.put("geolocation", geolocation);
        journal.put("timestamp", new Timestamp(new Date()));

        // Add a new document with a generated ID
        journals.document(id.toString()).set(journal);


    }

    // delete a journal
    public static Boolean deleteJournal(String userid, Journal journal) {

        // if the person who owns the post is the one deleting
        if(!userid.equals(journal.getUserid())) {
            System.out.println("Invalid user");
            return false;
        }

        // delete journal in the database
        System.out.println("deleting");
        FirebaseFirestore.getInstance().collection("journals").document(journal.getId().toString()).delete();

        // delete locally
        journals.remove(journal);

        // return if it was successful
        return true;

    }

    // edit a journal
    public static Journal editJournal(Journal journal, String text) {

        // update on the local application
        journal.setText(text);

        // Tell it that we're editing the text field
        Map<String, Object> edit = new HashMap<>();
        edit.put("text", text);

        // update in the database
        FirebaseFirestore.getInstance().collection("journals").document(journal.getId().toString()).update(edit);

        // return the new journal to be displayed
        return journal;

    }

}
