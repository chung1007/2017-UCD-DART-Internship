package com.ucdavis.dart.cardiac_arrhythmia_app;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
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
    public List<Float> bufferData;

    public ConnectedThread(BluetoothSocket socket, Activity context) {
        this.activity = context;
        bufferData = new ArrayList<>();
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
    }
    public void run() {
        initializeConnection();
        while (mmSocket != null) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(mmInStream));
            try{
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

    public void initializeConnection() {
        try {
            PrintWriter out;
            out = new PrintWriter(mmOutStream, true);
            out.println("*");
            out.flush();
        }catch (NullPointerException NPE) {

        }
    }
    private static void drawMergedPlot(int EcgRaLl) {
        Number[] seriesRNumbers = { EcgRaLl};
        MainActivity.ecgLevelsSeries.setModel(Arrays.asList(seriesRNumbers),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);

        if (MainActivity.ecgRaLlHistory.size() > 60) {
            MainActivity.ecgRaLlHistory.removeFirst();
        }

        MainActivity.ecgRaLlHistory.addLast(EcgRaLl);

        MainActivity.ecgRaLlHistorySeries.setModel(MainActivity.ecgRaLlHistory,
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
        MainActivity.ecgHistoryPlot.redraw();
    }

}


