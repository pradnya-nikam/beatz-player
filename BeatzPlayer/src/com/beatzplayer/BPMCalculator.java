package com.beatzplayer;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.beatzPlayer.R;
import com.bpmprocessor.BPMProcessor;
import com.bpmprocessor.EnergyOutputAudioDevice;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BPMCalculator extends AsyncTask<File,String, Map>{
    private Activity mainActivity;
    private final LinearLayout mainLayout;

    private String songsFolder;
    private Map<String,Integer> bpmMap;

    private static long startTime ;

    public BPMCalculator(Activity mainActivity) {
        this.mainActivity = mainActivity;
        bpmMap = new HashMap<String, Integer>();
        mainLayout = (LinearLayout) mainActivity.findViewById(R.id.mainLayout);
    }

    @Override
    protected void onPreExecute() {
        startTime = System.currentTimeMillis();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Map bpm) {
        super.onPostExecute(bpm);
        long timeRequired = (System.currentTimeMillis() - startTime) / 1000;
        showTextInTextView(mainLayout,"total time required : " + timeRequired);
    }

    @Override
    protected void onProgressUpdate(String... args) {
        super.onProgressUpdate(args);
        showTextInTextView(mainLayout,"bpm for " + args[0] + " : " + args[1]);
    }

    private void showTextInTextView(LinearLayout linearLayout, String text) {
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(mainActivity);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        linearLayout.addView(textView);
    }

    @Override
    protected Map doInBackground(File... songs) {
        for (int count = 0; count < songs.length; count++){
            String songName = songs[count].getAbsolutePath().split(songsFolder)[1].replaceAll("/","");
            int bpm = calculateBpm(songs[count]);
            publishProgress(songName, Integer.toString(bpm));
            bpmMap.put(songName, bpm);
        }
        return bpmMap;
    }

    private int calculateBpm(File song) {
        BPMProcessor processor = new BPMProcessor();
        processor.setSampleSize(1024);
        EnergyOutputAudioDevice output = new EnergyOutputAudioDevice(processor);
        output.setAverageLength(1024);
        AdvancedPlayer player;


        FileInputStream fileInputStream = null;
        BufferedInputStream inputStream = null;
        try {
            fileInputStream = new FileInputStream(song);
            inputStream = new BufferedInputStream(fileInputStream, 7 * 1024);
//                  player = new Player(inputStream, output);
//                  player.play();
            player = new AdvancedPlayer(inputStream, output);
            player.play(1000,2000);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }

        int bpm = processor.getBPM();

        try {
            if(fileInputStream!=null)
                fileInputStream.close();

            if (inputStream != null)
                inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bpm;

    }

    public void setSongsFolder(String songsFolder) {
        this.songsFolder = songsFolder;
    }
}