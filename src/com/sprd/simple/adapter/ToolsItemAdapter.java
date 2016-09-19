package com.sprd.simple.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sprd.simple.launcher.R;
import com.sprd.simple.model.IconInfo;
import com.sprd.simple.model.TextSpeech;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by SPRD on 2016/7/19.
 */
public class ToolsItemAdapter extends BaseAdapter {

    private final static String TAG = "ToolsItemAdapter";

    private ArrayList<IconInfo> mArrayList = new ArrayList<>();
    private Context mContext;
    private int mPosition;

    int pm[] = {R.string.sos_name, R.string.call_fire_wall_name, R.string.sound_recorder_name, R.string.note_name,
            R.string.backup_name, R.string.file_manager_name, R.string.calculator_name};

    public ToolsItemAdapter(ArrayList<IconInfo> arrayList, Context context) {
        mArrayList = arrayList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IconHolder iconHolder = null;
        if (convertView == null) {
            iconHolder = new IconHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.tools_app_item_appliction, null);
            iconHolder.iconName = (TextView) convertView.findViewById(R.id.list_item_name_icon);
            iconHolder.iconImage = (ImageView) convertView.findViewById(R.id.list_item_image_icon);
            convertView.setTag(iconHolder);
        } else {
            iconHolder = (IconHolder) convertView.getTag();
        }

        iconHolder.iconName.setText(mArrayList.get(position).getIconName());
        iconHolder.iconImage.setImageResource(mArrayList.get(position).getIconImage());

        if (mPosition == position) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.selected_list_color));
            TextSpeech.read(position, pm, mContext);
            Log.d(TAG, "read() tools ");
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.app_name_color));
        }

        return convertView;
    }

    public class IconHolder {
        private TextView iconName;
        private ImageView iconImage;
    }

    public void setPosition(int pos) {
        mPosition = pos;
    }

    public int getPosition() {
        return mPosition;
    }
}
