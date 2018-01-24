package com.siterwell.serialportapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TwoLineListItem;


import com.siterwell.seriallibrary.usbserial.Modbus.ModbusResolve;
import com.siterwell.seriallibrary.usbserial.bean.ErrorReadCofig;
import com.siterwell.seriallibrary.usbserial.dialog.ECAlertDialog;
import com.siterwell.seriallibrary.usbserial.driver.UsbSerialDriver;
import com.siterwell.seriallibrary.usbserial.driver.UsbSerialProber;
import com.siterwell.seriallibrary.usbserial.event.InitSerialEvent;
import com.siterwell.seriallibrary.usbserial.util.HexDump;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows a {@link ListView} of available USB devices.
 *
 * @author xuejunjun
 */
public class MainActivity extends Activity {

    private final String TAG = MainActivity.class.getSimpleName();

    private UsbManager mUsbManager;
    private ListView mListView;
    private TextView mProgressBarTitle;
    private ProgressBar mProgressBar;
    private Button button_manage;

    private static final int MESSAGE_REFRESH = 101;
    private static final long REFRESH_TIMEOUT_MILLIS = 5000;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_REFRESH:
                    refreshDeviceList();
                    mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH, REFRESH_TIMEOUT_MILLIS);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }

    };

    /** Simple container for a UsbDevice and its driver. */
    private static class DeviceEntry {
        public UsbDevice device;
        public UsbSerialDriver driver;

        DeviceEntry(UsbDevice device, UsbSerialDriver driver) {
            this.device = device;
            this.driver = driver;
        }
    }

    private List<DeviceEntry> mEntries = new ArrayList<DeviceEntry>();
    private ArrayAdapter<DeviceEntry> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mListView = (ListView) findViewById(R.id.deviceList);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBarTitle = (TextView) findViewById(R.id.progressBarTitle);
        button_manage = (Button)findViewById(R.id.manage);
        mAdapter = new ArrayAdapter<DeviceEntry>(this, android.R.layout.simple_expandable_list_item_2, mEntries) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final TwoLineListItem row;
                if (convertView == null){
                    final LayoutInflater inflater =
                            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = (TwoLineListItem) inflater.inflate(android.R.layout.simple_list_item_2, null);
                } else {
                    row = (TwoLineListItem) convertView;
                }

                final DeviceEntry entry = mEntries.get(position);
                final String title = String.format("Vendor %s Product %s",
                        HexDump.toHexString((short) entry.device.getVendorId()),
                        HexDump.toHexString((short) entry.device.getProductId()));
                row.getText1().setText(title);

                final String subtitle = entry.driver != null ?
                        entry.driver.getClass().getSimpleName() : "No Driver";
                row.getText2().setText(subtitle);

                return row;
            }

        };
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Pressed item " + position);
                if (position >= mEntries.size()) {
                    Log.w(TAG, "Illegal position.");
                    return;
                }

                final DeviceEntry entry = mEntries.get(position);
                final UsbSerialDriver driver = entry.driver;
                if (driver == null) {
                    Log.d(TAG, "No driver.");
                    return;
                }
                InitSerialEvent initSerialEvent = new InitSerialEvent();
                initSerialEvent.setUsbSerialDriver(driver);
                EventBus.getDefault().post(initSerialEvent);

                final Intent intent = new Intent(MainActivity.this, SerialConsoleActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                MainActivity.this.startActivity(intent);

            }
        });
        button_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ConfigListActivity.class);
                startActivity(intent);
            }
        });
        checkconfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(MESSAGE_REFRESH);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeMessages(MESSAGE_REFRESH);
    }

    private void checkconfig(){
         if(ModbusResolve.getInstance().errorReadCofig == ErrorReadCofig.ERROR_READ_COFIG_NULL){
             ECAlertDialog ecAlertDialog = ECAlertDialog.buildAlert(this, ErrorReadCofig.ERROR_READ_COFIG_NULL.getDesc(),null, new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialogInterface, int i) {
                       Intent intent = new Intent(MainActivity.this,ConfigListActivity.class);
                       startActivity(intent);
                 }
             });
             ecAlertDialog.show();
         }
    }

    private void refreshDeviceList() {
        showProgressBar();

        new AsyncTask<Void, Void, List<DeviceEntry>>() {
            @Override
            protected List<DeviceEntry> doInBackground(Void... params) {
                Log.d(TAG, "Refreshing device list ...");
                SystemClock.sleep(1000);
                final List<DeviceEntry> result = new ArrayList<DeviceEntry>();
                for (final UsbDevice device : mUsbManager.getDeviceList().values()) {
                    final List<UsbSerialDriver> drivers =
                            UsbSerialProber.probeSingleDevice(mUsbManager, device);
                    Log.d(TAG, "Found usb device: " + device);
                    if (drivers.isEmpty()) {
                        Log.d(TAG, "  - No UsbSerialDriver available.");
                        result.add(new DeviceEntry(device, null));
                    } else {
                        for (UsbSerialDriver driver : drivers) {
                            Log.d(TAG, "  + " + driver);
                            result.add(new DeviceEntry(device, driver));
                        }
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<DeviceEntry> result) {
                mEntries.clear();
                mEntries.addAll(result);
                mAdapter.notifyDataSetChanged();
                mProgressBarTitle.setText(
                        String.format("%s device(s) found",Integer.valueOf(mEntries.size())));
                hideProgressBar();
                Log.d(TAG, "Done refreshing, " + mEntries.size() + " entries found.");
            }

        }.execute((Void) null);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBarTitle.setText(R.string.refreshing);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }


}
