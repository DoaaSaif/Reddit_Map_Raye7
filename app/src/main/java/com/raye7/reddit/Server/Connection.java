package com.raye7.reddit.Server;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.raye7.reddit.App.AppController;
import com.raye7.reddit.Listener.AsyncTaskListener;
import com.raye7.reddit.Model.Comment;
import com.raye7.reddit.Model.Post;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 06-Jun-16.
 */
public class Connection {
    AsyncTaskListener mAsyncTaskListener;

    private String tag_string_req = "string_req";

    public Connection(AsyncTaskListener listener) {
        mAsyncTaskListener = listener;
    }

    public void postRequest(final int operation, String url) {
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskListener.onSuccessRemoteCallComplete(operation, response);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mAsyncTaskListener.onFailRemoteCallComplete(operation, error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    public void submitPostRequest(final int operation, String url, final Post post) {
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskListener.onSuccessRemoteCallComplete(operation, response);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mAsyncTaskListener.onFailRemoteCallComplete(operation, error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Post.DESCRIPTION_TAG, post.getDescription());
                params.put(Post.TEXT_TAG, post.getText());
                params.put(Post.MAIN_IMAGE_TAG, post.getMain_img());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void submitCommentRequest(final int operation, String url, final Comment comment) {
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskListener.onSuccessRemoteCallComplete(operation, response);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mAsyncTaskListener.onFailRemoteCallComplete(operation, error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Comment.COMMENT_TAG, comment.getComment());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    public void getRequest(final int operation,String url) {
        StringRequest strReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskListener.onSuccessRemoteCallComplete(operation,response);

                        Log.e("HttpClient", "success! response: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        mAsyncTaskListener.onFailRemoteCallComplete(operation,error.toString());
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
