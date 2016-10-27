package com.example.user_pc.hospital;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by User-PC on 2016/10/6.
 */

public class SanchongAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    // 用來儲存row裡每個view的id，以免每次都要取一次
    private static class ViewHolder {
        public TextView typeTextView;
        public TextView titleTextView;
        public TextView publisherTextView;
        public TextView datetimeTextView;
    }

    // 類別的建構子
    public SanchongAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mJsonArray = new JSONArray();
    }

    // 輸入JSON資料
    public void updateData(JSONArray jsonArray) {
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        return mJsonArray.optJSONObject(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // 檢查view是否已存在，如果已存在就不用再取一次id
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_main, null);
            holder = new ViewHolder();
            holder.typeTextView = (TextView) convertView.findViewById(R.id.text_type);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.text_title);
            holder.publisherTextView = (TextView) convertView.findViewById(R.id.text_publisher);
            holder.datetimeTextView = (TextView) convertView.findViewById(R.id.text_datetime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 取得目前這個Row的JSON資料
        JSONObject jsonObject = (JSONObject) getItem(position);
        Log.d("jsonObject Text:", jsonObject.toString());

            String type = "";
            String title = "";
            String publisher = "";
            String datetime = "";
            if (jsonObject.has("type")){
                type = jsonObject.optString("type");
            }
            if (jsonObject.has("title")) {
                title = jsonObject.optString("title");
            }
            if (jsonObject.has("publisher")) {
                publisher = jsonObject.optString("publisher");
            }
            if (jsonObject.has("datetime")) {
                datetime = jsonObject.optString("datetime");
            }

            // 將標題和摘要顯示在TextView上
            holder.typeTextView.setText(type+"(三重院區)");
            holder.titleTextView.setText("看診進度描述："+title);
            holder.publisherTextView.setText("資料提供者："+publisher);
            holder.datetimeTextView.setText("資料更新時間："+datetime);

            return convertView;
        }

}

