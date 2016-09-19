package com.sprd.simple.adapter;

import java.util.List;

import com.sprd.simple.model.AppInfo;
import com.sprd.simple.model.TextSpeech;
import com.sprd.simple.launcher.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BrowseApplicationInfoAdapter extends BaseAdapter {

    private List<AppInfo> mlistAppInfo = null;

    LayoutInflater infater = null;
    private Context mContext;
    private int mPosition;

    public BrowseApplicationInfoAdapter(Context context, List<AppInfo> apps) {
        infater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mlistAppInfo = apps;
        mContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        System.out.println("size" + mlistAppInfo.size());
        return mlistAppInfo.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mlistAppInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup arg2) {
        View view = null;
        ViewHolder holder = null;
        if (convertview == null || convertview.getTag() == null) {
            view = infater.inflate(R.layout.browser_appinfo_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertview;
            holder = (ViewHolder) convertview.getTag();
        }
        AppInfo appInfo = (AppInfo) getItem(position);
        holder.appIcon.setImageDrawable(appInfo.getAppIcon());
        holder.appLabel.setText(appInfo.getAppLabel());

        if (mPosition == position) {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.selected_list_color));
        } else {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.app_name_color));
        }
        return view;
    }

    class ViewHolder {
        ImageView appIcon;
        TextView appLabel;

        public ViewHolder(View view) {
            this.appIcon = (ImageView) view.findViewById(R.id.browser_appinfo_list_item_icon);
            this.appLabel = (TextView) view.findViewById(R.id.browser_appinfo_list_item_label);
        }
    }

    public void setPosition(int pos) {
        mPosition = pos;
    }

    public int getPosition() {
        return mPosition;
    }
}