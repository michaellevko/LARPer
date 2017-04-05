package com.example.user.larper;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.user.larper.Model.Model;
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
                    + " must implement OnInitProfileFragmentListener");
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

    private class SkillsAdapter extends ArrayAdapter<Skill>{

        private ArrayList<Skill> skills;
        private Context context;

        public SkillsAdapter(@NonNull Context context, @LayoutRes int resource,
                             @IdRes int textViewResourceId, @NonNull ArrayList<Skill> objects) {
            super(context, resource, textViewResourceId, objects);
            this.skills = objects;
            this.context = context;
        }

        class ViewHolder{
            protected EditText skillName;
            protected EditText skillLevel;
        }

        public ArrayList<Skill> getItems(){
            return this.skills;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            final Skill s = this.skills.get(position);
            final ArrayAdapter arrAdapter = this;

            // reuse views
            if (v == null) {
                v = getActivity().getLayoutInflater().inflate(R.layout.profile_skill_row, null);
                final ViewHolder vh = new ViewHolder();
                vh.skillName = (EditText)v.findViewById(R.id.skill_name_et);
                vh.skillLevel = (EditText)v.findViewById(R.id.skill_level_et);

                vh.skillName.addTextChangedListener(new CustomTextWatcher(vh.skillName, s));

                v.setTag(vh);
                vh.skillName.setTag(s);
                vh.skillLevel.setTag(s);
            } else {
                ViewHolder vh = (ViewHolder)v.getTag();
                vh.skillName.setTag(s);
                vh.skillLevel.setTag(s);
            }

            ViewHolder vh = (ViewHolder)v.getTag();
            vh.skillName.setText(s.getName());
            vh.skillLevel.setText(s.getLevel());

            v.findViewById(R.id.skill_add_btn).setVisibility(View.GONE);
            v.findViewById(R.id.skill_level_et).setVisibility(View.GONE);
            Button delSkillBtn = ((Button)v.findViewById(R.id.skill_sub_btn));
            delSkillBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    skills.remove(s);
                    arrAdapter.notifyDataSetChanged();
                    //arrAdapter.remove(s);

                }
            });

            Log.d("TAG", "Position is: " + position);
            return v;
        }
    }

    private class CustomTextWatcher implements TextWatcher {

        private EditText skillName;
        private Skill s;

        public CustomTextWatcher(EditText e, Skill s) {
            this.skillName = e;
            this.s = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable arg0) {

            String text = arg0.toString();

            if (text != null && text.length() > 0) {
                if (this.skillName.getId() == R.id.skill_name_et) {
                    this.s.setName(text);
                }
            }
        }
    }
}