package com.example.user.larper;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.user.larper.Model.Model;
import com.example.user.larper.Model.Profile;
import com.example.user.larper.Model.Skill;

import java.util.ArrayList;


public class ProfileFragment extends ListFragment {

    private OnProfileFragmentListener mListener;

    EditText nickName, age, race, scenarioClass, bio, hitPoints;
    Spinner genders;
    ArrayList<Skill> skills;
    SkillsAdapter lvAdapter;
    Profile acctProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("ProfileFragment", "OnCreateView");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ((Button)view.findViewById(R.id.profile_add_skill_btn)).setVisibility(View.INVISIBLE);

        // Get view objects
        nickName = ((EditText)view.findViewById(R.id.profile_nickname_et));
        age = ((EditText)view.findViewById(R.id.profile_age_et));
        genders = ((Spinner)view.findViewById(R.id.profile_gender_spinner));
        race = ((EditText)view.findViewById(R.id.profile_race_et));
        scenarioClass = ((EditText)view.findViewById(R.id.profile_class_et));
        bio = ((EditText)view.findViewById(R.id.profile_bio_et));
        hitPoints = ((EditText)view.findViewById(R.id.profile_hitpoints_et));

        if (Model.getInstance().getProfile() != null) {
            skills = Model.getInstance().getProfile().getSkills();
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.gender_spinner_layout, R.id.gender_spinner_item_tv,
                getResources().getStringArray(R.array.Genders));
        genders.setAdapter(spinnerAdapter);

        // Set account profile details
        acctProfile = Model.getInstance().getProfile();
        nickName.setText(acctProfile.getNickName());
        age.setText(acctProfile.getAge());
        genders.setSelection(spinnerAdapter.getPosition(acctProfile.getGender()));
        race.setText(acctProfile.getRace());
        scenarioClass.setText(acctProfile.getScenarioClass());
        bio.setText(acctProfile.getBiography());
        hitPoints.setText(acctProfile.getHitPoints());

        lvAdapter = new SkillsAdapter();
        setListAdapter(lvAdapter);

        nickName.setFocusable(false);
        nickName.setClickable(false);
        age.setFocusable(false);
        age.setClickable(false);
        genders.setFocusable(false);
        genders.setClickable(false);
        race.setFocusable(false);
        race.setClickable(false);
        scenarioClass.setFocusable(false);
        scenarioClass.setClickable(false);
        bio.setFocusable(false);
        bio.setClickable(false);
        hitPoints.setFocusable(false);
        hitPoints.setClickable(false);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProfileFragmentListener) {
            mListener = (OnProfileFragmentListener) context;
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

    public interface OnProfileFragmentListener {
        void profileDeleted();
    }

    private class SkillsAdapter extends BaseAdapter {

        class ViewHolder{
            CustomTextWatcher textWatch;
            protected EditText skillLevel;
            protected Button addBtn;
            protected Button subBtn;
        }

        @Override
        public int getCount() { return skills.size(); }

        @Override
        public Object getItem(int position) { return skills.get(position); }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            CustomTextWatcher textWatch;
            final Skill s = skills.get(position);

            // reuse views
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.profile_skill_row, null);
                ViewHolder vh = new ViewHolder();
                vh.skillLevel = ((EditText)convertView.findViewById(R.id.skill_level_et));
                vh.skillLevel.setText(skills.get(position).getLevel());
                vh.addBtn = (Button)convertView.findViewById(R.id.skill_add_btn);
                vh.subBtn = (Button)convertView.findViewById(R.id.skill_sub_btn);
                vh.textWatch = new CustomTextWatcher(vh.skillLevel, s, vh.addBtn, vh.subBtn);
                vh.skillLevel.addTextChangedListener(vh.textWatch);

                convertView.setTag(vh);
                vh.skillLevel.setTag(s);
                vh.addBtn.setTag(s);
                vh.subBtn.setTag(s);
            } else {
                ViewHolder vh = (ViewHolder)convertView.getTag();
                vh.skillLevel.setTag(s);
                vh.addBtn.setTag(s);
                vh.subBtn.setTag(s);
                vh.textWatch.s = s;
            }

            final ViewHolder vh = (ViewHolder)convertView.getTag();
            vh.skillLevel.setText(s.getLevel());

            vh.skillLevel.setFocusable(false);
            vh.skillLevel.setClickable(false);
            Button addSkillBtn = ((Button)convertView.findViewById(R.id.skill_add_btn));
            addSkillBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newVal = Integer.parseInt(skills.get(position).getLevel()) + 1;
                    if (newVal <= 100) {
                        skills.get(position).setLevel(Integer.toString(newVal));
                        vh.skillLevel.setText(Integer.toString(newVal));
                    }
                }
            });
            Button subSkillBtn = ((Button)convertView.findViewById(R.id.skill_sub_btn));
            subSkillBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newVal = Integer.parseInt(skills.get(position).getLevel()) - 1;
                    if (newVal >= 0) {
                        skills.get(position).setLevel(Integer.toString(newVal));
                        vh.skillLevel.setText(Integer.toString(newVal));
                    }
                }
            });

            Log.d("TAG", "Position is: " + position);
            ((EditText)convertView.findViewById(R.id.skill_name_et))
                    .setText(skills.get(position).getName());
            return convertView;
        }

        private class CustomTextWatcher implements TextWatcher {

            private EditText skillLevel;
            private Skill s;
            private Button addBtn;
            private Button subBtn;

            public CustomTextWatcher(EditText e, Skill s, Button add, Button sub) {
                this.skillLevel = e;
                this.s = s;
                this.addBtn = add;
                this.subBtn = sub;
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
                    if (this.skillLevel.getId() == R.id.skill_level_et) {
                        if (!skillLevel.getText().toString().equals("")) {
                            if (Integer.parseInt(skillLevel.getText().toString()) == 100) {
                                this.addBtn.setEnabled(false);
                            } else if (Integer.parseInt(skillLevel.getText().toString()) == 0) {
                                this.subBtn.setEnabled(false);
                            } else {
                                if (!this.addBtn.isEnabled()) {this.addBtn.setEnabled(true);}
                                if (!this.subBtn.isEnabled()) {this.subBtn.setEnabled(true);}
                            }
                        }
                    }
                }
            }
        }
    }
}
