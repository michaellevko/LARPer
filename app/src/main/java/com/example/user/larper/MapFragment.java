package com.example.user.larper;

import org.apache.commons.io.FilenameUtils;
import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.larper.Model.ModelFilesystem;
import com.example.user.larper.Model.ModelFirebase;
import com.example.user.larper.Model.ModelSqlite;
import com.example.user.larper.Model.StaticProfile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class MapFragment extends Fragment {

    private Activity activity;
    private ImageView image;
    private DrawingView mDrawingView;
    private boolean isLoaded;
    private Bitmap currBitmap;
    private File currBitmapFile;
    final static String SUFFIX = "_objective.jpeg";
    private ArrayList<StaticProfile> contacts;
    Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // init
        final Activity factivity = this.getActivity();
        activity = factivity;
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        image = (ImageView) view.findViewById(R.id.imageView);
        isLoaded = false;

        // init canvas for drawing
        mDrawingView=new DrawingView(factivity);
        LinearLayout mDrawingPad=(LinearLayout)view.findViewById(R.id.draw_layout);
        mDrawingPad.addView(mDrawingView);

        // init contact spinner adapter
        contacts = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter(
                activity.getBaseContext(),
                android.R.layout.simple_spinner_item,
                contacts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // define spinner
        spinner = ((Spinner) view.findViewById(R.id.spinner));
        spinner.setVisibility(View.INVISIBLE);
        spinner.setAdapter(adapter);

        // handle spinner contact selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                spinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                shareImage();
            }
        });

        // set listener for load button
        final Button button = (Button) view.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(factivity,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permissionCheck != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(factivity,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            FileChooser.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                }
                else {
                    loadImage();
                }
            }
        });

        // set listener for undo button
        final Button button_undo = (Button) view.findViewById(R.id.button3);
        button_undo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDrawingView.onClickUndo();
            }
        });

        // set listener for redo button
        final Button button_redo = (Button) view.findViewById(R.id.button4);
        button_redo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDrawingView.onClickRedo();
            }
        });

        // set listener for save button
        final Button button_save = (Button) view.findViewById(R.id.button5);
        button_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(saveImage())
                {
                    Toast.makeText(
                            activity.getApplicationContext(),
                            "Successfully saved objective",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(
                            activity.getApplicationContext(),
                            "Failed saving objective",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // set listener for share button
        final Button button_share = (Button) view.findViewById(R.id.button8);
        button_share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // check if there is an image to shares
                if(isLoaded)
                {
                    // refresh contact list
                    contacts.clear();
                    contacts.addAll(getOwnerContacts());
                    adapter.notifyDataSetChanged();

                    // make contact spinner visible
                    spinner.setVisibility(View.VISIBLE);
                }
            }
        });


        return view;
    }

    public void shareImage()
    {
        File save_file = new File(
                currBitmapFile.getParent()
                        + "/" + FilenameUtils.removeExtension(
                        currBitmapFile.getName())
                        + SUFFIX);
        ModelFirebase.saveFile(save_file,
                new ModelFirebase.FirebaseListener() {
                    @Override
                    public void complete(boolean result) {
                        if (result) {
                            Toast.makeText(
                                    activity.getApplicationContext(),
                                    "Successfully shared objective",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(
                                    activity.getApplicationContext(),
                                    "Failed sharing objective",
                                    Toast.LENGTH_SHORT).show();
                        }
                        spinner.setVisibility(View.INVISIBLE);
                    }

                }, new StaticProfile(
                        spinner.getSelectedItem().toString(),
                        "false"));
    }

    public void loadImage(){
        new FileChooser(activity).setFileListener(new FileChooser.FileSelectedListener() {
            @Override
            public void fileSelected(final File file) {
                // do something with the file
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                bitmap = Bitmap.createScaledBitmap(
                        bitmap,
                        mDrawingView.getWidth(),
                        mDrawingView.getHeight(),
                        false);
                image.setImageBitmap(bitmap);
                image.setScaleType(ImageView.ScaleType.FIT_START);
                //BitmapDrawable bitmap_scaled = (BitmapDrawable)image.getDrawable();
                // Rect bounds_scaled = bitmap_scaled.getBounds();
                // make mutable
                bitmap=bitmap.copy(Bitmap.Config.ARGB_8888, true);
                mDrawingView.setBitmap(bitmap);
                assignBitmap(bitmap, file);
            }
        }).showDialog();
    }

    public void assignBitmap(Bitmap bitmap, File file)
    {
        this.isLoaded = true;
        this.currBitmap = bitmap;
        this.currBitmapFile = file;
    }

    public ArrayList<StaticProfile> getOwnerContacts()
    {
        ModelSqlite sql = new ModelSqlite(this.getContext());
        return sql.getOwnerContacts();
    }

    public boolean saveImage(){
        if (this.isLoaded)
        {
            String save_path = this.currBitmapFile.getParent()
                    + "/" + FilenameUtils.removeExtension(this.currBitmapFile.getName())
                        + SUFFIX;

            File save_file = new File(save_path);

            try
            {
                // scale bitmap to device
                FileOutputStream fOut = new FileOutputStream(save_file);
                Bitmap bitmap_to_save = Bitmap.createScaledBitmap(
                        BitmapFactory.decodeFile(this.currBitmapFile.getAbsolutePath()),
                        mDrawingView.getWidth(),
                        mDrawingView.getHeight(),
                        false);

                // prepare canvas for saving
                this.mDrawingView.beforeSave(bitmap_to_save);

                // save canvas
                this.mDrawingView.getBitmap().compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush();
                fOut.close();
            }
            catch (java.io.IOException e)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case FileChooser.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImage();
                }
            }
        }
    }
}

