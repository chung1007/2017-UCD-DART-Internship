package com.ucdavis.dart.cardiac_arrhythmia_app;

import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mDevice;
    public static XYPlot ecgHistoryPlot = null;
    public static SimpleXYSeries ecgLevelsSeries = new SimpleXYSeries(
            "ECG History");
    public static SimpleXYSeries ecgRaLlHistorySeries = new SimpleXYSeries("Pulse");
    public static LinkedList<Double> ecgRaLlHistory = new LinkedList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
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
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        ecgHistoryPlot.setBackgroundColor(Color.GRAY);
        ecgHistoryPlot
                .setRangeBoundaries(-0.500, -0.200, BoundaryMode.FIXED);
        ecgHistoryPlot.setDomainBoundaries(0.0, Constants.domain, BoundaryMode.FIXED);
        ecgHistoryPlot.addSeries(ecgRaLlHistorySeries,
                new LineAndPointFormatter(
                        Color.rgb(0, 255, 0), null, null, null));
        ecgHistoryPlot.setDomainStepValue(1.0);
        ecgHistoryPlot.setTicksPerRangeLabel(3);
        ecgHistoryPlot.setDomainLabel("Time");
        ecgHistoryPlot.getDomainLabelWidget().pack();
        ecgHistoryPlot.setRangeLabel("Level");
        ecgHistoryPlot.getRangeLabelWidget().pack();
    }

}
