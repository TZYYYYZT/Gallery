package com.example.lenovo.gallery;

/**
 * Created by lenovo on 2016/7/12.
 */
public class MyUrlLoader {
    protected String getUrl(MyDataModel model, int width, int height) {
        // Construct the url for the correct size here.
        return model.buildUrl(width, height);
    }
}
