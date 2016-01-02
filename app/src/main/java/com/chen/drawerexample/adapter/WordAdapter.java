package com.chen.drawerexample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chen.drawerexample.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chen
 * Date : 16-1-3
 * Name : DrawerExample
 */
public class WordAdapter extends BaseAdapter {


    private LayoutInflater mLayoutInflater;

    private ArrayList<String> name;

    private ArrayList<String> tags;


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
        viewHolder.mWordTag.setText(tags.get(position));

        return convertView;
    }

    private class ViewHolder{

        @Bind(R.id.word_name)
        TextView mWordName;

        @Bind(R.id.word_tag)
        TextView mWordTag;

        public ViewHolder (View view) {
            ButterKnife.bind(this, view);

        }

    }
}
