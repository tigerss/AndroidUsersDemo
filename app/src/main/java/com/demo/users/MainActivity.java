package com.demo.users;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.demo.users.api.ServiceGenerator;
import com.demo.users.api.User;
import com.demo.users.api.UsersResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int USERS_REQUEST_LIMIT = 100;
    private static final String USERS_REQUEST_SEED = "abc";

    private EndlessRecyclerViewScrollListener mScrollListener;
    private ServiceGenerator.RandomUsersService mApiService;

    private UsersAdapter mUsersAdapter;
    private List<User> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView usersList = findViewById(R.id.main_users_recycler_view);

        mUsers = new ArrayList<>();
        mUsersAdapter = new UsersAdapter(this, mUsers);
        usersList.setAdapter(mUsersAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        usersList.setLayoutManager(linearLayoutManager);

        mScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextUsersPage(page);
            }
        };

        usersList.addOnScrollListener(mScrollListener);

        mApiService = ServiceGenerator.createService(ServiceGenerator.RandomUsersService.class);
        loadNextUsersPage(0);
    }

    public void loadNextUsersPage(final int pageOffset) {
        Log.d(TAG, "loadNextUsersPage: " + pageOffset);

        Call<UsersResponse> call = mApiService.getUsers(
                pageOffset,
                USERS_REQUEST_LIMIT,
                USERS_REQUEST_SEED
        );
        call.enqueue(usersCallback);
    }

    private void addMoreUsersToList(List<User> users) {
        final int currentSize = mUsersAdapter.getItemCount();

        mUsers.addAll(users);
        mUsersAdapter.notifyItemRangeInserted(currentSize, users.size());
    }

    private final Callback<UsersResponse> usersCallback = new Callback<UsersResponse>() {
        @Override
        public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {

            if (response.isSuccessful()) {
                UsersResponse usersResponse = response.body();

                addMoreUsersToList(usersResponse.results);
            } else {
                try {
                    Log.d(TAG, response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<UsersResponse> call, Throwable t) {
            Log.e(TAG, t.getMessage(), t);
        }
    };
}