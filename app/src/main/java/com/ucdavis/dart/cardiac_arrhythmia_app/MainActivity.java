package com.ucdavis.dart.cardiac_arrhythmia_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import java.util.LinkedList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mDevice;
    public static XYPlot ecgHistoryPlot = null;
    public static SimpleXYSeries ecgLevelsSeries = new SimpleXYSeries(
            "ECG History");
    public static SimpleXYSeries ecgRaLlHistorySeries = new SimpleXYSeries("Pulse");
    public static LinkedList<Integer> ecgRaLlHistory = new LinkedList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setMergedPlotParam();
        connect();
        AcceptThread mAcceptThread = new AcceptThread(mDevice, mBluetoothAdapter, MainActivity.this);
        mAcceptThread.start();
    }

    private void connect(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Utils.makeToast(this, "Device Does Not Support Bluetooth");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); startActivityForResult(enableBtIntent, 1);
        }
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices(); if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mDevice = device;
            } }
    }

    private void setMergedPlotParam() {
        ecgHistoryPlot = (XYPlot)findViewById(R.id.ecgSPlot);
        ecgHistoryPlot
                .setRangeBoundaries(-10, 100, BoundaryMode.FIXED);
        ecgHistoryPlot.setDomainBoundaries(0, 60, BoundaryMode.FIXED);
        ecgHistoryPlot.addSeries(ecgRaLlHistorySeries,
                new LineAndPointFormatter(
                        Color.rgb(0, 255, 0), null, null, null));
        ecgHistoryPlot.setDomainStepValue(5);
        ecgHistoryPlot.setTicksPerRangeLabel(3);
        ecgHistoryPlot.setDomainLabel("Time");
        ecgHistoryPlot.getDomainLabelWidget().pack();
        ecgHistoryPlot.setRangeLabel("Level");
        ecgHistoryPlot.getRangeLabelWidget().pack();
        //ecgHistoryPlot.disableAllMarkup();
    }
}
