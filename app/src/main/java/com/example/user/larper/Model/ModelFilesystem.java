package com.example.user.larper.Model;


import java.io.File;

/**
 * Created by event on 04/04/2017.
 */

public class ModelFilesystem {

    // file selection event handling
    public interface SaveFileListener {
        void complete(boolean result);
    }
    public ModelFilesystem setSaveListener(ModelFilesystem.SaveFileListener saveListener) {
        this.saveListener = saveListener;
        return this;
    }
    private ModelFilesystem.SaveFileListener saveListener;


}
