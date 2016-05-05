package com.example.elnemr.final_movie_app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by elnemr on 5/5/16.
 */
public class MyAdapter extends BaseAdapter {

    private static final String TAG = MyAdapter.class.getSimpleName();
    Context mycontext;
    List<Pojo> objects;

    public MyAdapter(Context context, List<Pojo> objects) {
        this.mycontext = context;
        this.objects = objects;
    }


    public class ViewHolder {
        ImageView imageView;
        TextView year;
        TextView vote_average;
        TextView overview;
        TextView title;

    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Pojo pojo = (Pojo) objects.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row, parent, false);

            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.year = (TextView) convertView.findViewById(R.id.deatil_movie_year);
            holder.vote_average = (TextView) convertView.findViewById(R.id.detail_movie_vote_Average);
            holder.overview = (TextView) convertView.findViewById(R.id.detail_movie_overview);
            holder.title = (TextView) convertView.findViewById(R.id.detail_movie_title);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(mycontext)
                .load("http://image.tmdb.org/t/p/w500/" + pojo.getImageurl())
                .into(holder.imageView);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Detail_Fragment fragment = new Detail_Fragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("on", pojo);
                fragment.setArguments(bundle);
                ((MainActivity) mycontext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, fragment).addToBackStack(null).commit();



            }
        });
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

    public void addAll(List<Pojo> list){
        objects.clear();
        objects.addAll(list);
        notifyDataSetChanged();
    }
}