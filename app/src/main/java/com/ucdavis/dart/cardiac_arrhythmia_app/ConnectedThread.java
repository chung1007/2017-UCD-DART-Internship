package com.ucdavis.dart.cardiac_arrhythmia_app;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sam on 7/8/17.
 */
public class ConnectedThread extends Thread {
    public BluetoothSocket mmSocket;
    public InputStream mmInStream;
    public OutputStream mmOutStream;
    public Activity activity;
    private Boolean notPaused = true;

    public ConnectedThread(BluetoothSocket socket, Activity context, BluetoothDevice device) {
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
        while (mmSocket != null) {
            if (notPaused){
                final BufferedReader reader = new BufferedReader(new InputStreamReader(mmInStream));
                try {
                    final int dataPoint = Integer.parseInt(reader.readLine());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            drawMergedPlot(dataPoint);
                        }
                    });

                } catch (IOException IO) {
                    break;
                }
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


