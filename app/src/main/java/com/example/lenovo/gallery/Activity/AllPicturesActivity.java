package com.example.lenovo.gallery.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.percent.PercentRelativeLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.example.lenovo.gallery.R;
import com.example.lenovo.gallery.adapter.AllPicturesAdapter;
import com.example.lenovo.gallery.adapter.FolderAdapter;
import com.example.lenovo.gallery.adapter.UsbAdapter;
import com.example.lenovo.gallery.bean.FolderBean;
import com.example.lenovo.gallery.bean.ImageBean;
import com.example.lenovo.gallery.utils.MyView;
import com.example.lenovo.gallery.utils.USBDbo;
import com.example.lenovo.gallery.utils.folderDbo;
import com.example.lenovo.gallery.utils.imageDbo;
import com.example.lenovo.gallery.utils.myCollectionDbo;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;


public class AllPicturesActivity extends Activity implements ViewSwitcher.ViewFactory,
        View.OnClickListener {
    /**
     * set adapter
     */
    private static final int MSG_START_SET_ADAPTER = 1;
    /**
     * 收藏图片
     */
    private static final int MSG_COPY_FILE_FINISHED = 2;
    /**
     * set folder adapter
     */
    private static final int MSG_START_SET_FOLDER_ADAPTER = 3;
    /**
     * set usb adapter
     */
    private static final int MSG_START_SET_USB_ADAPTER = 4;
    /**
     * set usb image adapter
     */
    private static final int MSG_START_SET_USB_IMAGE_ADAPTER = 5;
    //data
    private ContentResolver resolver;
    /**
     * 所有图片的Adapter
     */
    private AllPicturesAdapter myImageAdapter;
    /**
     * 我的相册Adapter
     */
    private FolderAdapter myFolderAdapter;
    /**
     * 存储设备Adapter
     */
    private UsbAdapter myUsbAdapter;
    /**
     * gridview list
     */
    private List<ImageBean> imageList = new ArrayList<ImageBean>();
    /**
     * 所有图片list
     */
    private List<ImageBean> imageList1 = new ArrayList<ImageBean>();
    /**
     * 收藏list
     */
    private List<ImageBean> imageList2 = new ArrayList<ImageBean>();
    /**
     * 我的相册list
     */
    private List<FolderBean> folderList = new ArrayList<FolderBean>();
    /**
     * 我的相册中包含图片list
     */
    private List<FolderBean> folderImageList = new ArrayList<FolderBean>();
    /**
     * 存储设备中包含图片list
     */
    private List<FolderBean> usbImageList = new ArrayList<FolderBean>();
    /**
     * 所有图片database
     */
    private imageDbo myImageDB;
    /**
     * 我的相册database
     */
    private folderDbo myFolderDb;
    /**
     * 我的收藏database
     */
    private myCollectionDbo myCollectionDb;
    /**
     * 存储设备database
     */
    private USBDbo myUsbDb;
    private List<FolderBean> myUsbList = new ArrayList<FolderBean>();
    private Context mContext = null;
    private byte[] mContent = null;
    private int usbClickTag = -1;//置为1，表示已经进入设备子文件夹
    private String ffPath = null;
    /**
     * 判断是否为文件夹
     */
    private boolean isFolder = false;
    private int folderState = 0;//0:USB设备,1:folder,2:图片
    //view
    /**
     * gridview
     */
    private GridView myGridView;
    private Gallery myGallery;
    /**
     * 点击图片后，全屏显示图片的view
     */
    private ImageView myImageView;
    private RelativeLayout mainView = null;
    private boolean myImageViewShow = false;
    /**
     * 时间排序button
     */
    private Button titleBar;
    /**
     * 当前点击位置
     */
    private int currentItemFocus = 0;
    // image details
    private TextView imageName = null;
    private TextView imageDate = null;
    private TextView imageSizeWidth = null;
    private TextView imageSizeHeight = null;
    private TextView imageType = null;
    private String picType = null;
    private int picPosition = -1;
    private String picName = null;
    private String picDate = null;
    private String picPath = null;
    private String picDisplayName = null;
    private int picSizeWidth = 0;
    private int picSizeHeight = 0;
    private int picId = -1;
    //popup window
    /**
     * 图片操作界面
     */
    private PopupWindow popupWindow;
    /**
     * 图片详细信息
     */
    private Button pop_details;
    /**
     * 删除图片
     */
    private Button pop_delete;
    /**
     * 图片收藏
     */
    private Button collection;
    private PercentRelativeLayout text_pop_details;
    private Boolean popDetailsShow = false;
    //左侧选项卡
    /**
     * 左侧选项卡
     */
    private RelativeLayout leftBar = null;
    /**
     * 标记左侧选项卡位置flag
     */
    private boolean isLeftBar = true;
    /**
     * 区分选项卡tag
     */
    private int tag = 0;
    /**
     * 我的收藏
     */
    private Button btn_myCollection;
    /**
     * 所有图片
     */
    private Button btn_allImage;
    /**
     * 我的相册
     */
    private Button btn_folder;
    /**
     * 存储设备
     */
    private Button btn_myStorageDevice;
    private ImageView usbFolderImage;
    /**
     * 图片排序方式
     */
    private String order = "ASC";
    private Bitmap bitmapOrg;
    private MyView myViewRotate;
    //图片收藏
    /**
     * 新创建的图片收藏路径
     */
    private String newPath = null;
    private MediaScannerConnection mediaScanConn = null;
    private ImageSannerClient client = null;
    private File oldfile = null;
    private File newfile = null;
    private String fileType = null;
    //文件夹
    private String folderPath = null;
    private boolean getStreamImage = true;
    /**
     * usb路径
     */
    private String path = null;

    //usb监听
    private BroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_pictures);
        Logger
                .init("TCBSoftWare")               // default tag : PRETTYLOGGER or use just init()
                .methodCount(3)            // default 2
                .hideThreadInfo()             // default it is shown
                .logLevel(LogLevel.NONE);  // default : LogLevel.FULL
//        Logger.init("TCBSoftWare");
        Logger.d("tzyy", "hello 1");
//        Logger.e("tzyy", "hello");
//        Logger.w("tzyy", "hello");
//        Logger.v("tzyy", "hello");
//        Logger.wtf("tzyy", "hello");
//        Logger.d("hello %s", "world");
//        Logger.t("mytag").d("hello");
//        Logger.json(TELECOM_SERVICE);
        //监听usb设备
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                Log.d("+++++++++++", "action=" + intent.getAction());

                if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {
                    //插入usb设备
                    path = intent.getDataString();
                    initDB(order);
                    myHandler.sendEmptyMessage(MSG_START_SET_USB_ADAPTER);
                    Log.d("+++++++++++", "path=" + path);
                } else if (intent.getAction().equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
                    //拔出usb设备
                    path = null;
                    initDB(order);
                    myHandler.sendEmptyMessage(MSG_START_SET_USB_ADAPTER);
                }

//                Log.d("+++++++++++", "path=" + path);
                Toast.makeText(AllPicturesActivity.this, "action-----" + intent.getAction(), Toast.LENGTH_SHORT).show();
            }

        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_CHECKING);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addAction(Intent.ACTION_MEDIA_SHARED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addDataScheme("file");
        registerReceiver(receiver, filter);
        File Usbfile = new File("/proc/partitions");
        if (Usbfile.exists()) {
            try {
                FileReader file = new FileReader("/proc/partitions");
                BufferedReader br = new BufferedReader(file);
                String strLine = "";
                while ((strLine = br.readLine()) != null) {
                    if (strLine.indexOf("sd") > 0) {//已经挂载
                        break;
                    }
                }
                br.close();
                file.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        newPath = getSDPath();
        mContext = AllPicturesActivity.this;
        client = new ImageSannerClient();
        mediaScanConn = new MediaScannerConnection(this, client);
        initView();
        initDB(order);
        new Thread(getdataRunnable).start();
        initCtrl();
    }

    //创建我的收藏文件夹路径
    public String getSDPath() {
        File sdDir = null;
        sdDir = new File(Environment.getExternalStorageDirectory(), "TUIGalleryFolder");//创建新目录存放我的收藏图片
        Log.d("www", "外部存储路径：" + Environment.getExternalStorageDirectory());
        if (!sdDir.exists()) {
            sdDir.mkdir();
        }
        return sdDir.toString();

    }

    @Override
    public View makeView() {
        final ImageView i = new ImageView(this);
        i.setBackgroundColor(0xff000000);
        i.setScaleType(ImageView.ScaleType.CENTER_CROP);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(PercentRelativeLayout.LayoutParams.FILL_PARENT, PercentRelativeLayout.LayoutParams.FILL_PARENT));
        return i;
    }

    private void initDB(String order) {
        Logger.d("tzyy", "hello 2");
        myImageDB = new imageDbo(AllPicturesActivity.this);
        myCollectionDb = new myCollectionDbo(AllPicturesActivity.this);
        myUsbDb = new USBDbo(AllPicturesActivity.this);
        myFolderDb = new folderDbo(AllPicturesActivity.this);
        imageList1 = myImageDB.getImageList(true, order, newPath);
        imageList2 = myCollectionDb.getImageListFromSystem(order, order);
        imageList = imageList1;
    }


    private void initFolderDB() {
        myGridView.setAdapter(null);
        folderList = myFolderDb.getFolderList(false, true, order, newPath);
        setDataToCtrlFolder(folderList);
        myGridView.setOnItemClickListener(gvItemClickListener);
        myGridView.setSelection(0);
    }

    private List<String> usbDeviceList;

    private void initUSBDB() {
        myGridView.setAdapter(null);
        myUsbList = myUsbDb.getListDataUsb();
        usbDeviceList = myUsbDb.getListDataUsbString();
        setDataToCtrlUsb(myUsbList, true);
        myGridView.setOnItemClickListener(gvUSBItemClickListener);
        myGridView.setSelection(0);
    }

    private void initView() {
        titleBar = (Button) findViewById(R.id.titleBar);
        btn_myCollection = (Button) findViewById(R.id.btn_myCollection);
        btn_allImage = (Button) findViewById(R.id.btn_allPictures);
        btn_allImage.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar2));
        btn_folder = (Button) findViewById(R.id.btn_myAlbum);
        btn_myStorageDevice = (Button) findViewById(R.id.btn_myStorage);
        collection = (Button) findViewById(R.id.btn_collection);
        titleBar.setOnClickListener(this);
        leftBar = (RelativeLayout) findViewById(R.id.leftbar);
        mainView = (RelativeLayout) findViewById(R.id.mainView);
        myImageView = (ImageView) findViewById(R.id.imageView);
        myGridView = (GridView) findViewById(R.id.gridView);
        usbFolderImage = (ImageView) findViewById(R.id.view_folder_list);
    }


    private void initCtrl() {
        btn_myCollection.setOnClickListener(this);
        btn_allImage.setOnClickListener(this);
        btn_allImage.setSelected(true);
        btn_folder.setOnClickListener(this);
        btn_myStorageDevice.setOnClickListener(this);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                isLeftBar = false;
                leftBar.setVisibility(View.GONE);
                mainView.setVisibility(View.GONE);
                picPosition = position;
                currentItemFocus = position;
//                Log.d("wmy","position in AllPicturesActivity:"+currentItemFocus);
                ImageBean iBean = imageList.get(currentItemFocus);
                picName = iBean.getImageName();
                picDate = iBean.getImageDate();
                picSizeWidth = iBean.getImageWidth();
                picSizeHeight = iBean.getImageHeight();
                picPath = iBean.getImagePath();
                picType = iBean.getImageTypeSys();
                picId = iBean.getImageId();
                picDisplayName = iBean.getImageExpandName();
                myImageView.setImageURI(Uri.parse(picPath));
                myImageView.setVisibility(View.VISIBLE);
                myImageViewShow = true;
                Glide.with(AllPicturesActivity.this).

                        load(imageList.get(position).imagePath).crossFade().into(myImageView);
                myImageView.requestFocus();
                myImageView.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                            //左翻

                            if (picPosition > 0 && picPosition <= imageList.size() - 1) {
                                --picPosition;

                            } else if (picPosition == 0) {
                                picPosition = imageList.size() - 1;
                            }
                            picName = imageList.get(picPosition).getImageName();
                            picDisplayName = imageList.get(picPosition).getImageExpandName();

                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                            //右翻
                            if (picPosition >= 0 && picPosition < imageList.size() - 1) {
                                ++picPosition;
                            } else if (picPosition == imageList.size() - 1) {
                                picPosition = 0;
                            }
                            picName = imageList.get(picPosition).getImageName();
                            picDisplayName = imageList.get(picPosition).getImageExpandName();
                            File f = new File(newPath + "/" + picName + "." + picDisplayName);
                            if (f.exists()) {
                            } else {
                            }
                        }
                        ImageBean iBean = imageList.get(picPosition);
                        picPath = iBean.getImagePath();
                        myImageViewShow = true;

                        Glide.with(AllPicturesActivity.this).

                                load(picPath).crossFade().into(myImageView);
                        return false;
                    }
                });
                /**触摸响应  */
                myImageView.setOnTouchListener(new View.OnTouchListener() {
                    private float DownX = 0;
                    private float UpX = 0;

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        switch (event.getAction()) {

                            case MotionEvent.ACTION_DOWN:
                                DownX = event.getX();//float DownX
                                return true;
                            case MotionEvent.ACTION_UP:
                                UpX = event.getX();//float DownX
                                if (UpX < DownX) {
                                    if (picPosition > 0 && picPosition <= imageList.size() - 1) {
                                        --picPosition;
                                    } else if (picPosition == 0) {
                                        picPosition = imageList.size() - 1;
                                    }
                                } else if (DownX < UpX) {
                                    if (picPosition >= 0 && picPosition < imageList.size() - 1) {
                                        ++picPosition;
                                    } else if (picPosition == imageList.size() - 1) {
                                        picPosition = 0;
                                    }
                                }
                                ImageBean iBean = imageList.get(picPosition);
                                picPath = iBean.getImagePath();
                                myImageViewShow = true;
                                Glide.with(AllPicturesActivity.this).

                                        load(imageList.get(picPosition).imagePath).crossFade().into(myImageView);
                                bitmapOrg = iBean.getImage();
                                return false;
                            default:
                                return false;
                        }
                    }
                });
            }
        });
//
    }


    private void setDataToCtrl(List<ImageBean> List) {
        myImageAdapter = new AllPicturesAdapter(
                AllPicturesActivity.this, List);
        myImageAdapter.notifyDataSetChanged();
        myGridView.setAdapter(myImageAdapter);
    }

    /**
     * “我的相册”adapter
     */
    private void setDataToCtrlFolder(List<FolderBean> List) {

        myFolderAdapter = new FolderAdapter(AllPicturesActivity.this, List, isFolder);
        myFolderAdapter.notifyDataSetChanged();
        myGridView.setAdapter(myFolderAdapter);
    }

    /**
     * “USB”adapter
     */
    private void setDataToCtrlUsb(List<FolderBean> List, boolean flag) {
        myUsbAdapter = new UsbAdapter(AllPicturesActivity.this, List, isFolder);
        myUsbAdapter.setUSBDeviceFlag(flag);
        myGridView.setAdapter(myUsbAdapter);
    }

    Handler myHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START_SET_ADAPTER:
                    myGridView.setAdapter(null);
                    setDataToCtrl(imageList);
                    break;
                case MSG_COPY_FILE_FINISHED:
                    Toast.makeText(AllPicturesActivity.this, "已保存到“我的收藏”", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_START_SET_FOLDER_ADAPTER:
                    myGridView.setAdapter(null);
                    setDataToCtrlFolder(folderImageList);
                    myGridView.requestFocus();
                    break;
                case MSG_START_SET_USB_ADAPTER:
                    usbClickTag = 1;
                    myGridView.setAdapter(null);
                    isFolder = false;
                    setDataToCtrlUsb(myUsbList, false);
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.titleBar:
                //按时间排序

                if (!isFolder) {
                    if (order == "ASC") {
                        order = "DESC";

                        if (tag == 0) {
                            imageList = myImageDB.getImageList(true, order, newPath);
                            new Thread(getdataRunnable).start();
                        } else if (tag == 1) {
                            folderImageList = myFolderDb.getStreamList(folderPath, true, false, order, newPath);
                            new Thread(getfolderdataRunnable).start();
                        } else if (tag == 2) {
                            imageList = myCollectionDb.getImageList(true, order);
                            new Thread(getdataRunnable).start();
                        }

                        myGridView.setSelection(0);
                    } else if (order == "DESC") {
                        order = "ASC";
                        if (tag == 0) {
                            imageList = myImageDB.getImageList(true, order, newPath);
                            new Thread(getdataRunnable).start();
                        } else if (tag == 1) {
                            folderImageList = myFolderDb.getStreamList(folderPath, true, false, order, newPath);
                            new Thread(getfolderdataRunnable).start();
                        } else if (tag == 2) {
                            imageList = myCollectionDb.getImageList(true, order);
                            new Thread(getdataRunnable).start();
                        }
                        myGridView.setSelection(0);
                    }


                } else {
                    Log.d("www", "folder order!!! " + order);
                    if (order == "ASC") {
                        order = "DESC";
                        folderList = myFolderDb.getFolderList(true, true, order, newPath);
                        myGridView.setOnItemClickListener(gvItemClickListener);
                        myGridView.setSelection(0);
                    } else if (order == "DESC") {
                        order = "ASC";
                        folderList = myFolderDb.getFolderList(true, true, order, newPath);
                        myGridView.setOnItemClickListener(gvItemClickListener);
                        myGridView.setSelection(0);
                    }
                }
                break;

            case R.id.btn_allPictures:
                tag = 0;
                btn_folder.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                btn_myCollection.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                btn_myStorageDevice.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                btn_allImage.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar2));
                isLeftBar = true;
                isFolder = false;
                myGridView.setAdapter(null);
                initDB(order);
                setDataToCtrl(imageList1);
                initCtrl();
                myGridView.setSelection(0);
//                initCtrl();
                break;
            case R.id.btn_myAlbum:
                tag = 1;
                btn_folder.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar2));
                btn_myCollection.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                btn_myStorageDevice.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                btn_allImage.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                isLeftBar = true;
                isFolder = true;
                initFolderDB();
                myGridView.setSelection(0);
                break;
            case R.id.btn_myCollection:
                tag = 2;
                btn_folder.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                btn_myCollection.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar2));
                btn_myStorageDevice.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                btn_allImage.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                isLeftBar = true;
                isFolder = false;
//                initDB(order);
                imageList2 = myCollectionDb.getImageListFromSystem(order, order);
                imageList = imageList2;
                myGridView.setAdapter(null);
                setDataToCtrl(imageList);
                initCtrl();
                break;
            case R.id.btn_myStorage:
                tag = 3;
                btn_folder.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                btn_myCollection.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                btn_myStorageDevice.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar2));
                btn_allImage.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                isLeftBar = true;
                isFolder = true;
                initUSBDB();
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * 我的相册gridview
     */
    private AdapterView.OnItemClickListener gvItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View arg1,
                                int position, long arg3) {
            if (isFolder) {

                FolderBean fBean = folderList.get(position);
                isLeftBar = false;
                isFolder = false;
                folderPath = fBean.getFolderPath();
                myGridView.setAdapter(null);
                new Thread(getfolderdataRunnable).start();
            } else {
                isLeftBar = false;

                FolderBean SBean = folderImageList.get(position);
                picPath = SBean.getImagePath();
                currentItemFocus = position;

                picName = SBean.getImageName();
                picDate = SBean.getImageDate();
                picSizeWidth = SBean.getImageWidth();
                picSizeHeight = SBean.getImageHeight();
                picType = SBean.getImageTypeSys();
                picId = SBean.getImageId();
                picDisplayName = SBean.getImageExpandName();
                myImageView.setVisibility(View.VISIBLE);
                leftBar.setVisibility(View.INVISIBLE);
                mainView.setVisibility(View.INVISIBLE);
                myImageViewShow = true;
                Glide.with(AllPicturesActivity.this).

                        load(picPath).into(myImageView);
                myImageView.requestFocus();
                myImageView.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                            if (currentItemFocus > 0 && currentItemFocus <= folderImageList.size() - 1) {
                                --currentItemFocus;
                            } else if (currentItemFocus == 0) {
                                currentItemFocus = folderImageList.size() - 1;
                            }
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                            if (currentItemFocus >= 0 && currentItemFocus < folderImageList.size() - 1) {
                                ++currentItemFocus;
                            } else if (currentItemFocus == folderImageList.size() - 1) {
                                currentItemFocus = 0;
                            }
                        }
                        FolderBean iBean = folderImageList.get(currentItemFocus);
                        picPath = iBean.getImagePath();
                        myImageViewShow = true;

                        Glide.with(AllPicturesActivity.this).

                                load(folderImageList.get(currentItemFocus).imagePath).into(myImageView);

                        return false;

                    }

                });
                myImageView.setOnTouchListener(new View.OnTouchListener() {
                    private float DownX = 0;
                    private float UpX = 0;

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        switch (event.getAction()) {

                            case MotionEvent.ACTION_DOWN:
                                DownX = event.getX();//float DownX
                                return true;
                            case MotionEvent.ACTION_UP:
                                UpX = event.getX();//float DownX
                                if (UpX < DownX) {
                                    if (currentItemFocus > 0 && currentItemFocus <= folderImageList.size() - 1) {
                                        --currentItemFocus;
                                    } else if (currentItemFocus == 0) {
                                        currentItemFocus = folderImageList.size() - 1;
                                    }
                                } else if (DownX < UpX) {
                                    if (currentItemFocus >= 0 && currentItemFocus < folderImageList.size() - 1) {
                                        ++currentItemFocus;
                                    } else if (currentItemFocus == folderImageList.size() - 1) {
                                        currentItemFocus = 0;
                                    }
                                }
                                FolderBean iBean = folderImageList.get(currentItemFocus);
                                picPath = iBean.getImagePath();
                                myImageViewShow = true;
                                Glide.with(AllPicturesActivity.this).

                                        load(folderImageList.get(currentItemFocus).imagePath).crossFade().into(myImageView);
                                bitmapOrg = iBean.getImage();
                                return false;
                            default:
                                return false;
                        }
                    }
                });


            }
        }
    };
    /**
     * usb点击响应
     */
    private AdapterView.OnItemClickListener gvUSBItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View arg1,
                                int position, long arg3) {
            FolderBean fBean = myUsbList.get(position);

            folderPath = fBean.getFolderPath();
//        }

            File file = new File(folderPath);

            if (file.exists())
                isFolder = file.isDirectory();

            if (isFolder) {
                ffPath = folderPath;
//            ffPath=file.getParentFile().getAbsolutePath();//获取上一级路径
                Log.d("www", "上一级路径ffPath111：" + ffPath);
                myGridView.setAdapter(null);
                new Thread(getUSBfolderdataRunnable).start();
                isLeftBar = false;
            } else {
                ffPath = file.getParentFile().getAbsolutePath();
                Log.d("www", "上一级路径ffPath222：" + ffPath);
                usbClickTag = -1;
                isLeftBar = false;

                long[] playIdList = new long[myUsbList.size()];

                for (int i = 0; i < myUsbList.size(); i++) {
                    File file1 = new File(myUsbList.get(i).imagePath);
                    if (file1.exists())
                        isFolder = file.isDirectory();
                    if (!isFolder) {
                    }
                    playIdList[i] = myUsbList.get(i).getImageId();
                }
                FolderBean SBean = myUsbList.get(position);


                picPath = SBean.getImagePath();
                File file1 = new File(picPath);
                if (file1.exists())
                    isFolder = file1.isDirectory();
                currentItemFocus = position;

                picName = SBean.getImageName();
                picDate = SBean.getImageDate();
                picSizeWidth = SBean.getImageWidth();
                picSizeHeight = SBean.getImageHeight();
                picType = SBean.getImageTypeSys();
                picId = SBean.getImageId();
                picDisplayName = SBean.getImageExpandName();
                myImageView.setImageURI(Uri.parse(picPath));
                myImageView.setVisibility(View.VISIBLE);
                leftBar.setVisibility(View.INVISIBLE);
                mainView.setVisibility(View.INVISIBLE);
                myImageViewShow = true;
                Glide.with(AllPicturesActivity.this).
                        load(myUsbList.get(position).imagePath).into(myImageView);
                myImageView.requestFocus();

                myImageView.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                            if (currentItemFocus > 0 && currentItemFocus <= myUsbList.size() - 1) {
                                --currentItemFocus;
                            } else if (currentItemFocus == 0) {
                                currentItemFocus = myUsbList.size() - 1;
                            }
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                            if (currentItemFocus >= 0 && currentItemFocus < myUsbList.size() - 1) {
                                ++currentItemFocus;
                            } else if (currentItemFocus == myUsbList.size() - 1) {
                                currentItemFocus = 0;
                            }
                        }
                        FolderBean iBean = myUsbList.get(currentItemFocus);
                        picPath = iBean.getImagePath();
                        myImageViewShow = true;

                        Glide.with(AllPicturesActivity.this).

                                load(myUsbList.get(currentItemFocus).imagePath).into(myImageView);
                        return false;
                    }

                });
                myImageView.setOnTouchListener(new View.OnTouchListener() {
                    private float DownX = 0;
                    private float UpX = 0;

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
//                        Log.d("fff", "action:" + event.getAction());
                        switch (event.getAction()) {

                            case MotionEvent.ACTION_DOWN:
                                DownX = event.getX();//float DownX
                                return true;
                            case MotionEvent.ACTION_UP:
                                UpX = event.getX();//float DownX
                                if (UpX < DownX) {
                                    if (currentItemFocus > 0 && currentItemFocus <= myUsbList.size() - 1) {
                                        --currentItemFocus;
                                    } else if (currentItemFocus == 0) {
                                        currentItemFocus = myUsbList.size() - 1;
                                    }
                                } else if (DownX < UpX) {
                                    if (currentItemFocus >= 0 && currentItemFocus < myUsbList.size() - 1) {
                                        ++currentItemFocus;
                                    } else if (currentItemFocus == myUsbList.size() - 1) {
                                        currentItemFocus = 0;
                                    }
                                }
                                FolderBean iBean = myUsbList.get(currentItemFocus);
                                picPath = iBean.getImagePath();
                                myImageViewShow = true;
                                Glide.with(AllPicturesActivity.this).

                                        load(myUsbList.get(currentItemFocus).imagePath).crossFade().into(myImageView);
                                bitmapOrg = iBean.getImage();
                                return false;
                            default:
                                return false;
                        }
                    }
                });

                bitmapOrg = SBean.getImage();

            }
        }
    };

    private void showPopupWindow1(final String picName) {

        isLeftBar = false;

        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.popu_window1, null);
        PercentRelativeLayout pop_title = (PercentRelativeLayout) contentView.findViewById(R.id.text_pic_information);
        imageName = (TextView) contentView.findViewById(R.id.text_pic_nameContent);
        imageDate = (TextView) contentView.findViewById(R.id.text_pic_dateContent);
        imageSizeWidth = (TextView) contentView.findViewById(R.id.text_pic_sizeContent);
        imageSizeHeight = (TextView) contentView.findViewById(R.id.text_pic_sizeContent);
        imageType = (TextView) contentView.findViewById(R.id.text_pic_typeContent);
        text_pop_details = (PercentRelativeLayout) contentView.findViewById(R.id.text_pic_details);
        text_pop_details.setVisibility(View.GONE);
        pop_details = (Button) contentView.findViewById(R.id.btn_details);
        pop_delete = (Button) contentView.findViewById(R.id.btn_delete);
        collection = (Button) contentView.findViewById(R.id.btn_collection);

        if (tag == 2) {
            collection.setBackgroundColor(getResources().getColor(R.color.Gray));
            collection.setText("");
            pop_delete.setFocusable(true);
            pop_delete.requestFocus();
        } else {
            /**
             * 判斷文件是否被收藏
             */
            Boolean fIsExist = false;
            if (imageList2 != null) {
                for (int i = 0; i < imageList2.size(); i++) {
                    Log.d("www", "f.exists    " + getFileMD5(picPath));
                    Log.d("www", "f.exists!!!!" + getFileMD5(imageList2.get(i).getImagePath()));
                    if (getFileMD5(picPath).equals(getFileMD5(imageList2.get(i).getImagePath()))) {
                        fIsExist = true;
                        Log.d("www", "fIsExist    ");
                        continue;
                    }
                }
            }
            if (fIsExist) {
                collection.setText("已收藏");
            } else {

                collection.setText("收藏");
            }
        }
        imageName.setText(picName);
        imageDate.setText(picDate);
        imageType.setText(picType);
        imageSizeWidth.setText("" + picSizeWidth + " " + picSizeHeight);
//实例化popup窗口
        popupWindow = new PopupWindow(contentView, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.color.Transparent));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(findViewById(R.id.parentLayout), Gravity.NO_GRAVITY, 0, 0);
        pop_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popDetailsShow == false) {
                    text_pop_details.setVisibility(View.VISIBLE);
                    popDetailsShow = true;
                } else {
                    text_pop_details.setVisibility(View.GONE);
                    popDetailsShow = false;
                }
            }
        });

        pop_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_delete();
            }
        });

        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = collection.getText().toString();
                String p = newPath + "/" + picName + "." + picDisplayName;
                if (n.equals("已收藏")) {
                    deletePictures(p);
                    collection.setText("收藏");
                } else {
                    Log.d("www", "coll " + picPath);
                    Log.d("www", "collect   " + newPath);
                    copyFile(picPath, newPath);
                    collection.setText("已收藏");
                }

//
            }
        });

    }

    /**
     * 删除图片
     */
    private void dialog_delete() {
        //设置监听
        DialogInterface.OnClickListener dialogOnclickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        deletePictures(picPath);
                        popupWindow.dismiss();
                        myImageView.setVisibility(View.GONE);

                        leftBar.setVisibility(View.VISIBLE);
                        mainView.setVisibility(View.VISIBLE);
                        myImageViewShow = false;
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //得到构造器
        builder.setTitle("提示");
        builder.setMessage("是否删除图片");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("确认", dialogOnclickListener);
        builder.setNegativeButton("取消", dialogOnclickListener);
        builder.create().show();
    }


    /**
     * 删除数据库条目
     */
    public void deletePictures(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
            ContentResolver resolver = getContentResolver();
            resolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{path});
            initDB(order);
            if (tag == 0) {
                imageList = imageList1;
            } else if (tag == 2) {
                imageList = imageList2;
            }
            new Thread(getdataRunnable).start();
            initCtrl();
        } else {
            Toast.makeText(AllPicturesActivity.this, "文件不存在",
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            String newPath2 = null;
            oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
//                picDisplayName="jpg";
                File f = new File(newPath + "/" + picName + "." + picDisplayName);
                Log.d("www", "www !f.exists() " + !f.exists());
                Log.d("www", "www picName " + picName);
                Log.d("www", "www picDisplayName " + picDisplayName);
                if (!f.exists() || picName == null) {

                    newPath2 = newPath + "/" + picName + "." + picDisplayName;
                    FileOutputStream fs = new FileOutputStream(newPath2);
                    byte[] buffer = new byte[512];
                    while ((byteread = inStream.read(buffer)) != -1) {
                        bytesum += byteread; //字节数 文件大小
                        System.out.println(bytesum);
                        fs.write(buffer, 0, byteread);

                    }
                    fs.close();
                    inStream.close();
                    myHandler.sendEmptyMessage(MSG_COPY_FILE_FINISHED);
                } else {
                    Toast.makeText(AllPicturesActivity.this, "!!!", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }
        scanfile(new File(newPath));
    }

    /**
     * 获取数据及缩略图
     */
    Runnable getdataRunnable = new Runnable() {

        @Override
        public void run() {
            int id = -1;
            String imageName = null;
//            Log.d("getdata", " imageList:" + imageList);
            if (imageList != null) {

                int length = imageList.size();
                int i = 0;

//                Log.d("wmy", "  length :" +  length );
                Log.d("wmy", "  length :" + length);
                while (i < length) {
                    ImageBean imageBean = imageList.get(i);
                    Log.d("wmy", "  ImageBean :" + imageBean.toString());
                    if (imageBean != null) {
                        id = imageBean.getImageId();
                        Log.d("wmy", "  id :" + id);
                        Log.d("wmy", "  i :" + i);
                        Log.d("wmy", "  myImageDB :" + myImageDB);
                        Bitmap bitmap = myImageDB.getImage(id);
                        Log.d("wmy", "  bitmap :" + bitmap);
                        if (bitmap != null)
                            imageList.get(i).setImage(bitmap);
                    }
                    i++;
                }

            }
            myHandler.sendEmptyMessage(MSG_START_SET_ADAPTER);

        }
    };
    /**
     * 获取文件夹下数据及缩略图
     */
    Runnable getfolderdataRunnable = new Runnable() {

        @Override
        public void run() {
            folderImageList = myFolderDb.getStreamList(folderPath, true, false, order, newPath);
            int id = -1;
            int length = folderImageList.size();
            int i = 0;
            getStreamImage = true;
            while (i < length) {

                id = folderImageList.get(i).getImageId();
                Bitmap bitmap = myImageDB.getImage(id);
                folderImageList.get(i).setImage(bitmap);

                i++;

            }
            myHandler.sendEmptyMessage(MSG_START_SET_FOLDER_ADAPTER);
        }
    };
    /**
     * 存储设备子线程
     */
    Runnable getUSBfolderdataRunnable = new Runnable() {

        @Override
        public void run() {
            myUsbList = myUsbDb.getFolderList(true, true, order, folderPath);
            int id = -1;
            int length = myUsbList.size();
            getStreamImage = true;
            for (int i = 0; i < length; i++) {
                id = myUsbList.get(i).getImageId();
                folderPath = myUsbList.get(i).getImagePath();
                Bitmap bitmap = myImageDB.getImage(id);
                myUsbList.get(i).setImage(bitmap);
            }
            myHandler.sendEmptyMessage(MSG_START_SET_USB_ADAPTER);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (myImageViewShow) {
                myImageView.setVisibility(View.GONE);
                leftBar.setVisibility(View.VISIBLE);
                mainView.setVisibility(View.VISIBLE);
                myGridView.requestFocus();
                myImageViewShow = false;
            } else if (!isFolder && tag == 1) {
                isFolder = true;
                initFolderDB();
                myGridView.requestFocus();
            } else if (tag == 3) {
                File file = new File(ffPath);
                ffPath = file.getParentFile().getAbsolutePath();//获取上一级路径
                isFolder = true;
                myGridView.setAdapter(null);

                myUsbList = myUsbDb.getFolderList(true, true, order, ffPath);
                if (usbDeviceList.contains(ffPath))
                    setDataToCtrlUsb(myUsbList, false);
                else
                    setDataToCtrlUsb(myUsbList, true);
                myGridView.setOnItemClickListener(gvUSBItemClickListener);
                myGridView.setSelection(0);
                myGridView.requestFocus();
            } else {
                finish();
            }
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (myImageViewShow) {
                showPopupWindow1(picName);
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

            if (isLeftBar) {
                isLeftBar = false;
                switch (tag) {
                    case 0:
                        btn_folder.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_myCollection.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_myStorageDevice.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_allImage.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar2));
                        break;
                    case 1:
                        btn_folder.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar2));
                        btn_myCollection.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_myStorageDevice.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_allImage.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        break;
                    case 2:
                        btn_folder.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_myCollection.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar2));
                        btn_myStorageDevice.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_allImage.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        break;
                    case 3:
                        btn_folder.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_myCollection.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_myStorageDevice.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar2));
                        btn_allImage.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        break;
                    default:
                        break;
                }

            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (!isLeftBar) {
                isLeftBar = true;
                switch (tag) {
                    case 0:
                        btn_folder.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_myCollection.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_myStorageDevice.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_allImage.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar2));
                        break;
                    case 1:
                        btn_folder.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar2));
                        btn_myCollection.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_myStorageDevice.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_allImage.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        break;
                    case 2:
                        btn_folder.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_myCollection.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar2));
                        btn_myStorageDevice.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_allImage.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        break;
                    case 3:
                        btn_folder.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_myCollection.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        btn_myStorageDevice.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar2));
                        btn_allImage.setBackground(getResources().getDrawable(R.drawable.selector_btn_leftbar));
                        break;
                    default:
                        break;
                }
            }

        }
        return false;
    }


    /**
     * 刷新数据库文件
     */
    class ImageSannerClient implements
            MediaScannerConnection.MediaScannerConnectionClient {

        public void onMediaScannerConnected() {
            Log.d("---------", "media service connected");
            Log.d("---------", "" + newfile);
            if (newfile != null) {

                if (newfile.isDirectory()) {
                    File[] files = newfile.listFiles();
                    Log.d("---------", "files11111");
                    if (files != null) {
                        for (int i = 0; i < files.length; i++) {
                            if (files[i].isDirectory()) {
                                Log.d("---------", "files：" + files[i].getAbsolutePath());
                            } else {
                                Log.d("---------", "else");
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE), files[i].getAbsolutePath());
                                mediaScanConn.scanFile(
                                        files[i].getAbsolutePath(), fileType);
                            }
                        }
                    }
                }
            }

            newfile = null;

            fileType = null;

        }

        public void onScanCompleted(String path, Uri uri) {
            // TODO Auto-generated method stub
            mediaScanConn.disconnect();
        }

    }

    private void scanfile(File f) {
        this.newfile = f;
        mediaScanConn.connect();
    }

    /**
     * 获取单个文件的MD5值
     *
     * @param path
     * @return
     */

    public static String getFileMD5(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

}