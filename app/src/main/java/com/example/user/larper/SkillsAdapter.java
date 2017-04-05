package com.example.user.larper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.larper.Model.Skill;

import java.util.ArrayList;

/**
 * Created by User on 4/5/2017.
 */

public class SkillsAdapter extends ArrayAdapter<Skill> {

    private ArrayList<Skill> skills;
    private Context context;

    public SkillsAdapter(@NonNull Context context, @LayoutRes int resource,
                         @IdRes int textViewResourceId, @NonNull ArrayList<Skill> objects) {
        super(context, resource, textViewResourceId, objects);
        this.skills = objects;
        this.context = context;
    }

    class ViewHolder{
        CustomTextWatcher textWatch;
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
            v = ((Activity)context).getLayoutInflater().inflate(R.layout.profile_skill_row, null);
            final ViewHolder vh = new ViewHolder();
            vh.skillName = (EditText)v.findViewById(R.id.skill_name_et);
            vh.skillLevel = (EditText)v.findViewById(R.id.skill_level_et);
            vh.textWatch = new CustomTextWatcher(vh.skillName, s);
            vh.skillName.addTextChangedListener(vh.textWatch);

            v.setTag(vh);
            vh.skillName.setTag(s);
            vh.skillLevel.setTag(s);
        } else {
            ViewHolder vh = (ViewHolder)v.getTag();
            vh.skillName.setTag(s);
            vh.skillLevel.setTag(s);
            vh.textWatch.s = s;
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


