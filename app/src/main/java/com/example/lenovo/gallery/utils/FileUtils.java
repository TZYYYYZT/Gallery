package com.example.lenovo.gallery.utils;

import android.content.Context;

import com.example.lenovo.gallery.bean.FolderBean;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FileUtils {
    private final static String TAG = "Upgrade.FileUtils";
    // public final static String SD_PARH = "/storage/sdcard0";
    // public final static String INTERNAL_MEMORY_PATH = "/mnt/flash";
    public final static String TV_MEDIA_SDCARD_PATH = "/mnt/media/external_sdcard/";
    public final static String FW_NAME = "update.zip";
    public final static String FW_BACKUP_NAME = "update_backup.zip";
    public final static String SAVE_DIR_NAME = "OLD FIRMWARE";
    private static final int BUFFER_SIZE = 16 * 1024;
    private File saveFile;


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
                    }
                }
            }
        }

        return list;

    }


    public List<Map<String, Object>> getListDataUsb(Context context) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;


        File dir = new File("/storage/external_storage");
        String regex = ".+\\.[jJ][pP][gG]";
        String regex_usb = "sd[a-z]([0-9])*";
        String regex_usb2 = "sd[a-z]([0-9])*";

        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles(new MyFilenameFilter(regex_usb));
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.exists() && file.isDirectory()) {
                        File[] files4 = file.listFiles(new MyFilenameFilter(regex));
                        if (files4 != null && files4.length > 0) {
                            for (File file4 : files4) {
                                map = new HashMap<String, Object>();
                                map.put("item_name", file4.getName());
                                map.put("item_path", file4.getAbsolutePath());
                                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm")
                                        .format(new Date(file4.lastModified()));
                                map.put("item_date", date);
                                list.add(map);
                            }
                        }

                        File[] files2 = file.listFiles(new MyFilenameFilter(regex_usb2));
                        if (files2 != null && files2.length > 0) {
                            for (File file2 : files2) {
                                if (file2.exists() && file2.isDirectory()) {
                                    File[] files3 = file2.listFiles(new MyFilenameFilter(regex));
                                    if (files3 != null && files3.length > 0) {
                                        for (File file3 : files3) {
                                            map = new HashMap<String, Object>();
                                            map.put("item_name", file3.getName());
                                            map.put("item_path", file3.getAbsolutePath());

                                            String date2 = new SimpleDateFormat("yyyy/MM/dd HH:mm")
                                                    .format(new Date(file3.lastModified()));
                                            map.put("item_date", date2);
                                            list.add(map);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return list;

    }


    public List<Map<String, Object>> getListDataSata(Context context) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;

        File dir = new File("/mnt/sata");
        String regex = ".+\\.[zZ][iI][pP]";
        String regex_sata = "sd[a-z]([0-9])*";

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles(new MyFilenameFilter(regex_sata));
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.exists() && file.isDirectory()) {
                        File[] files2 = file.listFiles(new MyFilenameFilter(regex));
                        if (files2 != null && files2.length > 0) {
                            for (File file2 : files2) {
                                map = new HashMap<String, Object>();
                                map.put("item_name", file2.getName());
                                map.put("item_path", file2.getAbsolutePath());

                                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm")
                                        .format(new Date(file2.lastModified()));
                                map.put("item_date", date);

                                list.add(map);
                            }
                        }
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

    public boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }


    /**
     * 功能：判断路径可读<br>
     * 编码作者：LiuQiang<br>
     * 创建时间：2015-6-26<br>
     * 修改时间：2015-6-26<br>
     * 版 本：V1.0<br>
     *
     * @param path
     * @return<br>
     */
    public static boolean pathCanRead(String path) {
        boolean b = false;
        if (path == null) {
            return b;
        }
        File file = new File(path);
        b = file.canRead();
        file = null;
        return b;
    }

    /**
     * 功能：判断路径可写<br>
     * 编码作者：LiuQiang<br>
     * 创建时间：2015-6-26<br>
     * 修改时间：2015-6-26<br>
     * 版 本：V1.0<br>
     *
     * @param path
     * @return<br>
     */
    public static boolean pathCanWrite(String path) {
        boolean b = false;
        if (path == null) {
            return b;
        }
        File file = new File(path);
        b = file.canWrite();
        file = null;
        return b;
    }
}
