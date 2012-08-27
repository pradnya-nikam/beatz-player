package com.beatzplayer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.beatzPlayer.R;

import java.io.File;

public class MyActivity extends Activity
{
    private BPMCalculator BPMCalculator;
    private final int REQUEST_CODE_PICK_DIR = 1;
    private Activity thisActivity;

    @Override
    public void onCreate(Bundle savedInstanceState){

        //read file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        BPMCalculator = new BPMCalculator(this);
        thisActivity = this;
        Button chooseFolder = (Button) findViewById(R.id.chooseFolder);

        chooseFolder.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                openFileExplorer();
            }
        });


    }

    private void openFileExplorer() {
        Intent fileExploreIntent = new Intent(com.fileexplorer.FileBrowserActivity.INTENT_ACTION_SELECT_DIR,
                null,
                thisActivity,
                com.fileexplorer.FileBrowserActivity.class
        );
        fileExploreIntent.putExtra(
                com.fileexplorer.FileBrowserActivity.startDirectoryParameter,
                Environment.getExternalStorageDirectory()
        );

        thisActivity.startActivityForResult(fileExploreIntent, REQUEST_CODE_PICK_DIR);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_PICK_DIR) {
            if(resultCode == this.RESULT_OK) {
                String songsDirectory = data.getStringExtra(com.fileexplorer.FileBrowserActivity.returnDirectoryParameter);
                Toast.makeText(this,"Received DIRECTORY path from file browser:\n"+songsDirectory,Toast.LENGTH_LONG).show();
                executeBPMCalculator(songsDirectory);
            } else {
                Toast.makeText(this,"Received NO result from file browser",Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void executeBPMCalculator(String songsDirectory) {
        File songsDir = new File(songsDirectory);
        File[] songs = songsDir.listFiles(new Mp3FileFilter());
//        Toast.makeText(thisActivity,"BPM calculation started!",Toast.LENGTH_LONG).show();
        TextView infoBox= (TextView) findViewById(R.id.info);
        infoBox.setText("FOLDER: "+ songsDirectory + " ; SONGS: " + songs.length);
//        Debug.startMethodTracing("tracesAfterAddingAsync");
        BPMCalculator.setSongsFolder(songsDirectory + "/");
        BPMCalculator.execute(songs);
    }
}
