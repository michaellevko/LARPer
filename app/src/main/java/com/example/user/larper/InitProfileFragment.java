package com.example.user.larper;

import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.user.larper.Model.Model;
import com.example.user.larper.Model.Profile;
import com.example.user.larper.Model.Skill;

import java.util.ArrayList;

public class InitProfileFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;
    private static final String ARG_PARAM1 = "acctName";
    private String acctName;
    ArrayList<Skill> skills = new ArrayList<>();
    SkillsAdapter lvAdapter;

    public InitProfileFragment() {
        // Required empty public constructor
    }

    public static InitProfileFragment newInstance(String acctName/*, String param2*/) {
        InitProfileFragment fragment = new InitProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, acctName);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.acctName = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_init_profile, container, false);

        // Display account username
        ((TextView)view.findViewById(R.id.hello_acct_tv)).setText(this.acctName);

        // Set gender spinner values
        Spinner spinner = ((Spinner)view.findViewById(R.id.profile_gender_spinner));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.gender_spinner_layout, R.id.gender_spinner_item_tv,
                        getResources().getStringArray(R.array.Genders));
        spinner.setAdapter(spinnerAdapter);

        // Set Skills listview
        lvAdapter = new SkillsAdapter();
        setListAdapter(lvAdapter);

        Button addSkillBtn = ((Button)view.findViewById(R.id.profile_add_skill_btn));
        addSkillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skills.add(new Skill());
                lvAdapter.notifyDataSetChanged();
            }
        });

        Button saveProfileBtn = ((Button)view.findViewById(R.id.init_profile_save_btn));
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

                Profile acctProfile = new Profile(nickName, age, gender, race,
                        scenarioClass, bio, hitpoints, skills);
                Model.getInstance().setProfile(acctProfile);
            }
        });

        return view;
    }

    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class SkillsAdapter extends BaseAdapter{

        @Override
        public int getCount() { return skills.size(); }

        @Override
        public Object getItem(int position) { return skills.get(position); }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // reuse views
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.profile_skill_row, null);
            }

            convertView.findViewById(R.id.skill_add_btn).setVisibility(View.INVISIBLE);
            convertView.findViewById(R.id.skill_level_et).setVisibility(View.INVISIBLE);
            Button delSkillBtn = ((Button)convertView.findViewById(R.id.skill_sub_btn));
            delSkillBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    skills.remove(position);
                    lvAdapter.notifyDataSetChanged();
                }
            });
            final EditText skillNameEt = ((EditText)convertView.findViewById(R.id.skill_name_et));
            skillNameEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean val = false;
                    if(event !=null){
                        if(actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_NEXT ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                            if (!event.isShiftPressed()) {
                                val = true;
                            }
                        }
                    }
                    else{
                        if(actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_NEXT ||
                                actionId == EditorInfo.IME_ACTION_DONE){
                            val = true;
                        }

                    }
                    if (val) {skills.get(position).setName(skillNameEt.getText().toString());}
                    return val;
                }
            });
            Log.d("TAG", "Position is: " + position);
            skillNameEt.setText(skills.get(position).getName());
            return convertView;
        }
    }
}
