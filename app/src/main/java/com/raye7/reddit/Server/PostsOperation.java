package com.raye7.reddit.Server;

import com.raye7.reddit.Listener.AsyncTaskListener;
import com.raye7.reddit.Model.Comment;
import com.raye7.reddit.Model.Post;
import com.raye7.reddit.R;
import com.raye7.reddit.utils.Const;

/**
 * Created by Doaa Saif on 6/11/2016.
 */

public class PostsOperation {
    private AsyncTaskListener mListener;
    private Connection mConnection;


    private String COMMENTS_TAG = "comments";
    private String VOTE_UP_TAG = "incrementrank";
    private String VOTE_DOWN_TAG = "decrementrank";

    //--------------------Operation---------------
    private String GetAllPosts = "GetAllPosts";
    private String GetPostComments = "GetPostComments";
    private String VotePostUp = "VoteUp";
    private String VotePostDown = "VoteDown";


    public PostsOperation(AsyncTaskListener listener) {
        this.mListener = listener;
        mConnection = new Connection(mListener);

    }

    public void GetAllPosts() {
        mConnection.getRequest(R.string.get_all_posts, Const.FETCHING_POSTS_URL);


    }

    public void GetPostComments(String id) {
        String comment_url = Const.BASE_URL + "/" + id + "/" + COMMENTS_TAG;
        mConnection.getRequest(R.string.get_post_comments, comment_url);
    }


    public void AddPost(Post post) {

        mConnection.submitPostRequest(R.string.submit_post, Const.BASE_URL, post);
    }

    public void AddComment(Comment comment) {

        String url = Const.BASE_URL + comment.getPostId() + "/" + COMMENTS_TAG;
        mConnection.submitCommentRequest(R.string.submit_comment, url, comment);
    }

    public void VotePostUp(String id) {
        String vote_up_url = Const.BASE_URL + "/" + id + "/" + VOTE_UP_TAG;
        mConnection.postRequest(R.string.vote_post_up, vote_up_url);
    }

    public void VotePostDown(String id) {
        String vote_down_url = Const.BASE_URL + "/" + id + "/" + VOTE_DOWN_TAG;
        mConnection.postRequest(R.string.vote_post_down, vote_down_url);
    }


}
