package com.example.elnemr.final_movie_app;

import java.io.Serializable;

/**
 * Created by elnemr on 5/5/16.
 */
public class  Pojo implements Serializable {

    private String imageurl;
    private String year;
    private String vote_average;
    private String overview;
    private String title;
    private String id;
    private String trailer;
    private String trailerText;
    private String author;
    private String content;


    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getTrailerText() {
        return trailerText;
    }

    public void setTrailerText(String trailerText) {
        this.trailerText = trailerText;
    }

    @Override
    public String toString() {
        return "Pojo{" +
                "imageurl='" + imageurl + '\'' +
                ", year='" + year + '\'' +
                ", vote_average='" + vote_average + '\'' +
                ", overview='" + overview + '\'' +
                ", title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", trailer='" + trailer + '\'' +
                ", trailerText='" + trailerText + '\'' +
                '}';
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}