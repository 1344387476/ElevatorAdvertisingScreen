package net.cloudelevator.elevatorcontrol.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.cloudelevator.elevatorcontrol.R;
import net.cloudelevator.elevatorcontrol.bean.SettingsBean;

import java.util.ArrayList;

public class SettingListAdapter extends BaseAdapter {
    private ArrayList<SettingsBean> mData;
    private Context mContext;

    public SettingListAdapter(Context context,ArrayList<SettingsBean> data){
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.setting_item,null);
        TextView title = view.findViewById(R.id.item_title);
        TextView content = view.findViewById(R.id.item_content);

        title.setText(mData.get(position).getTitle());
        content.setText(mData.get(position).getContent());
        return view;
    }
}
