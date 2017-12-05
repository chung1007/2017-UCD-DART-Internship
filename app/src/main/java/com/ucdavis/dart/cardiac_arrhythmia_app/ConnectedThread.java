package com.ucdavis.dart.cardiac_arrhythmia_app;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.androidplot.xy.SimpleXYSeries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sam on 7/8/17.
 */
public class ConnectedThread extends Thread{
    public BluetoothSocket mmSocket;
    public InputStream mmInStream;
    public OutputStream mmOutStream;
    public Activity activity;
    private Boolean notPaused = true;

    public ConnectedThread(BluetoothSocket socket, Activity context) {
        this.activity = context;
        try {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }catch (NullPointerException NPE){

        }
        graphTouchListener();

    }

    public void run() {
        initializeConnection();
        byte[] buffer = new byte[2]; // buffer store for the stream
        int bytes;
        String s = "";
         // bytes returned from read()
        while (mmSocket!=null) {
            try {
                if(notPaused) {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    s = s + readMessage;
                    if(readMessage.contains("/")){
                        final String num = s.replace(" ", "").replace("/", "");
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drawMergedPlot(Integer.parseInt(num));
                            }
                        });
                        s = "";
                    }
                }
            } catch (IOException e) {
                Log.e("ERROR ", "reading from btInputStream");
                break;
            }
        }
    }



    public void initializeConnection() {
        try {
            PrintWriter out;
            out = new PrintWriter(mmOutStream, true);
            out.println("*");
            out.flush();
        }catch (NullPointerException NPE) {

        }
    }
    private void graphTouchListener(){
        MainActivity.ecgHistoryPlot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(notPaused) {
                    notPaused = false;
                }else{
                    notPaused = true;
                }
                return true;
            }
        });
    }
    private static void drawMergedPlot(int EcgRaLl) {
        Number[] seriesRNumbers = { EcgRaLl};
        MainActivity.ecgLevelsSeries.setModel(Arrays.asList(seriesRNumbers),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);

        if (MainActivity.ecgRaLlHistory.size() > Constants.domain - 1) {
            MainActivity.ecgRaLlHistory.removeFirst();
        }
        MainActivity.ecgRaLlHistory.addLast(EcgRaLl);

        MainActivity.ecgRaLlHistorySeries.setModel(MainActivity.ecgRaLlHistory,
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
        MainActivity.ecgHistoryPlot.redraw();
    }

}


