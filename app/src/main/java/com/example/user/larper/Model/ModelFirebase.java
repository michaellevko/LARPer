package com.example.user.larper.Model;
import android.net.Uri;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;

/**
 * Created by event on 01/04/2017.
 */

final public class ModelFirebase {
    private ModelFirebase(){
    }

    public interface FirebaseListener{
        void complete(boolean result);
    }

    public static void saveFile(File file, final FirebaseListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference().child(file.getName());

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

    public static void loadFile(File file, final FirebaseListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference().child(file.getName());

        storageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                listener.complete(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // Handle any errors
                listener.complete(false);
            }
        });
    }
}
