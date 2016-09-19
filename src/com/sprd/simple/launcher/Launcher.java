package com.sprd.simple.launcher;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.sprd.simple.util.FlashlightController;
import com.sprd.simple.fragment.DefaultWorkspaceFragment;
import com.sprd.simple.fragment.FamilyWorkspaceFragment;
import com.sprd.simple.fragment.FourthWorkspaceFragment;
import com.sprd.simple.fragment.ThirdWorkspaceFragment;
import com.sprd.simple.util.AppIntentUtil;

import android.media.AudioManager;

import org.json.JSONArray;

/**
 * Created by SPDR on 2016/7/18.
 */
public class Launcher extends FragmentActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "Launcher";
    public static final String SOS_BROADCAST = "android.intent.action.SOS";
    public static final String STATUS_BAR_BROADCAST = "android.intent.action.show.statusbar";
    private static final String VOICE_MAIL_BROADCAST = "android.intent.action.VoiceMail";
    private static final int sFIRST_WORKSPACE = 0;
    private static final int sDEFAULT_WORKSPACE = 1;
    private static final int sTHIRD_WORKSPACE = 2;
    private static final int sFOURTH_WORKSPACE = 3;

    private static final int PERMISSION_REQUEST = 4;
    private static final int PERMISSION_ALL_ALLOWED = 5;
    private static final int PERMISSION_ALL_DENIED = 6;
    private static final int MINI_SDK_RETURN_VALUE = -1;
    public static boolean permissionFlag = false;
    public static final String DATABASE = "FamilyNumber";
    private String[] spData = new String[2];
    private static String[] mStrArray = new String[2];

    public static int mCurrPosition = 1;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the three primary sections of the app. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every loaded
     * fragment in memory. If this becomes too memory intensive, it may be best
     * to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the
     * app, one at a time.
     */
    ViewPager mViewPager;
    private Context mContext = null;

    // for FlashLight
    private FlashlightController fc;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mContext = this;
        fc = new FlashlightController(mContext);
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
                getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setCurrentItem(sDEFAULT_WORKSPACE);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // For Permission request
        switch (checkAppPermission(this)) {
            case Launcher.PERMISSION_ALL_ALLOWED: {
                Log.i(TAG, "permission allowed");
                break;
            }

            case Launcher.PERMISSION_ALL_DENIED: {
                Log.i(TAG, "permission denied");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[] {
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_SMS }, PERMISSION_REQUEST);
                }
                break;
            }

            case MINI_SDK_RETURN_VALUE: {
                break;
            }

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            permissionFlag = false;
                            Toast.makeText(Launcher.this, R.string.unread_hint,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            permissionFlag = true;
                        }
                    }
                }
            }
        }
    }

    private int checkAppPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean canReadCallLog = context
                    .checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED;
            boolean canReadSMS = context
                    .checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
            Log.i(TAG, "canReadCallLog = " + canReadCallLog);
            Log.i(TAG, "canReadSMS = " + canReadSMS);
            if (canReadCallLog && canReadSMS) {
                return PERMISSION_ALL_ALLOWED;
            } else {
                return PERMISSION_ALL_DENIED;
            }
        } else {
            return MINI_SDK_RETURN_VALUE;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        SharedPreferences sp = mContext.getSharedPreferences(DATABASE, Context.MODE_PRIVATE);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            // TODO
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            // TODO
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            // TODO
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            // TODO
        }

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

                case KeyEvent.KEYCODE_1:{
                    // Voice mail
                    Intent intent = new Intent(Launcher.VOICE_MAIL_BROADCAST);
                    this.sendBroadcast(intent);
                    Log.i(TAG, "send voice mail broadcast in Launcher Activity");
                    return true;
                }

                case KeyEvent.KEYCODE_2:{
                    // family number 1
                    if (sp.contains("0") == true) {
                        getData(this, 0, spData.length);
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Log.d(TAG, "strArray[1] = " + mStrArray[1]);
                        Uri data = Uri.parse("tel:" + mStrArray[1]);
                        intent.setData(data);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, mContext.getString(R.string.family_num1_not_set), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                case KeyEvent.KEYCODE_3:{
                    // family number 2
                    if (sp.contains("1") == true) {
                        getData(this, 1, spData.length);
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Log.d(TAG, "strArray[1] = " + mStrArray[1]);
                        Uri data = Uri.parse("tel:" + mStrArray[1]);
                        intent.setData(data);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, mContext.getString(R.string.family_num2_not_set), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                case KeyEvent.KEYCODE_4:{
                    // family number 3
                    if (sp.contains("2") == true) {
                        getData(this, 2, spData.length);
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Log.d(TAG, "strArray[1] = " + mStrArray[1]);
                        Uri data = Uri.parse("tel:" + mStrArray[1]);
                        intent.setData(data);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, mContext.getString(R.string.family_num3_not_set), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                case KeyEvent.KEYCODE_6: {
                    // FM Radio
                    Intent intent = new Intent();
                    intent.setClassName(getResources().getString(R.string.fm_package),
                            getResources().getString(R.string.fm_activity));
                    AppIntentUtil.intentSetFlag(intent);
                    startActivity(intent);
                    return true;
                }

                case KeyEvent.KEYCODE_0:{
                    // Flashlight
                    fc.setFlashlight(!fc.isEnabled());
                    return true;
                }

                case KeyEvent.KEYCODE_POUND:{
                    // Silent mode
                    AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);
                    if(audioManager.getRingerMode()==AudioManager.RINGER_MODE_SILENT){
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    }else{
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    }
                    return true;
                }

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * get the data from SharedPreference
     *
     * @param context
     * @param position
     * @param arrayLength
     * @return
     */
    public String[] getData(Context context, int position, int arrayLength) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE, Context.MODE_PRIVATE);
            mStrArray = new String[arrayLength];
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString(position + "", ""));
            for (int i = 0; i < jsonArray.length(); i++) {
                mStrArray[i] = jsonArray.getString(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mStrArray;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            Intent intent = new Intent(Launcher.STATUS_BAR_BROADCAST);
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            this.sendBroadcast(intent);
            Log.i(TAG, "send status bar broadcast in Launcher Activity");
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrPosition = position;
        if (mCurrPosition == 1) {
            new DefaultWorkspaceFragment().mAdapter.notifyDataSetChanged();
            Log.d(TAG, "notifyDataSetChanged DefaultWorkspaceFragment");
        } else if (mCurrPosition == 2) {
            new ThirdWorkspaceFragment().mAdapter.notifyDataSetChanged();
            Log.d(TAG, "notifyDataSetChanged ThirdWorkspaceFragment");
        } else if (mCurrPosition == 3) {
            new FourthWorkspaceFragment().mAdapter.notifyDataSetChanged();
            Log.d(TAG, "notifyDataSetChanged FourthWorkspaceFragment ");
        }
        Log.d(TAG, "mCurrPosition = " + mCurrPosition);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the primary sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        private Fragment currentFragment;

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case sFIRST_WORKSPACE:
                    // The first section of the app is the most interesting --
                    // it offers
                    // a launchpad into the other demonstrations in this example
                    // application.
                    return new FamilyWorkspaceFragment();

                case sDEFAULT_WORKSPACE:
                    return new DefaultWorkspaceFragment();

                case sTHIRD_WORKSPACE:
                    return new ThirdWorkspaceFragment();

                default:
                    // The other sections of the app are dummy placeholders.
                    Fragment fragment = new FourthWorkspaceFragment();
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position,
                Object object) {
            currentFragment = (Fragment) object;
            super.setPrimaryItem(container, position, object);
        }

        public Fragment getCurrentFragment() {
            return currentFragment;
        }

    }

}
