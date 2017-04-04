package com.example.user.larper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.larper.Model.Blueprint;
import com.example.user.larper.Model.Ingredient;
import com.example.user.larper.Model.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class NewBlueprintFragment extends Fragment {

    private OnNewBlueprintFragmentListener mListener;
    IngredientAdapter adapter = new IngredientAdapter();
    ArrayList<Ingredient> ingList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_blueprint, container, false);

        // Set list adapter for ingredients listview
        ListView lv = (ListView)view.findViewById(R.id.blueprint_ingredients_lv);
        lv.setAdapter(adapter);

        final EditText bpName = (EditText)view.findViewById(R.id.blueprint_name_et);
        final EditText bpCraftDurH = (EditText)view.findViewById(R.id.blueprint_crafting_time_hours_et);
        final EditText bpCraftDurM = (EditText)view.findViewById(R.id.blueprint_crafting_time_minutes_et);

        Button bpSave = (Button)view.findViewById(R.id.blueprint_new_save_btn);
        bpSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingList.size() > 0) {
                    String craftTime = bpCraftDurH.getText().toString() +
                            "h:" + bpCraftDurM.getText().toString() + "m";
                    Model.getInstance().addBlueprint(new Blueprint(bpName.getText().toString(),
                            ingList, craftTime));

                    Toast.makeText(getActivity(), "Blueprint Saved.", Toast.LENGTH_SHORT);

                    mListener.gotoBlueprintInterface();
                } else {
                    Toast.makeText(getActivity(), "Blueprint must contain at least one ingredient.",
                            Toast.LENGTH_SHORT);
                }
            }
        });

        Button bpCancel = (Button)view.findViewById(R.id.blueprint_new_cancel_btn);
        bpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Canceled.", Toast.LENGTH_SHORT);
                mListener.gotoBlueprintInterface();
            }
        });

        Button ingAdd = (Button)view.findViewById(R.id.blueprint_new_ingredient_add_btn);
        ingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingList.add(new Ingredient());
                adapter.notifyDataSetChanged();
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewBlueprintFragmentListener) {
            mListener = (OnNewBlueprintFragmentListener) context;
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

    public interface OnNewBlueprintFragmentListener {
        void gotoBlueprintInterface();
    }

    public class IngredientAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return ingList.size();
        }

        @Override
        public Object getItem(int position) {
            return ingList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Ingredient ing = ingList.get(position);

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.ingredient_row_details, null);

                EditText totalCost =(EditText)convertView.findViewById(R.id.ingredient_price_sum_et);
                totalCost.setFocusable(false);
                totalCost.setClickable(false);
            }

            final EditText price = (EditText)convertView.findViewById(R.id.ingredient_price_et);
            price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        ing.setPrice(price.getText().toString());
                        price.setTag(ing);
                    }
                }
            });
            final EditText quantity = (EditText)convertView.findViewById(R.id.ingredient_quantity_et);
            price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        ing.setPrice(quantity.getText().toString());
                        quantity.setTag(ing);
                    }
                }
            });

            final EditText name = (EditText)convertView.findViewById(R.id.ingredient_name_et);
            //final EditText price = (EditText)convertView.findViewById(R.id.ingredient_price_et);
            //final EditText quantity = (EditText)convertView.findViewById(R.id.ingredient_quantity_et);
            final EditText totalCost = (EditText)convertView.findViewById(R.id.ingredient_price_sum_et);

            name.setText(ing.getName());
            price.setText(ing.getPrice());
            quantity.setText(ing.getQuantity());
            if ((price.getTag() != null) && (quantity.getTag() != null)) {
                totalCost.setText(ing.getPriceSum());
            }

            return convertView;
        }
    }
}
