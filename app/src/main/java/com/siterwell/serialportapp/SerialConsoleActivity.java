package com.siterwell.serialportapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.siterwell.seriallibrary.usbserial.event.SerialReceiveEvent;
import com.siterwell.seriallibrary.usbserial.event.SerialSendEvent;
import com.siterwell.seriallibrary.usbserial.util.HexDump;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class SerialConsoleActivity extends Activity {

    private final String TAG = SerialConsoleActivity.class.getSimpleName();

    private ScrollView mScrollView;
    private TextView mTitleTextView;
    private TextView mDumpTextView;
    private Button mbtn_fasong;
    private byte[] ds = {0x01,0x02,0x03};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.serial_console);
        mTitleTextView = (TextView) findViewById(R.id.demoTitle);
        mDumpTextView = (TextView) findViewById(R.id.consoleText);
        mScrollView = (ScrollView) findViewById(R.id.demoScroller);
        mbtn_fasong =(Button)findViewById(R.id.fasong);
        mbtn_fasong.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                SerialSendEvent serialSendEvent = new SerialSendEvent();
                serialSendEvent.setContent(ds);
                EventBus.getDefault().post(serialSendEvent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void updateReceivedData(byte[] data) {
        final String message = "Read " + data.length + " bytes: \n"
                + HexDump.dumpHexString(data) + "\n\n";
        mDumpTextView.append(message);
        mScrollView.smoothScrollTo(0, mDumpTextView.getBottom());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SerialReceiveEvent event) {
        byte[] ds = event.getReceive_data();
        updateReceivedData(ds);
    }

}
