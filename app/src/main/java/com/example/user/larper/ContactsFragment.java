package com.example.user.larper;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.user.larper.Model.ModelFirebase;
import com.example.user.larper.Model.ModelFirebaseRealtime;
import com.example.user.larper.Model.ModelSqlite;
import com.example.user.larper.Model.StaticProfile;
import com.example.user.larper.Model.StaticProfilesSql;

import java.util.ArrayList;


public class ContactsFragment extends Fragment {

    ArrayList<StaticProfile> contacts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_contacts, container, false);

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
                        }
                    }
                });

            }
        });

            return view;
    }

    public void saveContactToSql(StaticProfile profile)
    {
        //StaticProfilesSql.writeContact();
    }
}
