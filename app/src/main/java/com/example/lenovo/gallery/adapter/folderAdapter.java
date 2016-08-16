package com.example.lenovo.gallery.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.gallery.R;
import com.example.lenovo.gallery.bean.FolderBean;

import java.util.ArrayList;
import java.util.List;

public class folderAdapter extends BaseAdapter {

    private Context context;
    private List<FolderBean> folderList = new ArrayList<FolderBean>();
    private boolean isFolder = true;

    public folderAdapter(Context context, List<FolderBean> folderList,
                             boolean Folder) {
        isFolder = Folder;
        this.context = context;
        if (null != folderList) {
            this.folderList = folderList;

        }
    }

    public void updateList(List<FolderBean> newList) {
        if (null != newList) {
            this.folderList = newList;

        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return folderList.size();
    }

    @Override
    public Object getItem(int position) {
        return folderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (isFolder) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.item_folder_layout, null);

                holder = new MyViewHolder();
                holder.ImageIV = (ImageView) convertView
                        .findViewById(R.id.view_folder_list);
                holder.nameTextView = (TextView) convertView
                        .findViewById(R.id.list_item_folderNameView);
                holder.numberTextView = (TextView) convertView
                        .findViewById(R.id.list_item_folderNumberView);
                convertView.setTag(holder);
            } else {
                holder = (MyViewHolder) convertView.getTag();
            }

            FolderBean folderBin = (FolderBean) getItem(position);
            holder.nameTextView.setText(folderBin.folderName);
            String numberText = folderBin.imageNumber + "张图片";
            holder.numberTextView.setText(numberText);

            if (folderBin.folderUpdate) {
                holder.updateTextView.setText("NEW");
            }
            return convertView;

        } else {
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
            FolderBean streamBin = (FolderBean) getItem(position);

            Bitmap videoImage = streamBin.getImage();


////            if (null == videoImage) {
////                holder.ImageIV.setImageResource(R.mipmap.ic_launcher);
////
////            } else {
//                holder.ImageIV.setImageBitmap(videoImage);
////            }


            holder.nameTextView.setText(streamBin.imageName);
            Glide
                    .with(context)
                    .load(streamBin.imagePath)
                    .into(holder.ImageIV);

            return convertView;
        }

    }

    class MyViewHolder {
        ImageView ImageIV;
        TextView nameTextView;
        TextView updateTextView;
        TextView numberTextView;
        TextView timeTextView;
    }

}
