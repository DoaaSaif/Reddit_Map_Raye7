package com.raye7.reddit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment implements Parcelable {


    public static String COMMENT_TAG = "comment";
    private String id;
    private String comment;
    private String created_at;
    private String postId;

    public Comment() {
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    private void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    private void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPostId() {
        return postId;
    }

    private void setPostId(String postId) {
        this.postId = postId;
    }

    public Comment parseComment(String result) {
        Comment mComment = new Comment();
        try {
            JSONObject commentObject = new JSONObject(result);
            mComment.setComment("comment");
            mComment.setId(commentObject.getString("id"));
            mComment.setPostId(commentObject.getString("postId"));
            mComment.setCreated_at(commentObject.getString("created_at"));

            String date = commentObject.getString("created_at");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mComment;
    }

    public Comment(String postid,String commentbody) {
        this.setPostId(postid);
        this.setComment(commentbody);
    }

    protected Comment(Parcel in) {
        id = in.readString();
        comment = in.readString();
        created_at = in.readString();
        postId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(comment);
        dest.writeString(created_at);
        dest.writeString(postId);
    }

    @SuppressWarnings("unused")
    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}