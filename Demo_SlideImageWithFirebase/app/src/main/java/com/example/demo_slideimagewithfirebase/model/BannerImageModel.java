package com.example.demo_slideimagewithfirebase.model;

public class BannerImageModel {

    String bannerImageOne, bannerImageTwo, bannerImageThree, bannerImageFour;

    public BannerImageModel() {
    }

    public BannerImageModel(String bannerImageOne, String bannerImageTwo, String bannerImageThree, String bannerImageFour) {
        this.bannerImageOne = bannerImageOne;
        this.bannerImageTwo = bannerImageTwo;
        this.bannerImageThree = bannerImageThree;
        this.bannerImageFour = bannerImageFour;
    }

    public String getBannerImageOne() {
        return bannerImageOne;
    }

    public void setBannerImageOne(String bannerImageOne) {
        this.bannerImageOne = bannerImageOne;
    }

    public String getBannerImageTwo() {
        return bannerImageTwo;
    }

    public void setBannerImageTwo(String bannerImageTwo) {
        this.bannerImageTwo = bannerImageTwo;
    }

    public String getBannerImageThree() {
        return bannerImageThree;
    }

    public void setBannerImageThree(String bannerImageThree) {
        this.bannerImageThree = bannerImageThree;
    }

    public String getBannerImageFour() {
        return bannerImageFour;
    }

    public void setBannerImageFour(String bannerImageFour) {
        this.bannerImageFour = bannerImageFour;
    }
}
