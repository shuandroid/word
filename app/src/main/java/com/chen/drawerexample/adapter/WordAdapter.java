package com.chen.drawerexample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chen.drawerexample.R;

import java.util.ArrayList;

/**
 * Created by chen
 * Date : 16-1-3
 * Name : DrawerExample
 */
public class WordAdapter extends BaseAdapter {


    private LayoutInflater mLayoutInflater;

    private ArrayList<String> name;

    private ArrayList<String> tags;

    public WordAdapter(Context context, ArrayList<String> name, ArrayList<String> tags) {
        mLayoutInflater = LayoutInflater.from(context);
        this.name = name;
        this.tags = tags;
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.word_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mWordName.setText(name.get(position));

        return convertView;
    }

    private class ViewHolder{


        TextView mWordName;

        public ViewHolder (View view) {
            mWordName = (TextView) view.findViewById(R.id.word_name);
        }

    }
}
