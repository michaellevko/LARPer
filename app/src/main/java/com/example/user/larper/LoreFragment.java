package com.example.user.larper;

import android.app.ListFragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.larper.Model.Model;
import com.example.user.larper.Model.Profile;

import java.util.ArrayList;


public class LoreFragment extends ListFragment {

    private OnLoreFragmentListener mListener;
    LoreAdapter adapter = new LoreAdapter();
    ArrayList<Profile> loreList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lore, container, false);

        // Set list adapter
        loreList = Model.getInstance().getLore();
        setListAdapter(adapter);

        ((Button)view.findViewById(R.id.lore_new_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoNewLoreInterface();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoreFragmentListener) {
            mListener = (OnLoreFragmentListener) context;
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

    public interface OnLoreFragmentListener {
        void gotoNewLoreInterface();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public class LoreAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return loreList.size();
        }

        @Override
        public Object getItem(int position) {
            return loreList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.lore_row_details, null);
                ImageButton loreShare = (ImageButton)convertView.findViewById(R.id.lore_share_btn);
                Drawable shareImg = getResources().getDrawable(R.drawable.share_icon);
                shareImg.setBounds(0, 0, 40, 40);
                loreShare.setImageDrawable(shareImg);
                loreShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*TODO: Implement share to firebase*/
                    }
                });
            }
            Profile profile = loreList.get(position);
            ((TextView)convertView.findViewById(R.id.lore_name_tv)).setText(profile.getNickName());
            ((TextView)convertView.findViewById(R.id.lore_race_tv)).setText(profile.getRace());
            ((TextView)convertView.findViewById(R.id.lore_hitpoints_tv)).setText(profile.getHitPoints());

            return convertView;
        }
    }


}
