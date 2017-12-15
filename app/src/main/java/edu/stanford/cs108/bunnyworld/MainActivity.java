package edu.stanford.cs108.bunnyworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.R.attr.version;


public class MainActivity extends AppCompatActivity {
    private SingletonGameVector singletonGameVector;
    public static final String EDIT_PREFIX = "edit_";
    public static final String PLAY_PREFIX = "play_";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // remember to load games that are already created from a file!!!
        singletonGameVector = SingletonGameVector.getInstance();
        singletonGameVector.setGameArray(new ArrayList<Game>());
//
//        File file = new File("/data/data/edu.stanford.cs108.bunnyworld/files/" +  "play_play_game");
//        file.delete(); // remove play_file

    }

    protected void onResume() {
        super.onResume();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void onPlayBtn (View view) {
        Intent intent = new Intent(this, ChooseGameToPlay.class);
        startActivity(intent);

        //overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    public void onEditBtn (View view) {
        Intent intent  = new Intent(this, ChooseGameToEdit.class);
        startActivity(intent);
    }

    /** method to copy a file
     *  copy from http://stackoverflow.com/questions/16433915/how-to-copy-file-from-one-location-to-another-location
     */
    private void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

}
