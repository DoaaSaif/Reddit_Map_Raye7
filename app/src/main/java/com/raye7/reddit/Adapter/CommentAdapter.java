package com.raye7.reddit.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.raye7.reddit.Model.Comment;
import com.raye7.reddit.R;

import java.util.ArrayList;


public class CommentAdapter extends ArrayAdapter<Comment> {
    private final Context context;
    private final ArrayList<Comment>mComments;


    public CommentAdapter(Context context,ArrayList<Comment> comments) {
        super(context, R.layout.comment_row, comments);
        this.context = context;
        this.mComments=comments;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.comment_row, parent, false);
        TextView comment_text = (TextView) rowView.findViewById(R.id.comment_text);

        TextView comment_date = (TextView) rowView.findViewById(R.id.comment_date);
        if(mComments.get(position).getComment()!= null)
            comment_text.setText(mComments.get(position).getComment());
        if(mComments.get(position).getCreated_at()!= null)
            comment_date.setText(mComments.get(position).getCreated_at().toString());
        return rowView;
    }

    private class List<T> {
    }
}