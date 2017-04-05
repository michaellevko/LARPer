package com.example.user.larper.Model;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by event on 01/04/2017.
 */

final public class ModelFirebaseRealtime {
    private ModelFirebaseRealtime() {
    }

    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String BLUEPRINTS_TABLE_NAME = "blueprints";

    public interface FirebaseRealtimeListener {
        void complete(boolean result);
    }

    public interface FirebaseReadContactListener {
            void complete(StaticProfile profile);
    }

    public interface FirebaseReadBlueprintsListener {
        void complete(ArrayList<Blueprint> blueprints);
    }

    public static void saveContact(StaticProfile profile, final FirebaseRealtimeListener listener) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        // save the contact to database
        mDatabase.child(CONTACTS_TABLE_NAME).
                child(profile.getGoogle_id()).
                setValue(profile, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError e, DatabaseReference r) {
                        listener.complete(e == null);
                    }
                });
    }

    public static String blueprintToJson(Blueprint blueprint, String owner)
    {
        BlueprintAndOwner blueprintowner = new BlueprintAndOwner(
                                            blueprint,
                                            owner);
        Gson gson = new Gson();
        return gson.toJson(blueprintowner);
    }

    public static void saveBlueprint(BlueprintAndOwner blueprint, final FirebaseRealtimeListener listener) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        // save the contact to database
        mDatabase.child(BLUEPRINTS_TABLE_NAME).
                child(UUID.randomUUID().toString()).
                setValue(blueprint, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError e, DatabaseReference r) {
                        listener.complete(e == null);
                    }
                });
    }

    public static void findBlueprintsByOwner(String owner, final FirebaseReadBlueprintsListener listener) {
        final String OWNER_FIELD = "owner";
        final ArrayList<Blueprint> blueprints = new ArrayList<Blueprint>();

        try
        {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            Query contact = mDatabase.
                    child(BLUEPRINTS_TABLE_NAME).orderByChild(OWNER_FIELD).equalTo(owner);

            ValueEventListener contactListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get contact object
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren())
                    {
                        blueprints.add(new Blueprint(childSnapshot.getValue(BlueprintAndOwner.class)));
                    }
                    listener.complete(blueprints);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting contact failed, log a message
                    Log.d("tag", "", databaseError.toException());
                    listener.complete(null);
                }
            };
            contact.addListenerForSingleValueEvent(contactListener);
        }
        catch(Exception e)
        {
            listener.complete(null);
        }
    }

    public static void findContactByName(String name, final FirebaseReadContactListener listener) {
        final String NICKNAME_FIELD = "nickname";
        try
        {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            Query contact = mDatabase.
                    child(CONTACTS_TABLE_NAME).orderByChild(NICKNAME_FIELD).equalTo(name);

            ValueEventListener contactListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get contact object
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren())
                    {
                        StaticProfile profile = childSnapshot.getValue(StaticProfile.class);
                        listener.complete(profile);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting contact failed, log a message
                    Log.d("tag", "", databaseError.toException());
                    listener.complete(null);
                }
            };
            contact.addListenerForSingleValueEvent(contactListener);
        }
        catch(Exception e)
        {
            listener.complete(null);
        }
    }

}
