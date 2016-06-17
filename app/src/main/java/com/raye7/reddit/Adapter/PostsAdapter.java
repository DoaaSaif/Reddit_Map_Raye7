package com.raye7.reddit.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.raye7.reddit.App.AppController;
import com.raye7.reddit.Model.Post;
import com.raye7.reddit.R;
import com.raye7.reddit.Screens.HomeActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by shabegt8040 on 4/17/2016.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {
    protected static Activity activity;
    protected static List<Post> posts;

    private ImageLoader imageLoader;
    LayoutInflater inflater;

    // protected StoredPlaces.CardViewOnClickListener cardListener;
    public PostsAdapter(Activity activity, List<Post> data) {
        //  super(context, R.layout.mylist, data);
        // TODO Auto-generated constructor stub

        //this.context = context;
        this.posts = data;
        //inflater = (LayoutInflater) context. getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
    }

    /* public View getView(int position, View view, ViewGroup parent) {
         View rowView = inflater.inflate(R.layout.user_place_row, null, true);
         TextView place_name = (TextView) rowView.findViewById(R.id.user_place_name);
         ImageView place_imageView = (ImageView) rowView.findViewById(R.id.user_place_image);

         place_name.setText(places.get(position).getName());
         byte[] photo = places.get(position).getPhoto();
         if (photo != null) {
            // place_imageView.setImageBitmap(BitmapFactory.decodeByteArray(photo, 0, photo.length));
             Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);

             place_imageView.setImageBitmap(bmp);
         }  return rowView;

     }*/
    public Post getItem(int position) {
        return posts.get(position);
    }

  //  @Override
   // public int getItemViewType(int position) {
    //    return super.getItemViewType(position);
    //}

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row, parent, false);
        view.setTag(viewType);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        URL image_url;

        TextView post_text = holder.post_text;

        TextView post_description = holder.post_description;
        TextView post_rank = holder.post_rank;
        ImageButton post_vote_down = holder.post_vote_down;
        ImageButton post_vote_up = holder.post_vote_up;
        ImageButton post_comments = holder.post_comments;
        NetworkImageView post_imageView = holder.post_imageView;


        holder.post_vote_up.setTag(position);
        holder.post_vote_down.setTag(position);
        holder.post_comments.setTag(position);



        if (posts.get(position) != null) {
            if (posts.get(position).getText() != null)
                post_text.setText(posts.get(position).getText());

            if (posts.get(position).getRank() != null)
                post_rank.setText(Integer.toString(posts.get(position).getRank()));
            if (posts.get(position).getDescription() != null)
                post_description.setText(posts.get(position).getDescription());


            if (posts.get(position).getMain_img() != null) {

                try {
                    image_url = new URL(posts.get(position).getMain_img());
                    post_imageView.setImageUrl(String.valueOf(image_url), imageLoader);
                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (NullPointerException e) {
                    e.printStackTrace();

                }


            } //else
            //  post_imageView.setDefaultImageResId(R.mipmap.ic_launcher);
        }
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {// } implements View.OnClickListener {

        TextView post_text;
        TextView post_description;
        NetworkImageView post_imageView;
        TextView post_rank;
        ImageButton post_vote_up;
        ImageButton post_vote_down;

        ImageButton post_comments;



        public MyViewHolder(View itemView) {
            super(itemView);

            post_text = (TextView) itemView.findViewById(R.id.post_text);
            post_description = (TextView) itemView.findViewById(R.id.post_description);
            post_imageView = (NetworkImageView) itemView.findViewById(R.id.post_image);

            post_rank = (TextView) itemView.findViewById(R.id.rank_number_tv);
            post_vote_up = (ImageButton) itemView.findViewById(R.id.rank_up_btn);
            post_vote_down = (ImageButton) itemView.findViewById(R.id.rank_down_btn);
            post_comments = (ImageButton) itemView.findViewById(R.id.post_comment_btn);
            post_vote_up.setOnClickListener(HomeActivity.myOnClickListener);
            post_vote_down.setOnClickListener(HomeActivity.myOnClickListener);
            post_comments.setOnClickListener(HomeActivity.myOnClickListener);


        }

    }
}


