package com.example.lenovo.gallery.bean;

import android.graphics.Bitmap;

public class ImageBean {
    public String imageDate;
    public int imageId;
    public String imageName; /* 名称 */
    public String imagePath; /* 路径 */
    public Bitmap image;
    public int width;
    public int height;
    /**
     * 文件名称（不含扩展名）
     */
    public String imageNameNonType;

    /**
     * 文件扩展名
     */
    public String imageExpandName;

    /**
     * 所在文件夹名称
     */
    public String videoFolderName;

    /**
     * 所在文件夹路径
     */
    public String videoFolderPath;
    // DB路径
    public String videoStreamPath;


    /**
     * 文件类型（系统类型）
     */
    public String imageTypeSys;

    /**
     * @return the streamImage
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * @param streamImage the streamImage to set
     */
    public void setImage(Bitmap streamImage) {
        this.image = streamImage;
    }

    /**
     * @return the imageId
     */
    public int getImageId() {
        return imageId;
    }

    /**
     * @param imageId the streamId to set
     */
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }


    /**
     * @return the imageName
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * @param imageName the imageName to set
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public String getImageDate() {
        return imageDate;
    }

    public void setImageDate(String imageDate) {
        this.imageDate = imageDate;
    }

    /**
     * @return the videoNameNonType
     */
    public String getImageNameNonType() {
        return imageNameNonType;
    }

    /**
     * @param imageNameNonType the v imageNameNonType to set
     */
    public void setImageNameNonType(String imageNameNonType) {
        this.imageNameNonType = imageNameNonType;
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
     * @return the videoFolderName
     */
    public String getVideoFolderName() {
        return videoFolderName;
    }

    /**
     * @param videoFolderName the videoFolderName to set
     */
    public void setVideoFolderName(String videoFolderName) {
        this.videoFolderName = videoFolderName;
    }

    /**
     * @return the videoFolderPath
     */
    public String getVideoFolderPath() {
        return videoFolderPath;
    }

    /**
     * @param videoFolderPath the videoFolderPath to set
     */
    public void setVideoFolderPath(String videoFolderPath) {
        this.videoFolderPath = videoFolderPath;
    }

    /**
     * @return the videoStreamPath
     */
    public String getVideoStreamPath() {
        return videoStreamPath;
    }

    /**
     * @param videoStreamPath the videoStreamPath to set
     */
    public void setVideoStreamPath(String videoStreamPath) {
        this.videoStreamPath = videoStreamPath;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "imageDate='" + imageDate + '\'' +
                ", imageId=" + imageId +
                ", imageName='" + imageName + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", image=" + image +
                ", width=" + width +
                ", height=" + height +
                ", imageNameNonType='" + imageNameNonType + '\'' +
                ", imageExpandName='" + imageExpandName + '\'' +
                ", videoFolderName='" + videoFolderName + '\'' +
                ", videoFolderPath='" + videoFolderPath + '\'' +
                ", videoStreamPath='" + videoStreamPath + '\'' +
                ", imageTypeSys='" + imageTypeSys + '\'' +
                '}';
    }
}
