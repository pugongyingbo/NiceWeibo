package com.it.zzb.niceweibo.bean;

import android.graphics.Bitmap;


import com.it.zzb.niceweibo.util.Bimp;

import java.io.IOException;


public class ImageBean {
    public String id;
    public String path;
    private Bitmap bitmap;

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String thumbnailPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        if(bitmap == null){
            try {
                bitmap = Bimp.revitionImageSize(path);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
