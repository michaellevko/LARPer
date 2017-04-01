package com.example.user.larper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by event on 01/04/2017.
 */

public final class FirebaseManager {
    static FirebaseStorage storage;
    static String projectUrl = "gs://firebase-larper.appspot.com";

    private FirebaseManager()
    {
        this.storage = FirebaseStorage.getInstance();
    }

    public static boolean UploadFile(String name)
    {
        return true;
    }

    public static boolean DownloadFile(InputStream istream)
    {
        return true;
    }

    public static boolean DownloadFile(File file)
    {
        boolean result;
        StorageReference storageRef = storage.getReferenceFromUrl(projectUrl).child("Cover.jpg");
        FileDownloadTask f = storageRef.getFile(file);
        //f.getResult()
        try {
            storageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.println(exception.toString());
                }
            });
        } catch (IOException e ) {}

        File file = null;
        try {
            file = File.createTempFile("test2", "txt");
        } catch( IOException e ) {

        }
        UploadTask uploadTask = storageReference.putFile(Uri.fromFile(file));
        return true;
    }
}
