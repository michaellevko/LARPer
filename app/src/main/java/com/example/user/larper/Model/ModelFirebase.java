package com.example.user.larper.Model;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.util.UUID;

/**
 * Created by event on 01/04/2017.
 */

final public class ModelFirebase {
    private ModelFirebase(){
    }

    public interface FirebaseListener{
        void complete(boolean result);
    }

    public interface FirebaseLoadListener{
        void complete(File file);
    }

    public static void saveFile(File file, final FirebaseListener listener, StaticProfile profile){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(profile.getNickname());

        UploadTask uploadTask = storageRef.putFile(Uri.fromFile(file));
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.complete(false);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                listener.complete(true);
            }
        });
    }

    public static void loadFileByOwner(String owner, final FirebaseLoadListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference pathReference = storageRef.child(owner);

        final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                   + "/" + owner + ".jpeg");

        pathReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                listener.complete(file);
                pathReference.delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // Handle any errors
                listener.complete(null);
            }
        });
    }
}
