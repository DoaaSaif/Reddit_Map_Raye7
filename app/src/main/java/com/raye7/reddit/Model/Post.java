package com.raye7.reddit.Model;

import android.os.Parcel;
import android.os.Parcelable;


public class Post implements Parcelable {

    //------------Tags-----------

    public static String POST_TAG = "post";
    public static String ID_TAG = "id";
    public static String TEXT_TAG = "text";
    public static String DESCRIPTION_TAG = "description";
    public static String RANK_TAG = "rank";
    public static String MAIN_IMAGE_TAG = "main_img";
    //---------------------------


    private String id;
    private String text;
    private String description;
    private Integer rank;
    private String main_img;

    public Post(String text, String description, String link){
        this.setDescription(description);
        this.setMain_img(link);
        this.setText(text);
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    private void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public Integer getRank() {
        return rank;
    }

    private void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getMain_img() {
        return main_img;
    }

    private void setMain_img(String main_img) {
        this.main_img = main_img;
    }

    protected Post(Parcel in) {
        id = in.readString();
        text = in.readString();
        description = in.readString();
        rank = in.readByte() == 0x00 ? null : in.readInt();
        main_img = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(text);
        dest.writeString(description);
        if (rank == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(rank);
        }
        dest.writeString(main_img);
    }

    @SuppressWarnings("unused")
    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}