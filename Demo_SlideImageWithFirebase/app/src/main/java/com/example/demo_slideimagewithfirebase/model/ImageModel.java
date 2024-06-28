package com.example.demo_slideimagewithfirebase.model;

public class ImageModel {
    private String  urlImage,nameImgae,noteImage,linkWebsite;
    public ImageModel(String urlImage, String nameImgae, String noteImage) {
        this.urlImage = urlImage;
        this.nameImgae = nameImgae;
        this.noteImage = noteImage;
    }

    public ImageModel(String urlImage, String nameImgae, String noteImage, String linkWebsite) {
        this.urlImage = urlImage;
        this.nameImgae = nameImgae;
        this.noteImage = noteImage;
        this.linkWebsite = linkWebsite;
    }

    public String getLinkWebsite() {
        return linkWebsite;
    }

    public void setLinkWebsite(String linkWebsite) {
        this.linkWebsite = linkWebsite;
    }

    public ImageModel() {
    }

    public String getNameImgae() {
        return nameImgae;
    }

    public void setNameImgae(String nameImgae) {
        this.nameImgae = nameImgae;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(String noteImage) {
        this.noteImage = noteImage;
    }
}
