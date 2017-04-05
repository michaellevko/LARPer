package com.example.user.larper;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.user.larper.Model.Model;
import com.example.user.larper.Model.ModelSqlite;
import com.example.user.larper.Model.Profile;
import com.example.user.larper.Model.Skill;

import java.util.ArrayList;

/**
 * Created by User on 4/5/2017.
 */

public class NewLoreFragment extends ListFragment {

    private OnNewLoreFragmentListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_new_lore, container, false);

        // Set gender spinner values
        Spinner spinner = ((Spinner)view.findViewById(R.id.profile_gender_spinner));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.gender_spinner_layout, R.id.gender_spinner_item_tv,
                getResources().getStringArray(R.array.Genders));
        spinner.setAdapter(spinnerAdapter);

        // Set Skills listview
        final SkillsAdapter lvAdapter = new SkillsAdapter(getActivity(),
                R.layout.profile_skill_row, R.id.skill_name_et, new ArrayList<Skill>());
        setListAdapter(lvAdapter);

        Button addSkillBtn = ((Button)view.findViewById(R.id.profile_add_skill_btn));
        addSkillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //skills.add(new Skill());
                //lvAdapter.notifyDataSetChanged();
                lvAdapter.add(new Skill());
            }
        });

        Button saveProfileBtn = ((Button)view.findViewById(R.id.lore_new_save_btn));
        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = ((EditText)view.findViewById(R.id.profile_nickname_et))
                        .getText().toString();
                String age = ((EditText)view.findViewById(R.id.profile_age_et))
                        .getText().toString();
                String gender = ((Spinner)view.findViewById(R.id.profile_gender_spinner))
                        .getSelectedItem().toString();
                String race = ((EditText)view.findViewById(R.id.profile_race_et))
                        .getText().toString();
                String scenarioClass = ((EditText)view.findViewById(R.id.profile_class_et))
                        .getText().toString();
                String bio = ((EditText)view.findViewById(R.id.profile_bio_et))
                        .getText().toString();
                String hitpoints = ((EditText)view.findViewById(R.id.profile_hitpoints_et))
                        .getText().toString();

                Profile profile = new Profile(nickName, age, gender, race,
                        scenarioClass, bio, hitpoints, lvAdapter.getItems());
                Model.getInstance().addProfileToLore(profile);
                ModelSqlite sql = new ModelSqlite(getContext());
                sql.createProfile(profile);


                mListener.gotoLoreInterface();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewLoreFragmentListener) {
            mListener = (OnNewLoreFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewLoreFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnNewLoreFragmentListener {
        void gotoLoreInterface();
    }
}
