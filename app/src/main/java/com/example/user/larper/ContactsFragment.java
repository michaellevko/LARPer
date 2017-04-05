package com.example.user.larper;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.larper.Model.Blueprint;
import com.example.user.larper.Model.ModelFirebase;
import com.example.user.larper.Model.ModelFirebaseRealtime;
import com.example.user.larper.Model.ModelSqlite;
import com.example.user.larper.Model.StaticProfile;
import com.example.user.larper.Model.StaticProfilesSql;

import java.io.File;
import java.util.ArrayList;


public class ContactsFragment extends Fragment {

    Activity activity;
    ArrayList<StaticProfile> contacts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        activity = this.getActivity();
        contacts = this.getOwnerContacts();

        if(contacts == null)
            contacts = new ArrayList<StaticProfile>();

        final ArrayAdapter adapter = new ArrayAdapter(
                    this.getActivity().getBaseContext(),
                    android.R.layout.simple_list_item_1,
                    contacts);

        ListView listView = ((ListView)view.findViewById(R.id.listView1));
        listView.setAdapter(adapter);

        // set listener for new button
        final Button button = (Button) view.findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText search = (EditText)view.findViewById(R.id.editText);
                String search_name = search.getText().toString();
                ModelFirebaseRealtime.findContactByName(
                        search_name,
                        new ModelFirebaseRealtime.FirebaseReadContactListener() {
                    @Override
                    public void complete(StaticProfile profile)
                    {
                        if(profile != null)
                        {
                            contacts.add(contacts.size(), profile);
                            adapter.notifyDataSetChanged();
                            saveContactToSql(profile);
                            Toast.makeText(
                                    activity.getBaseContext(),
                                    "Successfully saved contact",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        // set listener for check data button
        final Button button_shared_data = (Button) view.findViewById(R.id.button7);
        button_shared_data.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // load image data
                loadObjectivesData();
                // load blueprint data
                loadBlueprintsData();
            }
        });

            return view;
    }

    private void loadBlueprintsData()
    {
        // load blueprint data
        ModelFirebaseRealtime.findBlueprintsByOwner(
                StaticProfilesSql.curr_owner.toString(),
                new ModelFirebaseRealtime.FirebaseReadBlueprintsListener() {
                    @Override
                    public void complete(ArrayList<Blueprint> blueprints)
                    {
                        if(blueprints.size() > 0)
                        {
                            ModelSqlite sql = new ModelSqlite(getContext());
                            for (Blueprint blueprint : blueprints)
                            {
                                sql.saveBlueprint(
                                        blueprint,
                                        StaticProfilesSql.curr_owner.toString());
                            }
                            Toast.makeText(
                                    activity.getBaseContext(),
                                    "Successfully saved new blueprints",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(
                                    activity.getBaseContext(),
                                    "No blueprints to load",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadObjectivesData()
    {
        final ArrayList<File> result = new ArrayList<File>();
        ModelFirebase.loadFileByOwner(StaticProfilesSql.curr_owner.toString(),
                       new ModelFirebase.FirebaseLoadListener()
                       {
                           @Override
                           public void complete(File file)
                           {
                              if (file != null)
                              {
                                  Toast.makeText(
                                          activity.getBaseContext(),
                                          "Successfully saved a new objective",
                                          Toast.LENGTH_SHORT).show();
                              }
                              else
                              {
                                  Toast.makeText(
                                          activity.getBaseContext(),
                                          "No objective to load",
                                          Toast.LENGTH_SHORT).show();
                              }
                           }
                       });
    }

    public void saveContactToSql(StaticProfile contact)
    {
        ModelSqlite sql = new ModelSqlite(this.getContext());
        sql.saveContact(contact);
    }

    public ArrayList<StaticProfile> getOwnerContacts()
    {
        ModelSqlite sql = new ModelSqlite(this.getContext());
        return sql.getOwnerContacts();
    }
}
