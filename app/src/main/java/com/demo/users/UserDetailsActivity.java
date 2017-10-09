package com.demo.users;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.demo.users.api.User;

import java.io.Serializable;

public class UserDetailsActivity extends AppCompatActivity {

    private ImageView mAvatarImageView;
    private TextView mNameTextView;
    private TextView mPhoneTextView;
    private TextView mEmailTextView;
    private TextView mAddressTextView;
    private TextView mUserIdTextView;

    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        setUpToolbar();

        if (!readUserFromIntent()) {
            finish();
            return;
        }

        setupViews();

        refreshUserFields(mUser);

        loadUserAvatar();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private boolean readUserFromIntent() {
        Serializable userArgument = getIntent().getSerializableExtra("user");

        if (null == userArgument || !(userArgument instanceof User)) {
            return false;
        }

        mUser = (User) userArgument;
        return true;
    }

    private void setupViews() {
        mAvatarImageView = findViewById(R.id.user_details_avatar);
        mNameTextView = findViewById(R.id.user_details_name);
        mPhoneTextView = findViewById(R.id.user_details_phone_number);
        mEmailTextView = findViewById(R.id.user_details_email);
        mAddressTextView = findViewById(R.id.user_details_address);
        mUserIdTextView = findViewById(R.id.user_details_id);
    }

    private void refreshUserFields(final User user) {
        final String firstName = Utils.capitalizeFirstLetter(user.name.first);
        final String lastName = Utils.capitalizeFirstLetter(user.name.last);
        mNameTextView.setText(firstName + " " + lastName);

        mPhoneTextView.setText(user.phone);
        mEmailTextView.setText(user.email);
        mAddressTextView.setText(user.location.street);
        mUserIdTextView.setText("ID: " + user.id.name + " " + user.id.value);
    }

    private void loadUserAvatar() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.user_placeholder)
                .transform(new CircleCrop());
        Glide.with(this)
                .load(mUser.picture.large)
                .apply(options)
                .into(mAvatarImageView);
    }
}
