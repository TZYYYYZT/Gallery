package com.example.lenovo.gallery.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.gallery.R;
import com.example.lenovo.gallery.bean.FolderBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UsbAdapter extends BaseAdapter {

    private Context context;
    private List<FolderBean> folderList = new ArrayList<FolderBean>();
    private boolean isFolder = true;
    private boolean usbDeviceFlag = false;

    public void setUSBDeviceFlag(boolean flag) {
        usbDeviceFlag = flag;
    }


    public UsbAdapter(Context context, List<FolderBean> folderList,
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
        File file = new File(folderList.get(position).getFolderPath());
        isFolder = file.isDirectory();

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

            FolderBean folderBin = folderList.get(position);
            if(usbDeviceFlag)
            holder.ImageIV.setImageResource(R.mipmap.ic_launcher);
            else
            holder.ImageIV.setImageResource(R.mipmap.item_folder_grid);
            holder.nameTextView.setText(folderBin.folderName);
            String numberText = folderBin.imageNumber + "张图片";
            Log.d("www", "numberText!!" + numberText);
            holder.numberTextView.setText(numberText);

            if (folderBin.folderUpdate) {
                holder.updateTextView.setText("NEW");
            }


        } else {
            Log.d("wmy", "USBadapter is image");
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
            FolderBean folderBin = folderList.get(position);

            Bitmap videoImage = folderBin.getImage();


            holder.nameTextView.setText(folderBin.folderName);
            Glide
                    .with(context)
                    .load(folderBin.folderPath)
                    .into(holder.ImageIV);


        }
        return convertView;
    }

    class MyViewHolder {
        ImageView ImageIV;
        TextView nameTextView;
        TextView updateTextView;
        TextView numberTextView;
        TextView timeTextView;
    }

}
