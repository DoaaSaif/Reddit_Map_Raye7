package com.raye7.reddit.Screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raye7.reddit.Adapter.CommentAdapter;
import com.raye7.reddit.Adapter.PostsAdapter;
import com.raye7.reddit.Listener.AsyncTaskListener;
import com.raye7.reddit.Model.Post;
import com.raye7.reddit.R;
import com.raye7.reddit.Server.PostsOperation;

import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AsyncTaskListener {


    public static View.OnClickListener myOnClickListener;
    private static PostsAdapter postsAdapter;
    private static CommentAdapter commentAdapter;
    private RecyclerView mPosts_recyclerlist;

    private RecyclerView.LayoutManager layoutManager;

    private static PostsOperation mPostsOperation;
    private static Intent commentsIntent;
    List<Post> posts_list;

    int AddPostDialogTag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // mConnection = new Connection(HomeActivity.this);
        mPostsOperation = new PostsOperation(HomeActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPosts_recyclerlist = (RecyclerView) findViewById(R.id.posts_recyclerlist);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(HomeActivity.this, PostDialog.class), AddPostDialogTag);
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG) .setAction("Action", null).show();

            }
        });
        commentsIntent = new Intent(HomeActivity.this, CommentsDialog.class);
        myOnClickListener = new CardViewOnClickListener(this);

        mPostsOperation.GetAllPosts();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AddPostDialogTag) {
            if (resultCode == Activity.RESULT_OK) {
                Post post = data.getParcelableExtra("result");
                mPostsOperation.AddPost(post);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    private class CardViewOnClickListener implements View.OnClickListener {

        private final Context context;

        private CardViewOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            TextView rank_textview;
            Integer rank_value;
            Intent activityIntent;
            Post selectedPost;
            Integer itemPosition = (Integer) v.getTag();

            android.support.v7.widget.RecyclerView.ViewHolder viewHolder = mPosts_recyclerlist.findViewHolderForAdapterPosition(itemPosition);
            selectedPost = posts_list.get(itemPosition);
            rank_value = selectedPost.getRank();
            rank_textview = ((TextView) viewHolder.itemView.findViewById(R.id.rank_number_tv));

            switch (v.getId()) {
                case (R.id.rank_down_btn):
                    //update displayed rank vlaue
                    //update rank vlaue on server
                    rank_textview.setText(Integer.toString(rank_value - 1));
                    mPostsOperation.VotePostDown(selectedPost.getId());
                    break;
                case (R.id.rank_up_btn):
                    //update displayed rank vlaue
                    //update rank vlaue on server
                    rank_textview.setText(Integer.toString(rank_value + 1));
                    mPostsOperation.VotePostUp(selectedPost.getId());
                    break;
                case (R.id.post_comment_btn):


                  //  mPostsOperation.GetPostComments(.getId());
                    commentsIntent.putExtra(Post.POST_TAG , posts_list.get(itemPosition));
                    startActivity(commentsIntent);

                    break;
            }
        }


    }


    @Override
    public void onSuccessRemoteCallComplete(int Operation, String result) {
        Gson gson = new Gson();

        // switch on the requested operation
            switch (Operation) {
                case R.string.get_all_posts:
                    //convert retrieved posts into List of posts
                    posts_list = gson.fromJson(result, new TypeToken<List<Post>>() { }.getType());
                    Collections.reverse(posts_list);
                    postsAdapter = new PostsAdapter(HomeActivity.this, posts_list );
                    mPosts_recyclerlist.setHasFixedSize(true);


                    layoutManager = new LinearLayoutManager(this);
                    mPosts_recyclerlist.setLayoutManager(layoutManager);
                    mPosts_recyclerlist.setItemAnimator(new DefaultItemAnimator());
                    mPosts_recyclerlist.setAdapter(postsAdapter);
                    break;

            }


    }

    @Override
    public void onFailRemoteCallComplete(int Operation, String result) {
        switch (Operation) {
            case R.string.get_all_posts:
                Toast.makeText(HomeActivity.this, getResources().getString(R.string.error_loading_posts), Toast.LENGTH_LONG).show();
                break;
            case R.string.get_post_comments:

                Toast.makeText(HomeActivity.this, getResources().getString(R.string.error_loading_comments), Toast.LENGTH_LONG).show();
                break;
        }
    }
}
