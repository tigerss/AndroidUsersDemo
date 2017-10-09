package com.demo.users;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.demo.users.api.User;

import java.io.Serializable;

public class UserDetailsActivity extends AppCompatActivity {

    private static final String TAG = "UserDetailsActivity";

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_details, menu);
        return true;
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

        findViewById(R.id.user_details_card_phone).setOnClickListener(onPhoneClickListener);
        findViewById(R.id.user_details_card_email).setOnClickListener(onEmailClickListener);
        findViewById(R.id.user_details_card_address).setOnClickListener(onAddressClickListener);
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

    private View.OnClickListener onPhoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "on phone clicked");

            Intent intent = new Intent(
                    Intent.ACTION_DIAL,
                    Uri.fromParts("tel", mUser.phone, null));
            startActivity(intent);
        }
    };
    private View.OnClickListener onEmailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "on email clicked");
        }
    };
    private View.OnClickListener onAddressClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "on address clicked");

            final String destination = Uri.encode(
                    // use only city because google maps has problems finding addresses from randomuser.me
//                    mUser.location.street + "," +
                            mUser.location.city);
            Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/dir/?api=1&destination="
                        + destination + "&travelmode=driving")
            );
            startActivity(intent);
        }
    };
}
