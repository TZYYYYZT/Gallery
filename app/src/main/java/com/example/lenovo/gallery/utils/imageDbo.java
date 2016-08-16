package com.example.lenovo.gallery.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

import com.example.lenovo.gallery.bean.ImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class imageDbo {

    private Context context;

    /**
     * 保存系统上所有图片
     */
    private List<ImageBean> myImageList = null;

    public imageDbo(Context context) {
        this.context = context;
    }

    private ImageBean myImageBean = null;

    /**
     * 功能：<获取当前系统上的所有有效的图片文件><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-16<br>
     * 修改时间：2016-5-16<br>
     * 版 本：V1.0<br>
     *
     * @param key
     * @return<br>
     */
    public List<ImageBean> getImageListFromSystem(String key, String order) {

        List<ImageBean> myList = new ArrayList<ImageBean>();
        /* 链接数据库 */
        ContentResolver resolver = context.getContentResolver();
        /* 查询数据 */
        Cursor cursor;
        cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null, null, Images.Media.DATE_ADDED + " " + order);
//                null, null, null, Images.Media.DEFAULT_SORT_ORDER);

        ImageBean vBean;
        int imageNumber = 0;
        while (cursor.moveToNext()) {
            vBean = new ImageBean();
            // 路径
            String imagePath = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            vBean.imagePath = imagePath;

			/* 验证文件有效 */
            File file = new File(imagePath);
//            Log.d("HYP", "file1:" + file.toString());
            if (file == null)
                continue;
            // ID
            int imageID = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Images.Media._ID));
            // 名称
            String imageName = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(Images.Media.DISPLAY_NAME));

            int index = imageName.lastIndexOf(".");
            //大小
            int width = cursor.getInt(cursor.getColumnIndex(Images.Media.WIDTH));
            int height = cursor.getInt(cursor.getColumnIndex(Images.Media.HEIGHT));
            String imageType = cursor.getString(cursor.getColumnIndex(Images.Media.MIME_TYPE));
            vBean.imageId = imageID;
            vBean.imageName = imageName;
            vBean.imageNameNonType = imageName.substring(0, index);
            String expandName = imageName.substring(index + 1);
            vBean.imageExpandName = expandName;
            vBean.width = width;
            vBean.height = height;
            vBean.imageTypeSys = imageType;
            // 目录路径
            vBean.imagePath = imagePath;
            // 日期
            String imageDate = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
            vBean.imageDate = imageDate;


//            if (key != null) {
//                cursor.close();
//                return null;
//            }
            imageNumber++;

            myList.add(vBean);

        }
        cursor.close();

        if (imageNumber == 0) {
            return null;
        }

        return myList;
    }

    /**
     * 功能：<获取缩略图><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-16<br>
     * 修改时间：2016-5-16<br>
     * 版 本：V1.0<br>
     *
     * @param id
     * @return<br>
     */
    public Bitmap getImage(int id) {
        System.out.println("---dddddddd-----");

        int index = -1;
        ImageBean imageBean = null;
        for (int i = 0; i < myImageList.size(); i++) {
            imageBean = myImageList.get(i);
            if (imageBean.getImageId() == id) {
                index = i;
                break;
            }
        }

        if (index < 0) {
            return null;
        }

        Bitmap myImage = imageBean.getImage();
        if (myImage != null) {
            return myImage;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        myImage = MediaStore.Images.Thumbnails.getThumbnail(
                context.getContentResolver(), id, Images.Thumbnails.MICRO_KIND,
                options);

        myImageList.get(index).setImage(myImage);
        return myImage;

    }

    public ImageBean getImageBeanByPath(String path) {

        String key = path;

        // 重新访问数据库获取具体视频信息
        getImageBeanByPath(key);

        if (myImageBean == null) {
            return null;
        }

        return myImageBean;
    }

    /**
     * 功能：<返回文件列表><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-16<br>
     * 修改时间：2016-5-16<br>
     * 版 本：V1.0<br>
     *
     * @param fromDB 是否重新访问数据库
     * @return<br>
     */
    public List<ImageBean> getImageList(boolean fromDB, String order, String newPath) {

        // 重新访问 ImageList, 更新 imageList
        List<ImageBean> imageList = new ArrayList<ImageBean>();
        imageList = getImageListFromSystem(null, order);
//      imageList = getImageList(true);
        if (null == imageList) {
            return null;
        }

        // 赋值 StreamList
        List<ImageBean> myList = new ArrayList<ImageBean>();
        ImageBean vBean;
        ImageBean sBean;
        for (int i = 0; i < imageList.size(); i++) {
            vBean = imageList.get(i);
            // list
            sBean = new ImageBean();
            sBean.image = vBean.getImage();
            sBean.imageDate = vBean.getImageDate();
            sBean.imageId = vBean.getImageId();
            sBean.imageName = vBean.getImageNameNonType();
            sBean.imagePath = vBean.getImagePath();
            if (sBean.imagePath.contains(newPath)) {
                continue;
            }
            sBean.height = vBean.getImageHeight();
            sBean.width = vBean.getImageWidth();
            sBean.imageTypeSys = vBean.getImageTypeSys();
            sBean.imageExpandName = vBean.getImageExpandName();
            myList.add(sBean);

        }
        myImageList = myList;
        return myImageList;

    }

}
