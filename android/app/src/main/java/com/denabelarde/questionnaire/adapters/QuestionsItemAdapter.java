package com.denabelarde.questionnaire.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by rygalang on 10/23/14.
 */
public class QuestionsItemAdapter extends BaseAdapter {

    ArrayList<String> data;
    LayoutInflater inflater;
    Context context;
    HashMap<String,String> selectedBarcode;

    public void refreshAdapter(ArrayList<String> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public QuestionsItemAdapter(Context context, ArrayList<String> data, HashMap<String, String> selectedBarcode) {
        this.context = context;
        this.data = data;
        this.selectedBarcode = selectedBarcode;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
//        if (view == null) {
//            view = inflater.inflate(R.layout.item_list_layout, viewGroup, false);
//            holder = new ViewHolder((TextView) view.findViewById(R.id.item_number), (TextView) view.findViewById(R.id.mps));
//            view.setTag(holder);
//        } else {
//            holder = (ViewHolder) view.getTag();
//        }

        return view;
    }

    public class ViewHolder {
        TextView item_number, mps;

        public ViewHolder(TextView item_number, TextView mps) {
            this.item_number = item_number;
            this.mps = mps;
        }
    }
}
