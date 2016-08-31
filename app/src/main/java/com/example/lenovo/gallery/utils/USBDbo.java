package com.example.lenovo.gallery.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.Log;

import com.example.lenovo.gallery.bean.FolderBean;
import com.example.lenovo.gallery.bean.ImageBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class USBDbo {

    private Context context;
    private String usbPath;
    private String usbPathString = null;
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

    private ImageBean myImageBean = null;
    private FolderBean myFolderBean = null;

    private static final String MOUNTS_FILE = "/proc/mounts";

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
            String imagePath = cursor.getString(cursor
                    .getColumnIndexOrThrow(Images.Media.DATA));

            vBean.imagePath = imagePath;

			/* 验证文件有效 */
            File file = new File(imagePath);
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
                myImageBean = vBean;
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
     * 功能：<获取某个图片的缩略图><br>
     * 编码作者：WangMengyao<br>
     * 创建时间：2016-5-20<br>
     * 修改时间：2016-5-20<br>
     * 版          本：V1.0<br>
     *
     * @param id
     * @return<br>
     */

    public Bitmap getImage(int id) {
        System.out.println("---dddddddd-----");
        myVideoList = getImageListFromSystem(null, "ASC");
        int index = -1;
        ImageBean imageBean = null;
        for (int i = 0; i < myVideoList.size(); i++) {
            imageBean = myVideoList.get(i);
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

        myVideoList.get(index).setImage(myImage);
        return myImage;

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
            videoImage = getImage(id);

            newVideoList.get(i).setImage(videoImage);
        }

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
    private List<ImageBean> getImageList(boolean fromDB, String order) {

        // 不更新，返回之前的ImageList
        if (!fromDB) {
            return myVideoList;
        }
        // 重新从读取数据库，获取 ImageList
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
        videoList = getImageByPath(path);
        Log.d("wmy", "videoList!!!:" + videoList.size());

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
            // 图片路径
            String fPath = vBean.getVideoFolderPath();

            if (fPath.equals(path)) {
                fBean = new FolderBean();
                fBean.imageId = vBean.getImageId();
                fBean.imageImage = getImage(fBean.imageId);
                fBean.imagePath = vBean.getImagePath();
                fBean.folderName = vBean.getImageName();
                fBean.folderPath = vBean.getImagePath();
//                Log.d("wmy", "fBean3!!!" + fBean.folderPath);
                myList.add(fBean);
//                continue;
            }

            String regex = path + "/[^/]*";

            if (folderMap.containsKey(fPath)) {
                // 更新 number
                fBean = folderMap.get(fPath);
                fBean.imageNumber++;
//                Log.d("www", "!!!" + fBean.folderPath);
            } else if (fPath.matches(regex)) {
//                Log.d("www","mathce:"+fPath.matches(regex));
                // 添加路径到 Map
                fBean = new FolderBean();
                fBean.folderName = vBean.getVideoFolderName();
                fBean.folderPath = vBean.getVideoFolderPath();
                fBean.imagePath = vBean.getImagePath();
                fBean.imageNumber = 1;
                fBean.folderUpdate = false;
                fBean.check = false;
                pathList.add(fPath);
                folderMap.put(fPath, fBean);
//                Log.d("www", "```!!!" + fBean.folderPath);

            } else {
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(fPath);
                while (m.find()) {
                    String key = m.group();
                    if (folderMap.containsKey(key)) {
                        fBean = folderMap.get(key);
                        fBean.imageNumber++;
                    }

                }

            }


        }

        if (folderMap.size() < 1) {
            return myList;

        }
        for (int i = 0; i < pathList.size(); i++) {
            String key = pathList.get(i);
            fBean = folderMap.get(key);
            List<FolderBean> list = new FileUtils().getListDataUsb();
            for (FolderBean bean : list) {
                if (fBean.getFolderPath().contains(bean.getFolderPath())) {
                    myList.add(fBean);
                }
            }
        }

        myFolderList = myList;
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
        videoList = getImageList(fromDB, order);

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
        videoList = getImageList(fromDB, order);

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


    public List<String> isMounted(String[] path) {
        List<String> pathList = new ArrayList<String>();
        boolean blnRet = false;
        String strLine = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(MOUNTS_FILE));

            while ((strLine = reader.readLine()) != null) {
                for (int i = 0; i < path.length; i++) {
                    String p = path[i];
                    if (strLine.contains(p)) {
                        String[] temp = TextUtils.split(strLine, " ");
                        Log.d("www", "strLine:   " + strLine);
//                //分析内容可看出第二个空格后面是路径
                        String result = temp[1];
                        if (result.contains("udisk") || result.contains("sdcard")|| result.contains("sda")) {
                            Log.d("www", "result:   " + result);
                            pathList.add(result);
//                        break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            pathList.add(Environment.getExternalStorageDirectory().getAbsolutePath());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reader = null;
            }
        }
        return pathList;
    }
//    /**
//     * 查询ext_volumes表中USB挂载信息
//     *
//     * @return 路径list
//     */
//    public ArrayList<HashMap<String, Object>> getUsbInfo() {
//        final String TABLENAME = "files";
//        String columnName = "_data";
//        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
////        String sql = "SELECT * FROM " + TABLENAME;
////        Cursor result = db.rawQuery(sql, null); // 执行查询语句
//        ContentResolver resolver = context.getContentResolver();
//        Cursor cursor;
//        cursor = resolver.query(
//                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
//                null,  FilesImages.Media.DATE_ADDED);
//        Log.d("www","**cursor**   "+cursor);
//        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put(columnName, cursor.getString(cursor.getColumnIndex(columnName)));
//            list.add(map);
//        }
//        return list;
//
//    }

    //
    public List<FolderBean> getListDataUsb() {
        List<FolderBean> list = new ArrayList<>();

        String path[] = {"/storage", "/storage/external_storage", "/mnt/sdcard"};
        List<String> pathList2 = null;
        pathList2 = isMounted(path);
        FolderBean bean = new FolderBean();
        for (int i = 0; i < pathList2.size(); i++) {
            File file = new File(pathList2.get(i).toString());
            bean.setFolderPath(pathList2.get(i).toString());
            bean.setFolderName(file.getName());
            list.add(bean);

        File files= new File(pathList2.get(i).toString());
        List<ImageBean> imageBeanList = getImageList(true, "ASC");
//                    int i = 0;
        for (ImageBean imageBean : imageBeanList)
                        if (imageBean.getImagePath().contains(files.getAbsolutePath())) {
                            i++;
                        }
                    bean.setImageNumber(i);  }
        return list;
    }

    public List<String> getListDataUsbString() {
        List<String> list = new ArrayList<>();
        String regex_usb = "sd[a-z]([0-9])*|udisk([0-9])*";
        File dir = null;
        File dir0 = new File("/storage/");
        File dir1 = new File("storage/external_storage");
        File dir2 = new File("/mnt/sdcard");
        if (dir0 != null && dir0.exists() && dir0.isDirectory()) {
            File[] files = dir0.listFiles(new MyFilenameFilter(regex_usb));
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.exists() && file.isDirectory()) {
                        list.add(file.getAbsolutePath());

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
                        list.add(file.getAbsolutePath());
                    }
                }
            }
        }

        return list;

    }

    private List<ImageBean> getImageByPath(String path) {
        List<ImageBean> imageBeanList = getImageList(true, "ASC");
        List<ImageBean> imageListByPath = new ArrayList<ImageBean>();
//        int i = 0;
        for (ImageBean imageBean : imageBeanList)
            if (imageBean.getImagePath().contains(path)) {
                imageListByPath.add(imageBean);

            }
        return imageListByPath;
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

