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
import com.example.user.larper.Model.StaticProfilesSql;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;


public class BlueprintsFragment extends ListFragment {

    private OnBlueprintFragmentListener mListener;
    BlueprintAdapter adapter = new BlueprintAdapter();
    ArrayList<Blueprint> bpList;
    Spinner spinner;

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

        // init contact spinner adapter
        ArrayList<StaticProfile> contacts = new ArrayList<StaticProfile>();
        ModelSqlite sql = new ModelSqlite(this.getContext());
        contacts = sql.getOwnerContacts();
        final ArrayAdapter Arrayadapter = new ArrayAdapter(
                getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item,
                contacts);
        Arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // define spinner
        spinner = ((Spinner) view.findViewById(R.id.spinner2));
        spinner.setAdapter(Arrayadapter);
        spinner.setVisibility(View.INVISIBLE);

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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final Blueprint bp = bpList.get(position);

            final ArrayList<Integer> spinnerCount = new ArrayList<>();
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.blueprint_row_details, null);
            }

            ImageButton bpShare = (ImageButton)convertView.findViewById(R.id.blueprint_share_btn);

            // handle spinner contact selection
            if (spinner.getOnItemClickListener() == null) {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        spinner.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        if (spinnerCount.size() > 0)
                        {
                            shareBlueprint(position);
                        }
                        else
                        {
                            spinnerCount.add(1);
                        }
                    }
                });
            }

            bpShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spinner.setVisibility(View.VISIBLE);
                }
            });

            ((ImageButton)convertView.findViewById(R.id.blueprint_delete_btn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bpList.remove(bp);
                    adapter.notifyDataSetChanged();
                    ModelSqlite sql = new ModelSqlite(getContext());
                    sql.deleteBlueprint(bp);
                }
            });

            ((TextView)convertView.findViewById(R.id.blueprint_name_tv)).setText(bp.getName());
            ((TextView)convertView.findViewById(R.id.blueprint_crafting_time_tv)).setText(bp.getCraftingTime());
            ((TextView)convertView.findViewById(R.id.blueprint_total_cost_tv)).setText(bp.getTotalCost());

            return convertView;
        }
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
                                    getActivity().getBaseContext(),
                                    "Successfully shared blueprint",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(
                                    getActivity().getBaseContext(),
                                    "Failed sharing blueprint",
                                    Toast.LENGTH_SHORT).show();
                        }
                        spinner.setVisibility(View.INVISIBLE);
                    }
                });
    }
}