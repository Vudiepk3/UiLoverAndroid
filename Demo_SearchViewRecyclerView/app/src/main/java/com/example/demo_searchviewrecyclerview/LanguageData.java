package com.example.demo_searchviewrecyclerview;

public class LanguageData {
    private String title;
    private int logo;

    public LanguageData(String title, int logo) {
        this.title = title;
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public int getLogo() {
        return logo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "LanguageData{" +
                "title='" + title + '\'' +
                ", logo=" + logo +
                '}';
    }
}
