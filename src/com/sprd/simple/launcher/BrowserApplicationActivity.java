package com.sprd.simple.launcher;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.sprd.simple.adapter.BrowseApplicationInfoAdapter;
import com.sprd.simple.adapter.ToolsItemAdapter;
import com.sprd.simple.model.AppInfo;
import com.sprd.simple.model.IconInfo;
import com.sprd.simple.util.AppIntentUtil;
import com.sprd.simple.util.FlashlightController;
import com.sprd.simple.util.PackageInfoUtil;
import com.sprd.simple.util.StatusBarUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class BrowserApplicationActivity extends Activity implements
        AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "BrowserApplicationActivity";
    public static final String DATABASE = "FamilyNumber";

    private ListView mBrowserListView;
    private BrowseApplicationInfoAdapter mAdapter = null;

    private Button mBTChoose;
    private Button mBTBack;

    private int mListSize;

    private Context mContext;

    private ArrayList<AppInfo> mArrayList;

    private String stk = "com.android.stk";
    private String download = "com.android.providers.downloads.ui";

    private String[] spData = new String[2];
    private static String[] mStrArray = new String[2];

    // for FlashLight
    private FlashlightController fc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        StatusBarUtils.setWindowStatusBarColor(this); // change status bar color
        setContentView(R.layout.activity_browser_appinfo);

        mContext = this;
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.browser_title_bar);

        mBTChoose = (Button) findViewById(R.id.browser_btn_choose);
        mBTBack = (Button) findViewById(R.id.browser_btn_back);

        mBrowserListView = (ListView) findViewById(R.id.browser_appinfo_folder_item);
        mAdapter = new BrowseApplicationInfoAdapter(this, getData(PackageInfoUtil.FILTER_SYSTEM_APP));
        mAdapter.setPosition(0);
        mBrowserListView.setAdapter(mAdapter);
        mBrowserListView.setOnItemLongClickListener(this);
        mBrowserListView.setOnItemClickListener(this);

        fc = new FlashlightController(mContext);
    }
    
    private List<AppInfo> getData(int filter) {
        mArrayList = new ArrayList<>();
        ArrayList<AppInfo> dataList = PackageInfoUtil.queryFilterAppInfo(mContext, filter);

        for (int i = 0; i < dataList.size(); i++) {
            String name = dataList.get(i).getAppPkg();
            Log.i(TAG, "" + name);
            if(name.equals(stk) || name.equals(download)){
                mArrayList.add(dataList.get(i));
            }
        }

        Log.i(TAG, "mArrayList.size = " + mArrayList.size());
        mListSize = mArrayList.size();
        return mArrayList;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch ((event.getKeyCode())) {
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (event.getAction() == KeyEvent.ACTION_UP)
                        return true;
                    mAdapter.setPosition(mAdapter.getPosition() == mListSize -1 ? 0
                            : (mAdapter.getPosition() + 1));
                    Log.i(TAG, "KEYCODE_DPAD_DOWN mAdapter.getPosition() = "
                            + mAdapter.getPosition());
                    mAdapter.notifyDataSetChanged();
                    if (mBrowserListView.getLastVisiblePosition() < mAdapter
                            .getPosition()
                            || mBrowserListView.getLastVisiblePosition() == mListSize -1 ) {
                        mBrowserListView.setSelection(mAdapter.getPosition());
                    }
                    return true;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (event.getAction() == KeyEvent.ACTION_UP)
                        return true;
                    mAdapter.setPosition(mAdapter.getPosition() == 0 ? mListSize - 1
                            : (mAdapter.getPosition() - 1));
                    Log.i(TAG, "KEYCODE_DPAD_UP mAdapter.getPosition() = "
                            + mAdapter.getPosition());
                    mAdapter.notifyDataSetChanged();
                    if (mBrowserListView.getFirstVisiblePosition() > mAdapter
                            .getPosition()
                            || mBrowserListView.getFirstVisiblePosition() == 0) {
                        mBrowserListView.setSelection(mAdapter.getPosition());
                    }
                    return true;
                case KeyEvent.KEYCODE_ENTER:
                case KeyEvent.KEYCODE_DPAD_CENTER: {
                    launchApp(mArrayList.get(mAdapter.getPosition())
                            .getAppPkg());
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        mAdapter.setPosition(position);
        mAdapter.notifyDataSetChanged();
        launchApp(mArrayList.get(position).getAppPkg());

    }

    private void launchApp(String packageName) {
        try{
            Intent intent = new Intent();
            AppIntentUtil.intentSetFlag(intent);
            intent = this.getPackageManager().getLaunchIntentForPackage(packageName);
            startActivity(intent);
        } catch (NullPointerException e){
            Log.i(TAG, "Open error");
            Toast.makeText(this, R.string.no_sim_card_inserted, Toast.LENGTH_LONG)
                .show();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id) {
        Intent intent = new Intent(Launcher.SOS_BROADCAST);
        sendBroadcast(intent);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        SharedPreferences sp = mContext.getSharedPreferences(DATABASE,
                Context.MODE_PRIVATE);
        if (event.isLongPress()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_MENU: {
                    Log.i(TAG, "event.isLongPress() = " + event.isLongPress()
                            + "   keyCode = " + keyCode);
                    Intent intent = new Intent(Launcher.STATUS_BAR_BROADCAST);
                    intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                    this.sendBroadcast(intent);
                    Log.i(TAG, "send status bar broadcast in Launcher Activity");
                    return true;
                }

                case KeyEvent.KEYCODE_1: {
                    // Voice mail
                    Intent intent = new Intent(
                            ToolsActivity.VOICE_MAIL_BROADCAST);
                    this.sendBroadcast(intent);
                    Log.i(TAG, "send voice mail broadcast in ToolsActivity");
                    return true;
                }

                case KeyEvent.KEYCODE_2: {
                    // family number 1
                    if (sp.contains("0") == true) {
                        getFamilyNumber(this, 0, spData.length);
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Log.d(TAG, "strArray[1] = " + mStrArray[1]);
                        Uri data = Uri.parse("tel:" + mStrArray[1]);
                        intent.setData(data);
                        startActivity(intent);
                    } else {
                        Toast.makeText(
                                this,
                                mContext.getString(R.string.family_num1_not_set),
                                Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                case KeyEvent.KEYCODE_3: {
                    // family number 2
                    if (sp.contains("1") == true) {
                        getFamilyNumber(this, 1, spData.length);
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Log.d(TAG, "strArray[1] = " + mStrArray[1]);
                        Uri data = Uri.parse("tel:" + mStrArray[1]);
                        intent.setData(data);
                        startActivity(intent);
                    } else {
                        Toast.makeText(
                                this,
                                mContext.getString(R.string.family_num2_not_set),
                                Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                case KeyEvent.KEYCODE_4: {
                    // family number 3
                    if (sp.contains("2") == true) {
                        getFamilyNumber(this, 2, spData.length);
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Log.d(TAG, "strArray[1] = " + mStrArray[1]);
                        Uri data = Uri.parse("tel:" + mStrArray[1]);
                        intent.setData(data);
                        startActivity(intent);
                    } else {
                        Toast.makeText(
                                this,
                                mContext.getString(R.string.family_num3_not_set),
                                Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                case KeyEvent.KEYCODE_6: {
                    // FM Radio
                    Intent intent = new Intent();
                    intent.setClassName(
                            getResources().getString(R.string.fm_package),
                            getResources().getString(R.string.fm_activity));
                    AppIntentUtil.intentSetFlag(intent);
                    startActivity(intent);
                    return true;
                }

                case KeyEvent.KEYCODE_0: {
                    // Flashlight
                    fc.setFlashlight(!fc.isEnabled());
                    return true;
                }

                case KeyEvent.KEYCODE_POUND: {
                    // Silent mode
                    AudioManager audioManager = (AudioManager) getApplicationContext()
                            .getSystemService(AUDIO_SERVICE);
                    if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                        audioManager
                                .setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    } else {
                        audioManager
                                .setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    }
                    return true;
                }

            }
        }

        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                mBTChoose.setPressed(true);
                break;
            case KeyEvent.KEYCODE_BACK:
                mBTBack.setPressed(true);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            Intent intent = new Intent(Launcher.STATUS_BAR_BROADCAST);
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            this.sendBroadcast(intent);
            Log.i(TAG, "send status bar broadcast in ToolsActivity");
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    /**
     * get the data from SharedPreference
     *
     * @param context
     * @param position
     * @param arrayLength
     * @return
     */
    public String[] getFamilyNumber(Context context, int position, int arrayLength) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    DATABASE, Context.MODE_PRIVATE);
            mStrArray = new String[arrayLength];
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString(
                    position + "", ""));
            for (int i = 0; i < jsonArray.length(); i++) {
                mStrArray[i] = jsonArray.getString(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mStrArray;
    }

}
