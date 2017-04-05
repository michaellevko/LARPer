package com.example.user.larper;

import android.app.ListFragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.larper.Model.Blueprint;
import com.example.user.larper.Model.BlueprintAndOwner;
import com.example.user.larper.Model.Model;
import com.example.user.larper.Model.ModelFirebase;
import com.example.user.larper.Model.ModelFirebaseRealtime;
import com.example.user.larper.Model.ModelSqlite;
import com.example.user.larper.Model.StaticProfile;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;


public class BlueprintsFragment extends ListFragment {

    private OnBlueprintFragmentListener mListener;
    BlueprintAdapter adapter = new BlueprintAdapter();
    ArrayList<Blueprint> bpList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blueprints, container, false);

        // Set list
        Model.getInstance().setBlueprints(this.getOwnerBlueprints());

        bpList = Model.getInstance().getBlueprints();
        setListAdapter(adapter);

        ((Button)view.findViewById(R.id.blueprint_new_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoNewBlueprintInterface();
            }
        });

        return view;
    }

    public ArrayList<Blueprint> getOwnerBlueprints()
    {
        ModelSqlite sql = new ModelSqlite(this.getContext());
        return sql.getOwnerBlueprints();

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBlueprintFragmentListener) {
            mListener = (OnBlueprintFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInitProfileFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnBlueprintFragmentListener {
        void gotoNewBlueprintInterface();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public class BlueprintAdapter extends BaseAdapter {

        ArrayList<StaticProfile> contacts;
        Spinner spinner;

        @Override
        public int getCount() {
            return bpList.size();
        }

        @Override
        public Object getItem(int position) {
            return bpList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void shareBlueprint(int position) {
            Blueprint bp = bpList.get(position);

            ModelFirebaseRealtime.saveBlueprint(new BlueprintAndOwner(bp,
                    spinner.getSelectedItem().toString()),
                    new ModelFirebaseRealtime.FirebaseRealtimeListener() {
                @Override
                public void complete(boolean result) {
                    if (result) {
                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                "Successfully shared blueprint",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                "Failed sharing blueprint",
                                Toast.LENGTH_SHORT).show();
                    }
                    spinner.setVisibility(View.INVISIBLE);
                }
            });
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.blueprint_row_details, null);
                ImageButton bpShare = (ImageButton)convertView.findViewById(R.id.blueprint_share_btn);
                Drawable shareImg = getResources().getDrawable(R.drawable.share_icon);
                shareImg.setBounds(0, 0, 40, 40);
                bpShare.setImageDrawable(shareImg);

                // init contact spinner adapter
                contacts = new ArrayList<StaticProfile>();
                final ArrayAdapter adapter = new ArrayAdapter(
                        getActivity().getBaseContext(),
                        android.R.layout.simple_spinner_item,
                        contacts);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // define spinner
                spinner = ((Spinner) convertView.findViewById(R.id.spinner2));
                spinner.setVisibility(View.INVISIBLE);
                spinner.setAdapter(adapter);

                // handle spinner contact selection
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                        spinner.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                        shareBlueprint(position);
                    }
                });

                bpShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spinner.setVisibility(View.VISIBLE);
                    }
                });
            }
            Blueprint bp = bpList.get(position);
            ((TextView)convertView.findViewById(R.id.blueprint_name_tv)).setText(bp.getName());
            ((TextView)convertView.findViewById(R.id.blueprint_crafting_time_tv)).setText(bp.getCraftingTime());
            ((TextView)convertView.findViewById(R.id.blueprint_total_cost_tv)).setText(bp.getTotalCost());

            return convertView;
        }
    }


}