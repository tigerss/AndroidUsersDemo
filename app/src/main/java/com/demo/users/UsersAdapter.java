package com.demo.users;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.demo.users.api.User;

import java.util.List;

public class UsersAdapter extends
        RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public Button messageButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = itemView.findViewById(R.id.item_user_name);
//            messageButton = (Button) itemView.findViewById(R.id.item_;
        }
    }

        // Store a member variable for the contacts
        private List<User> mUsers;
        // Store the context for easy access
        private Context mContext;

        // Pass in the contact array into the constructor
        public UsersAdapter(Context context, List<User> contacts) {
            mUsers = contacts;
            mContext = context;
        }

        // Easy access to the context object in the recyclerview
        private Context getContext() {
            return mContext;
        }

        // Usually involves inflating a layout from XML and returning the holder
        @Override
        public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View userRowView = inflater.inflate(R.layout.item_user, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(userRowView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(UsersAdapter.ViewHolder viewHolder, int position) {
            // Get the data model based on position
            User user = mUsers.get(position);

//            // Set item views based on your views and data model
            TextView textView = viewHolder.nameTextView;
            textView.setText(user.name.first + " " + user.name.last);
//            Button button = viewHolder.messageButton;
//            button.setText(contact.isOnline() ? "Message" : "Offline");
//            button.setEnabled(contact.isOnline());
        }

        // Returns the total count of items in the list
        @Override
        public int getItemCount() {
            return mUsers.size();
        }
}
