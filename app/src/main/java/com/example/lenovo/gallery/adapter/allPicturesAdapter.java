package com.example.lenovo.gallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.gallery.R;
import com.example.lenovo.gallery.bean.ImageBean;

import java.util.ArrayList;
import java.util.List;


public class AllPicturesAdapter extends BaseAdapter {
    private Context context;
    private List<ImageBean> imageList = new ArrayList<ImageBean>();

    public AllPicturesAdapter(Context context, List<ImageBean> imageList) {

        this.context = context;
        if (null != imageList) {
            this.imageList = imageList;
        }
    }

    /**
     * 功能：<更新 streamList><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-16<br>
     * 修改时间：2016-5-16<br>
     * 版          本：V1.0<br>
     *
     * @param newList<br>
     */

    public void updateList(List<ImageBean> newList) {

        if (null != newList) {
            this.imageList = newList;
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageList.size();
    }
//    public int getCount() { return Integer.MAX_VALUE;}

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_grid_allpictures, null);

            holder = new MyViewHolder();
            holder.ImageIV = (ImageView) convertView
                    .findViewById(R.id.imageView);
            holder.nameTextView = (TextView) convertView
                    .findViewById(R.id.item_title);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        ImageBean streamBin = (ImageBean) getItem(position);
      holder.nameTextView.setText(streamBin.imageName);
        Glide
                .with(context)
                .load(streamBin.imagePath)
                .into(holder.ImageIV);

        return convertView;
    }

    class MyViewHolder {
        TextView nameTextView;
        ImageView ImageIV;
    }
}