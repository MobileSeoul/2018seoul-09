package com.example.ggavi.ahnsungmin.nomal_mode;

/**
 * Created by PHU
 */


public class ImageData {


    String userId;
    String image_data;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage_data() {
        return image_data;
    }

    public void setImage_data(String image_data) {
        this.image_data = image_data;
    }

    public ImageData(String userId, String image_data) {
        this.userId = userId;
        this.image_data = image_data;
    }


}
