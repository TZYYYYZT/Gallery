package com.example.lenovo.gallery.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;

import com.example.lenovo.gallery.bean.FolderBean;
import com.example.lenovo.gallery.bean.ImageBean;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class USBDbo {

    private Context context;
    private String usbPath;
//    private String order = null;
    /**
     * 保存系统上所有节目
     */
    private List<ImageBean> myVideoList = null;

    /**
     * 包含图片的文件夹列表
     */
    private List<FolderBean> myFolderList = null;

    private List<FolderBean> myStreamList = null;

    /**
     * 保存最近搜索结果的节目列表
     */
    private List<ImageBean> myCurrentList = null;

    private ImageBean myimageBean = null;

    private boolean stopWaste = false;

    public USBDbo(Context context) {
        this.context = context;
    }

    /**
     * 功能：<获取当前路径的图片文件的ID><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-20<br>
     * 修改时间：2016-5-20<br>
     * 版          本：V1.0<br>
     *
     * @param path
     * @return<br>
     */

    public long getVideoID(String path) {

        List<ImageBean> myID = new ArrayList<ImageBean>();

		/* 链接数据库 */
        ContentResolver resolver = context.getContentResolver();

		/* 查询数据 */
        Cursor cursor;
        if (null == path) {

            cursor = resolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
        } else {

            cursor = resolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
                    MediaStore.Video.Media.DATA + "=?", new String[]{path},
                    null);
        }

        ImageBean vBean;
        while (cursor.moveToNext()) {
            vBean = new ImageBean();

            // ID
            String videoID = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Video.Media._ID));

            long id = Long.parseLong(videoID);
            return id;
        }
        return -1;
    }

    /**
     * 功能：<获取当前系统上的所有有效的图片文件><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-20<br>
     * 修改时间：2016-5-20<br>
     * 版          本：V1.0<br>
     *
     * @param key
     * @return<br>
     */
    public List<ImageBean> getVideoListFromSystem(String key, String order, String path) {

        List<ImageBean> myList = new ArrayList<ImageBean>();

		/* 链接数据库 */
        ContentResolver resolver = context.getContentResolver();

		/* 查询数据 */
        Cursor cursor;
        if (null == key) {

            cursor = resolver.query(
                    Images.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
//            Log.d("www", "cursor:  " + cursor);
        } else {

            cursor = resolver.query(
                    Images.Media.EXTERNAL_CONTENT_URI,
                    null, null, null, Images.Media.DATE_ADDED + " " + order);

        }
        if (cursor == null) {
            return null;
        }

        ImageBean vBean;
        int videoNumber = 0;
        while (cursor.moveToNext()) {
            vBean = new ImageBean();

            // 路径
            String videoPath = cursor.getString(cursor
                    .getColumnIndexOrThrow(Images.Media.DATA));

            vBean.imagePath = videoPath;

			/* 验证文件有效 */
            File file = new File(videoPath);
            if (!file.exists())
                break;

            // ID
            int videoID = cursor.getInt(cursor
                    .getColumnIndex(Images.Media._ID));

            vBean.imageId = videoID;


            // 名称
            String videoName = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(Images.Media.DISPLAY_NAME));

            int index = videoName.lastIndexOf(".");

            vBean.imageName = videoName;
            vBean.imageNameNonType = videoName.substring(0, index);
            String expandName = videoName.substring(index + 1);
            vBean.imageExpandName = expandName;
            if (expandName.contains("ts")) {

            }


            // 目录名称
            String videoFolderName = cursor.getString(cursor
                    .getColumnIndexOrThrow(Images.Media.DATA));
            String parentName = new File(videoFolderName).getParentFile().getName();
            vBean.videoFolderName = parentName;

            // 目录路径
            String videoFolderPath = file.getParent();
            vBean.videoFolderPath = videoFolderPath;

            // 日期
            String videoDate = cursor.getString(cursor
                    .getColumnIndex(Images.Media.DATE_MODIFIED));
            vBean.imageDate = videoDate;

            if (key != null) {
                myimageBean = vBean;
                cursor.close();
                return null;
            }
            videoNumber++;

            myList.add(vBean);

        }
        cursor.close();

        if (videoNumber == 0) {
            return null;
        }

        return myList;
    }

    /**
     * 功能：<获取某个图片的缩略图><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-20<br>
     * 修改时间：2016-5-20<br>
     * 版          本：V1.0<br>
     *
     * @param id
     * @return<br>
     */

    public Bitmap getVideoImage(int id) {
        Log.d("wmy", "videoImage id  :  " + id);
        int index = -1;
        ImageBean imageBean = null;
        Log.d("wmy", "myVideoList size  :  " + myVideoList.size());
        for (int i = 0; i < myVideoList.size(); i++) {
            imageBean = myVideoList.get(i);
            if (imageBean.getImageId() == id) {
                index = i;
                break;
            }
        }
        Log.d("wmy", "index  :  " + index);
        if (index < 0) {
            return null;
        }
        Log.d("wmy", "ImageBean  :  " + imageBean);
        Bitmap videoImage = imageBean.getImage();

        if (videoImage != null) {

            return videoImage;
        }
        Log.d("wmy", "videoImage  :  " + videoImage);
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        videoImage = MediaStore.Video.Thumbnails.getThumbnail(
                context.getContentResolver(), id, Images.Thumbnails.MICRO_KIND,
                options);
        Log.d("wmy", "vide22oImag2e  :  " + videoImage);
        myVideoList.get(index).setImage(videoImage);

        return videoImage;

    }


    /**
     * 功能：<给 VideoList 的videoImage 赋值><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-20<br>
     * 修改时间：2016-5-20<br>
     * 版          本：V1.0<br><br>
     */
    public void setVideoListImage() {
        List<ImageBean> newVideoList = myVideoList;

        int id = -1;
        Bitmap videoImage = null;
        for (int i = 0; i < newVideoList.size(); i++) {
            id = newVideoList.get(i).getImageId();
            videoImage = getVideoImage(id);

            newVideoList.get(i).setImage(videoImage);
        }

    }

    /**
     * 功能：<通过 path获取节目信息><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-20<br>
     * 修改时间：2016-5-20<br>
     * 版          本：V1.0<br>
     *
     * @param path
     * @return<br>
     */
    public ImageBean getimageBeanByPath(String path, String order) {

        String key = path;

        // 重新访问数据库获取具体图片信息
        getVideoListFromSystem(key, order, path);

        if (myimageBean == null) {
            return null;
        }

        return myimageBean;
    }


    /**
     * 功能：<获取 VideoList><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-20<br>
     * 修改时间：2016-5-20<br>
     * 版          本：V1.0<br>
     *
     * @param fromDB
     * @return<br>
     */
    private List<ImageBean> getVideoList(boolean fromDB, String order) {

        // 不更新，返回之前的 VideoList
        if (!fromDB) {
            return myVideoList;
        }
        // 重新从读取数据库，获取 VideoList
        myVideoList = getVideoListFromSystem(null, order, usbPath);

        if (null == myVideoList) {
            return null;
        }

        return myVideoList;

    }

    /**
     * 功能：<获取包含图片的目录列表><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-20<br>
     * 修改时间：2016-5-20<br>
     * 版          本：V1.0<br>
     *
     * @param updateFolderList
     * @param fromDB
     * @return<br>
     */
    public List<FolderBean> getFolderList(boolean updateFolderList,
                                          boolean fromDB, String order, String path) {

        // 不更新, 返回之前的 FolderList
        if (null != myFolderList && !updateFolderList) {
            return myFolderList;
        }

        // 重新访问 VideoList, 更新 FolderList
        List<ImageBean> videoList = new ArrayList<ImageBean>();
        videoList = getVideoList(fromDB, order);

        if (null == videoList) {
            return null;
        }

        // 赋值 FolderList
        List<FolderBean> myList = new ArrayList<FolderBean>();
        Map<String, FolderBean> folderMap = new HashMap<String, FolderBean>();
        List<String> pathList = new ArrayList<String>();

        ImageBean vBean;
        FolderBean fBean;
        for (int i = 0; i < videoList.size(); i++) {
            vBean = videoList.get(i);
            String fPath = vBean.getVideoFolderPath();

            if (folderMap.containsKey(fPath)) {
                // 更新 number
                fBean = folderMap.get(fPath);
                fBean.imageNumber++;
            } else {
                // 添加路径到 Map
                fBean = new FolderBean();
                fBean.folderName = vBean.getVideoFolderName();
                fBean.folderPath = vBean.getVideoFolderPath();
                fBean.imageNumber = 1;
                fBean.folderUpdate = false;
                fBean.check = false;
                pathList.add(fPath);
                folderMap.put(fPath, fBean);
            }
        }

        if (folderMap.size() < 1) {
            return null;
        }
        for (int i = 0; i < pathList.size(); i++) {
            String key = pathList.get(i);
            fBean = folderMap.get(key);
            List<FolderBean> list = new FileUtils().getListDataUsb();
            for (FolderBean bean : list) {
//                Log.d("www","path2:"+fBean.getFolderPath());
                if (fBean.getFolderPath().contains(bean.getFolderPath())) {
//                    myList.add(fBean);
                    myList.add(fBean);
                }
            }

        }


        myFolderList = myList;
        Log.d("wmy", "folder list size:" + myList.size());
        return myList;

    }

    /**
     * 功能：<获取包含图片的USB列表><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-20<br>
     * 修改时间：2016-5-20<br>
     * 版          本：V1.0<br>
     *
     * @param updateFolderList
     * @param fromDB
     * @return<br>
     */
    public List<FolderBean> getUsbList(boolean updateFolderList,
                                       boolean fromDB, String order, String path) {

        // 不更新, 返回之前的 FolderList
        if (null != myFolderList && !updateFolderList) {
            return myFolderList;
        }

        // 重新访问 VideoList, 更新 FolderList
        List<ImageBean> videoList = new ArrayList<ImageBean>();
        videoList = getVideoList(fromDB, order);

        if (null == videoList) {
            return null;
        }

        // 赋值 FolderList
        List<FolderBean> usbList = new ArrayList<FolderBean>();
        Map<String, FolderBean> folderMap = new HashMap<String, FolderBean>();
        List<String> pathList = new ArrayList<String>();

        ImageBean vBean;
        FolderBean fBean;
        for (int i = 0; i < videoList.size(); i++) {
            vBean = videoList.get(i);
            String fPath = vBean.getVideoFolderPath();

            if (folderMap.containsKey(fPath)) {
                // 更新 number
                fBean = folderMap.get(fPath);
                fBean.imageNumber++;
            } else {
                // 添加路径到 Map
                fBean = new FolderBean();
                fBean.folderName = vBean.getVideoFolderName();
                fBean.folderPath = vBean.getVideoFolderPath();
                fBean.imageNumber = 1;
                fBean.folderUpdate = false;
                fBean.check = false;
                pathList.add(fPath);
                folderMap.put(fPath, fBean);
            }
        }

        if (folderMap.size() < 1) {
            return null;
        }
        for (int i = 0; i < pathList.size(); i++) {
            String key = pathList.get(i);
            fBean = folderMap.get(key);
            List<FolderBean> list = new FileUtils().getListDataUsb();
            for (FolderBean bean : list) {
//                Log.d("www","path2:"+fBean.getFolderPath());
                if (fBean.getFolderPath().contains(bean.getFolderPath())) {
//                    myList.add(fBean);
                    usbList.add(fBean);
                }
            }

        }


        myFolderList = usbList;
        return usbList;

    }

    /**
     * 功能：<返回目录下的文件列表><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-20<br>
     * 修改时间：2016-5-20<br>
     * 版          本：V1.0<br>
     *
     * @param folderPath
     * @param updateStreamList
     * @param fromDB
     * @return<br>
     */
    public List<FolderBean> getStreamList(String folderPath,
                                          boolean updateStreamList, boolean fromDB, String order) {

        // 不更新, 返回之前的 StreamList
        if (null != myStreamList && !updateStreamList) {
            return myStreamList;
        }

        // 重新访问 VideoList, 更新 StreamList
        List<ImageBean> videoList = new ArrayList<ImageBean>();
        videoList = getVideoList(fromDB, order);

        if (null == videoList) {
            return null;
        }

        // 赋值 StreamList
        List<FolderBean> myList = new ArrayList<FolderBean>();
        ImageBean vBean;
        FolderBean sBean;
        for (int i = 0; i < videoList.size(); i++) {
            vBean = videoList.get(i);
            String fPath = vBean.getVideoFolderPath();

            // 判断目录
            if (fPath.equals(folderPath)) {
                sBean = new FolderBean();
                sBean.imageId = vBean.getImageId();
                sBean.imageName = vBean.getImageNameNonType();
                sBean.imagePath = vBean.getImagePath();
                sBean.imageUpdate = false;
                myList.add(sBean);
            }
        }

        myStreamList = myList;

        return myList;

    }

    /**
     * 功能：<重命名文件夹名称><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-20<br>
     * 修改时间：2016-5-20<br>
     * 版          本：V1.0<br>
     *
     * @param oldFolderPath
     * @param newNamePath
     * @return<br>
     */
    public boolean renameFolderName(String oldFolderPath, String newNamePath) {
        Log.d("File Operations", "重命名文件夹");

        File file = new File(oldFolderPath);
        if (file.renameTo(new File(newNamePath))) {
            Log.d("File Operations", "新文件夹名称：" + newNamePath);
            return true;
        }

        return false;
    }

    /**
     * 功能：<重命名文件名称><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-20<br>
     * 修改时间：2016-5-20<br>
     * 版          本：V1.0<br>
     *
     * @param oldFilePath
     * @param newNamePath
     * @return<br>
     */
    public boolean renameFileName(String oldFilePath, String newNamePath) {
        Log.d("File Operations", "重命名文件" + oldFilePath);

        File file = new File(oldFilePath);
        if (file.renameTo(new File(newNamePath))) {
            Log.d("File Operations", "新文件名称：" + newNamePath);
            return true;
        }

        return false;
    }

    /**
     * 功能：<删除指定文件><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-20<br>
     * 修改时间：2016-5-20<br>
     * 版          本：V1.0<br>
     *
     * @param filePath
     * @return<br>
     */
    public boolean deleteFilePath(String filePath) {
        Log.d("File Operations", "删除文件" + filePath);

        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
                return true;
            }
        }
        return false;
    }

    /**
     * 功能：<删除文件列表><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-20<br>
     * 修改时间：2016-5-20<br>
     * 版          本：V1.0<br>
     *
     * @param fileList
     * @return<br>
     */
    public boolean deleteFilePathList(List<String> fileList) {
        Log.d("File Operations", "删除文件列表");
        List<String> delList = fileList;

        for (int i = 0; i < delList.size(); i++) {
            deleteFilePath(delList.get(i));
        }

        return true;
    }

    /**
     * @return the myFolderList
     */
    public List<FolderBean> getMyFolderList() {
        if (null == myFolderList) {
            myFolderList = new ArrayList<FolderBean>();
        }

        return myFolderList;
    }

    public List<FolderBean> getListDataUsb() {
        List<FolderBean> list = new ArrayList<>();
        String regex_usb = "sd[a-z]([0-9])*|udisk([0-9])*";
        File dir = null;
        File dir0 = new File("/storage");
        File dir1 = new File("/storage/external_storage");
        File dir2 = new File("/mnt/sdcard");
        if (dir0 != null && dir0.exists() && dir0.isDirectory()) {
            File[] files = dir0.listFiles(new MyFilenameFilter(regex_usb));
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.exists() && file.isDirectory()) {
                        FolderBean bean = new FolderBean();
                        bean.setFolderPath(file.getAbsolutePath());
                        bean.setFolderName(file.getName());
                        list.add(bean);

                    }
                }
            }
        }
        if (dir1 != null && dir1.exists() && dir1.isDirectory()) {
            dir = dir1;
        } else if (dir2 != null && dir2.exists() && dir2.isDirectory()) {
            dir = dir2;
        }


        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles(new MyFilenameFilter(regex_usb));
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.exists() && file.isDirectory()) {
                        FolderBean bean = new FolderBean();
                        bean.setFolderPath(file.getAbsolutePath());
                        bean.setFolderName(file.getName());
                        list.add(bean);
                        List<ImageBean> imageBeanList = getVideoList(true, "ASC");
                        int i = 0;
                        for (ImageBean imageBean : imageBeanList)
                            if (imageBean.getImagePath().contains(file.getAbsolutePath())) {
                                i++;
                            }
                        Log.d("HYP","i:"+i);
                        bean.setImageNumber(i);
                    }
                }
            }
        }

        return list;

    }

    private class MyFilenameFilter implements FilenameFilter {
        private Pattern p;

        public MyFilenameFilter(String regex) {
            p = Pattern.compile(regex);
        }

        public boolean accept(File file, String name) {
            return p.matcher(name).matches();
        }

    }
}

