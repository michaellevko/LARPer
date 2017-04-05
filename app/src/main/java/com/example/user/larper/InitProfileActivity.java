package com.example.user.larper;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.user.larper.Model.Model;
import com.example.user.larper.Model.Profile;
import com.example.user.larper.Model.Skill;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InitProfileActivity extends ListActivity {

    private String acctDisplayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_profile);

        acctDisplayName = this.getIntent().getExtras()
                .getString(getResources().getString(R.string.account_display_name));
        ((TextView)findViewById(R.id.hello_acct_tv)).setText(acctDisplayName);

        // Set gender spinner values
        Spinner spinner = ((Spinner)findViewById(R.id.profile_gender_spinner));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.gender_spinner_layout, R.id.gender_spinner_item_tv,
                getResources().getStringArray(R.array.Genders));
        spinner.setAdapter(spinnerAdapter);

        // Set Skills listview
        final SkillsAdapter lvAdapter = new SkillsAdapter(this,
                R.layout.profile_skill_row, R.id.skill_name_et, new ArrayList<Skill>());
        setListAdapter(lvAdapter);

        Button addSkillBtn = ((Button)findViewById(R.id.profile_add_skill_btn));
        addSkillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //skills.add(new Skill());
                //lvAdapter.notifyDataSetChanged();
                Skill s = new Skill();
                lvAdapter.add(s);
            }
        });

        Button saveProfileBtn = ((Button)findViewById(R.id.init_profile_save_btn));
        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = ((EditText)findViewById(R.id.profile_nickname_et))
                        .getText().toString();
                String age = ((EditText)findViewById(R.id.profile_age_et))
                        .getText().toString();
                String gender = ((Spinner)findViewById(R.id.profile_gender_spinner))
                        .getSelectedItem().toString();
                String race = ((EditText)findViewById(R.id.profile_race_et))
                        .getText().toString();
                String scenarioClass = ((EditText)findViewById(R.id.profile_class_et))
                        .getText().toString();
                String bio = ((EditText)findViewById(R.id.profile_bio_et))
                        .getText().toString();
                String hitpoints = ((EditText)findViewById(R.id.profile_hitpoints_et))
                        .getText().toString();

                Profile acctProfile = new Profile(nickName, age, gender, race,
                        scenarioClass, bio, hitpoints, lvAdapter.getItems());
                Model.getInstance().setProfile(acctProfile);

                launchMainActivity();
            }
        });
    }

    private void launchMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getResources().getString(R.string.account_display_name), acctDisplayName);
        startActivity(intent);
        this.finish();
    }
}
