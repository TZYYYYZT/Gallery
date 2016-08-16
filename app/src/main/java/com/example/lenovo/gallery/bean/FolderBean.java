package com.example.lenovo.gallery.bean;

import android.graphics.Bitmap;

public class FolderBean {
    public String imageIcon;
    public String name  = "";
    public String description = "";

    public boolean isFolder() {
        return isFolder;
    }

    public boolean isFolder;

    public String folderName;
    public String folderPath;
    public int imageNumber;
    public boolean folderUpdate;

    public boolean check = false;
    /**
     * 节目日期
     */
    public String imageDate;
    public int imageId;
    public String imageName; /* 节目名称 */
    public String imagePath; /* 节目路径 */
    public String imageTime; /* 节目时长 */
    public boolean imageUpdate; /* 节目是否为新 */
    public Bitmap imageImage;
    public int width;
    public int height;
    public String imageTypeSys;
    public String imageExpandName;
    /**
     * @return the imageImage
     */
    public Bitmap getImage() {
        return imageImage;
    }

    /**
     * @param imageImage
     *            the imageImage to set
     */
    public void setImage(Bitmap imageImage) {
        this.imageImage = imageImage;
    }

    /**
     * @return the imageId
     */
    public int getImageId() {
        return imageId;
    }

    /**
     * @param imageId
     *            the imageId to set
     */
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    /**
     * @param imageUpdate
     *            the imageUpdate to set
     */
    public void setImageUpdate(boolean imageUpdate) {
        this.imageUpdate = imageUpdate;
    }
    /**
     * width
     *
     * @return
     */
    public int getImageWidth() {
        return width;
    }

    /**
     * @param width
     */
    public void setImageWidth(int width) {
        this.width = width;
    }

    /**
     * height
     *
     * @return
     */
    public int getImageHeight() {
        return height;
    }

    /**
     * @param height
     */
    public void setImageHeight(int height) {
        this.height = height;
    }
    /**
     * @return the imageTypeSys
     */
    public String getImageTypeSys() {
        return imageTypeSys;
    }

    /**
     * @param imageTypeSys the imageTypeSys to set
     */
    public void setImageTypeSys(String imageTypeSys) {
        this.imageTypeSys = imageTypeSys;
    }

    /**
     * @return the imageName
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * @param imageName
     *            the imageName to set
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    /**
     * @return the imageExpandName
     */
    public String getImageExpandName() {
        return imageExpandName;
    }

    /**
     * @param imageExpandName the imageExpandName to set
     */
    public void setImageExpandName(String imageExpandName) {
        this.imageExpandName = imageExpandName;
    }

    /**
     * @return the imageTime
     */
    public String getImageTime() {
        return imageTime;
    }

    /**
     * @param imageTime
     *            the imageTime to set
     */
    public void setImageTime(String imageTime) {
        this.imageTime = imageTime;
    }


    /**
     * @return the imageUpdate
     */
    public Boolean getImageUpdate() {
        return imageUpdate;
    }

    /**
     * @param imageUpdate
     *            the imageUpdate to set
     */
    public void setimageUpdate(Boolean imageUpdate) {
        this.imageUpdate = imageUpdate;
    }

    public String getImageDate() {
        return imageDate;
    }

    public void setImageDate(String imageDate) {
        this.imageDate = imageDate;
    }
    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath
     *            the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    /**
     * 是否被移出列表
     */
    public boolean folderRemoved = false;

    /**
     * @return the folderRemoved
     */
    public boolean isFolderRemoved() {
        return folderRemoved;
    }

    /**
     * @param folderRemoved
     *            the folderRemoved to set
     */
    public void setFolderRemoved(boolean folderRemoved) {
        this.folderRemoved = folderRemoved;
    }

    /**
     * @param folderUpdate
     *            the folderUpdate to set
     */
    public void setFolderUpdate(boolean folderUpdate) {
        this.folderUpdate = folderUpdate;
    }

    /**
     * @return the check
     */
    public boolean isCheck() {
        return check;
    }

    /**
     * @param check
     *            the check to set
     */
    public void setCheck(boolean check) {
        this.check = check;
    }

    /**
     * @return the folderName
     */
    public String getFolderName() {
        return folderName;
    }

    /**
     * @param folderName
     *            the folderName to set
     */
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    /**
     * @return the folderPath
     */
    public String getFolderPath() {
        return folderPath;
    }

    /**
     * @param folderPath
     *            the folderPath to set
     */
    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    /**
     * @return the imageNumber
     */
    public int getImageNumber() {
        return imageNumber;
    }

    /**
     * @param imageNumber
     *            the imageNumber to set
     */
    public void setImageNumber(int imageNumber) {
        this.imageNumber = imageNumber;
    }

    /**
     * @return the folderUpdate
     */
    public Boolean getFolderUpdate() {
        return folderUpdate;
    }

    /**
     * @param folderUpdate
     *            the folderUpdate to set
     */
    public void setFolderUpdate(Boolean folderUpdate) {
        this.folderUpdate = folderUpdate;
    }

}

