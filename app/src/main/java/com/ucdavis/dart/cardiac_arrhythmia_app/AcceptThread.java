package com.ucdavis.dart.cardiac_arrhythmia_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by sam on 7/8/17.
 */
public class AcceptThread extends Thread {
    BluetoothAdapter mBluetoothAdapter;
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private static final UUID MY_UUID = UUID.fromString("f741fa52-bd68-4f10-8529-934017a049b8");

    public AcceptThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        mmDevice = device;
        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {

        }
        mmSocket = tmp;
    }
    public void run() {
        mBluetoothAdapter.cancelDiscovery();
        try {
            mmSocket.connect();
        } catch (IOException connectException) {
            try {
                mmSocket.close();
            } catch (IOException closeException) {

            }
            return;
        }
        ConnectedThread mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }


}
