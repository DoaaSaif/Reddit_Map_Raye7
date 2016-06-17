package com.raye7.reddit.Screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.raye7.reddit.Model.Post;
import com.raye7.reddit.R;


public class PostDialog extends Activity implements
        View.OnClickListener {


    // Content View Elements

    private EditText mNew_post_text;
    private EditText mNew_post_description;
    private EditText  mNew_post_image_link;
    private Button mNew_post_ok_btn;
    private Button mNew_post_cancel_btn;

    // End Of Content View Elements


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_post_dialog);
        bindViews();


    }

    private void bindViews() {

        mNew_post_text = (EditText) findViewById(R.id.new_post_text);
        mNew_post_description = (EditText) findViewById(R.id.new_post_description);

        mNew_post_image_link = (EditText) findViewById(R.id.new_post_image_link);
        mNew_post_ok_btn = (Button) findViewById(R.id.new_post_ok_btn);
        mNew_post_cancel_btn = (Button) findViewById(R.id.new_post_cancel_btn);
        mNew_post_cancel_btn.setOnClickListener(this);
        mNew_post_ok_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Post post;
        switch (v.getId()) {

            case R.id.new_post_ok_btn:

                post=new Post(mNew_post_text.getText().toString(),mNew_post_description.getText().toString(),  mNew_post_image_link.getText().toString());

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",post);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                break;
            case R.id.new_post_cancel_btn:
                finish();
                break;
            default:
                break;
        }
    }

}
