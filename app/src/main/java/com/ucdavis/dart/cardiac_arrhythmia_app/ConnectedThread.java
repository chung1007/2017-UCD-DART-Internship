package com.ucdavis.dart.cardiac_arrhythmia_app;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by sam on 7/8/17.
 */
public class ConnectedThread extends Thread {
    private BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private Activity context;

    public ConnectedThread(BluetoothSocket socket, Activity context) {
        this.context = context;
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
        setButtonClickListener();
        while(mmSocket != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mmInStream));
            try {
                while(!reader.readLine().equals("\0")){
                    //process incoming 200Hz of data.
                }
            } catch (IOException IO) {
                break;
            }
        }
    }

    public void setButtonClickListener() {
        final Button b= (Button) context.findViewById(R.id.requestButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("sent", "Request");
                try {
                    PrintWriter out;
                    out = new PrintWriter(mmOutStream, true);
                    out.println("*");
                    out.flush();
                }catch (NullPointerException NPE){

                }
            }
        });
    }
}
