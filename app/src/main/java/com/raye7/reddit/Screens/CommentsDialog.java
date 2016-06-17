package com.raye7.reddit.Screens;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.raye7.reddit.Adapter.CommentAdapter;
import com.raye7.reddit.Listener.AsyncTaskListener;
import com.raye7.reddit.Model.Comment;
import com.raye7.reddit.Model.Post;
import com.raye7.reddit.R;
import com.raye7.reddit.Server.PostsOperation;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

public class CommentsDialog extends Activity implements View.OnClickListener, AsyncTaskListener {
    private ListView mPost_comments_list;
    private EditText mAdd_comment_et;
    private Button mPost_comment_btn;


    private Post mPost;
    private CommentAdapter mCommentsAdapter;
    private PostsOperation mPostsOperation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.comments_dialog);
        bindViews();
        // mComments = getIntent().getParcelableArrayListExtra(Comment.COMMENT_TAG);
        mPost = getIntent().getParcelableExtra(Post.POST_TAG);

        mPostsOperation = new PostsOperation(CommentsDialog.this);
        if (mPost != null)
            mPostsOperation.GetPostComments(mPost.getId());

    }

    private void bindViews() {

        mPost_comments_list = (ListView) findViewById(R.id.post_comments_list);
        mAdd_comment_et = (EditText) findViewById(R.id.add_comment_et);
        mPost_comment_btn = (Button) findViewById(R.id.post_comment_btn);
        mPost_comment_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_comment_btn:
                if (!mAdd_comment_et.getText().toString().isEmpty())
                    mPostsOperation.AddComment(new Comment(mPost.getId(), mAdd_comment_et.getText().toString()));
                break;
        }
    }

    @Override
    public void onSuccessRemoteCallComplete(int operation, String result) {

        ArrayList<Comment> post_comments;
        switch (operation) {
            case R.string.get_post_comments:
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(result);

                    post_comments = new ArrayList<Comment>();
                    for (int index = 0; index < jsonArray.length(); index++) {
                        //parse comment object, insert it into list
                        post_comments.add(new Comment().parseComment(jsonArray.getString(index)));
                    }
                    //start comments dialog list to display the returned comments
                    if (post_comments.size() > 0) {
                        Collections.reverse(post_comments);
                        mCommentsAdapter = new
                                CommentAdapter(CommentsDialog.this, post_comments);
                        mPost_comments_list.setAdapter(mCommentsAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case R.string.submit_comment:
                //get comments after sumbmitting user's comment
                mPostsOperation.GetPostComments(mPost.getId());
                break;
        }
    }

    @Override
    public void onFailRemoteCallComplete(int operation, String result) {
        switch (operation) {
            case R.string.get_post_comments:
                Toast.makeText(CommentsDialog.this, getResources().getString(R.string.error_loading_comments), Toast.LENGTH_LONG).show();


                break;
            case R.string.submit_comment:
                Toast.makeText(CommentsDialog.this, getResources().getString(R.string.error_submit_comment), Toast.LENGTH_LONG).show();

                break;
        }
    }
}

