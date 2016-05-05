package com.example.elnemr.final_movie_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by elnemr on 5/5/16.
 */
public class ReviewAdapter extends BaseAdapter {

    Context mycontext;
    List<Pojo> objects;

    public ReviewAdapter(Context context, List<Pojo> objects) {
        this.mycontext = context;
        this.objects = objects;
    }

    public class ViewHolder {
        TextView main;
        TextView sub_item;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Pojo pojo = (Pojo) objects.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater)
                    mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.review, null);

            holder = new ViewHolder();
            holder.main = (TextView) convertView.findViewById(R.id.main_item);
            holder.sub_item = (TextView) convertView.findViewById(R.id.sub_item);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }


        holder.main.setText(objects.get(position).getAuthor());
        holder.sub_item.setText(objects.get(position).getContent());

        return convertView;

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);

    }
}
