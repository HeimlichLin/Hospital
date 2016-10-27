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
 * Created by User-PC on 2016/10/19.
 */

public class HospitalAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    // 用來儲存row裡每個view的id，以免每次都要取一次
    private static class ViewHolder {
        public TextView depdayTextView;
        public TextView noonTextView;
        public TextView areaTextView;
        public TextView DepCodeTextView;
        public TextView depnameTextView;
        public TextView roomTextView;
        public TextView DocCodeTextView;
        public TextView docnameTextView;
    }

    // 類別的建構子
    public HospitalAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.hospitalrow_main, null);
            holder = new ViewHolder();
            holder.depdayTextView = (TextView) convertView.findViewById(R.id.text_depday);
            holder.noonTextView = (TextView) convertView.findViewById(R.id.text_noon);
            holder.areaTextView = (TextView) convertView.findViewById(R.id.text_area);
            holder.DepCodeTextView = (TextView) convertView.findViewById(R.id.text_DepCode);
            holder.depnameTextView = (TextView) convertView.findViewById(R.id.text_depname);
            holder.roomTextView = (TextView) convertView.findViewById(R.id.text_room);
            holder.DocCodeTextView = (TextView) convertView.findViewById(R.id.text_DocCode);
            holder.docnameTextView = (TextView) convertView.findViewById(R.id.text_docname);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 取得目前這個Row的JSON資料
        JSONObject jsonObject = (JSONObject) getItem(position);
        Log.d("jsonObject Text:", jsonObject.toString());

        String depday = "";
        String noon = "";
        String area = "";
        String DepCode = "";
        String depname = "";
        String room = "";
        String DocCode = "";
        String docname = "";

        if (jsonObject.has("depday")){
            depday = jsonObject.optString("depday");
        }
        if (jsonObject.has("noon")) {
            noon = jsonObject.optString("noon");
        }
        if (jsonObject.has("area")) {
            area = jsonObject.optString("area");
        }
        if (jsonObject.has("DepCode")) {
            DepCode = jsonObject.optString("DepCode");
        }
        if (jsonObject.has("depname")) {
            depname = jsonObject.optString("depname");
        }
        if (jsonObject.has("room")) {
            room = jsonObject.optString("room");
        }
        if (jsonObject.has("DocCode")) {
            DocCode = jsonObject.optString("DocCode");
        }
        if (jsonObject.has("docname")) {
            docname = jsonObject.optString("docname");
        }

        // 將標題和摘要顯示在TextView上
        holder.depdayTextView.setText("門診日期："+depday);
        holder.noonTextView.setText("看診時間："+noon);
        holder.areaTextView.setText("看診院區："+area);
        holder.DepCodeTextView.setText("科別代碼："+DepCode);
        holder.depnameTextView.setText("科別名稱："+depname);
        holder.roomTextView.setText("診間代碼 ："+room);
        holder.DocCodeTextView.setText("醫師代碼："+DocCode);
        holder.docnameTextView.setText("醫師姓名："+docname);

        return convertView;
    }
}

