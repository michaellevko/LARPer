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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.larper.Model.Blueprint;
import com.example.user.larper.Model.Model;
import com.example.user.larper.Model.ModelSqlite;

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

        // Set list adapter
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.blueprint_row_details, null);
                ImageButton bpShare = (ImageButton)convertView.findViewById(R.id.blueprint_share_btn);
                Drawable shareImg = getResources().getDrawable(R.drawable.share_icon);
                shareImg.setBounds(0, 0, 40, 40);
                bpShare.setImageDrawable(shareImg);
                bpShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*TODO: Implement share to firebase*/
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