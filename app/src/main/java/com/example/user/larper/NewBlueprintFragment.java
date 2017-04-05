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
import com.example.user.larper.Model.ModelSqlite;
import com.example.user.larper.Model.StaticProfilesSql;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class NewBlueprintFragment extends Fragment {

    private OnNewBlueprintFragmentListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_blueprint, container, false);

        // Set list adapter for ingredients listview
        ListView lv = (ListView)view.findViewById(R.id.blueprint_ingredients_lv);
        final ArrayList<Ingredient> ingList = new ArrayList<>();
        final IngredientAdapter adapter = new IngredientAdapter(getActivity(),
                R.layout.ingredient_row_details, R.id.ingredient_name_et, ingList);
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
                    Blueprint blueprintToSave = new Blueprint(bpName.getText().toString(),
                            ingList, craftTime);

                    // save in memory
                    Model.getInstance().addBlueprint(blueprintToSave);

                    // save in sql
                    ModelSqlite sql = new ModelSqlite(getContext());
                    sql.saveBlueprint(blueprintToSave, StaticProfilesSql.curr_owner.toString());

                    Toast.makeText(getActivity(), "Blueprint Saved.", Toast.LENGTH_SHORT).show();

                    mListener.gotoBlueprintInterface();
                } else {
                    Toast.makeText(getActivity(), "Blueprint must contain at least one ingredient.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button bpCancel = (Button)view.findViewById(R.id.blueprint_new_cancel_btn);
        bpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Canceled.", Toast.LENGTH_SHORT).show();
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

    public class IngredientAdapter extends ArrayAdapter {

        private ArrayList<Ingredient> ingList;
        private Context context;

        public IngredientAdapter(@NonNull Context context, @LayoutRes int resource,
                                 @IdRes int textViewResourceId, @NonNull ArrayList<Ingredient> objects) {
            super(context, resource, textViewResourceId, objects);
            this.ingList = objects;
            this.context = context;
        }

        private class ViewHolder{
            CustomTextWatcher nameTextWatch;
            CustomTextWatcher priceTextWatch;
            CustomTextWatcher quantityTextWatch;
            EditText ingName;
            EditText price;
            EditText quantity;
            EditText totalCost;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Ingredient ing = ingList.get(position);

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.ingredient_row_details, null);

                ViewHolder vh = new ViewHolder();
                vh.ingName = (EditText)convertView.findViewById(R.id.ingredient_name_et);
                vh.price = (EditText)convertView.findViewById(R.id.ingredient_price_et);
                vh.quantity = (EditText)convertView.findViewById(R.id.ingredient_quantity_et);
                vh.totalCost = (EditText)convertView.findViewById(R.id.ingredient_price_sum_et);
                vh.nameTextWatch = new CustomTextWatcher(vh.ingName, vh.totalCost, ing);
                vh.ingName.addTextChangedListener(vh.nameTextWatch);
                vh.priceTextWatch = new CustomTextWatcher(vh.price, vh.totalCost, ing);
                vh.price.addTextChangedListener(vh.priceTextWatch);
                vh.quantityTextWatch = new CustomTextWatcher(vh.quantity, vh.totalCost, ing);
                vh.quantity.addTextChangedListener(vh.quantityTextWatch);
                vh.totalCost.setFocusable(false);
                vh.totalCost.setClickable(false);

                convertView.setTag(vh);
                vh.ingName.setTag(ing);
                vh.price.setTag(ing);
                vh.quantity.setTag(ing);
                vh.totalCost.setTag(ing);
            } else {
                ViewHolder vh = (ViewHolder)convertView.getTag();
                vh.ingName.setTag(ing);
                vh.price.setTag(ing);
                vh.quantity.setTag(ing);
                vh.totalCost.setTag(ing);
                vh.nameTextWatch.ing = ing;
                vh.priceTextWatch.ing = ing;
                vh.quantityTextWatch.ing = ing;
            }

            ViewHolder vh = (ViewHolder)convertView.getTag();
            vh.ingName.setText(ing.getName());
            vh.price.setText(ing.getPrice());
            vh.quantity.setText(ing.getQuantity());
            vh.totalCost.setText(ing.getPriceSum());

            return convertView;
        }

        private class CustomTextWatcher implements TextWatcher {

            private EditText et;
            private EditText totalCost;
            private Ingredient ing;

            public CustomTextWatcher(EditText e, EditText t, Ingredient ing) {
                this.et = e;
                this.totalCost = t;
                this.ing = ing;
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
                    switch (this.et.getId()) {
                        case R.id.ingredient_name_et: {
                            ing.setName(text);
                            break;
                        }
                        case R.id.ingredient_price_et: {
                            ing.setPrice(text);
                            this.totalCost.setText(ing.getPriceSum());
                            break;
                        }
                        case R.id.ingredient_quantity_et: {
                            ing.setQuantity(text);
                            this.totalCost.setText(ing.getPriceSum());
                            break;
                        }
                    }
                }
            }
        }
    }
}
