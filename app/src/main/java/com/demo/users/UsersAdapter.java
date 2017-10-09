package com.demo.users;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.demo.users.api.User;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

public class UsersAdapter extends
        RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    public interface OnUserClickListener {
        void onClick(View view, int position);
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatar;
        public TextView nameTextView;
        public TextView ageTextView;
        public TextView countryCodeTextView;
        public TextView time;
        public ImageView flag;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            avatar = itemView.findViewById(R.id.item_user_avatar);
            nameTextView = itemView.findViewById(R.id.item_user_name);
            ageTextView = itemView.findViewById(R.id.item_user_age);
            countryCodeTextView = itemView.findViewById(R.id.item_user_country_code);
            time = itemView.findViewById(R.id.item_user_time);
            flag = itemView.findViewById(R.id.item_user_flag);
        }
    }

    private List<User> mUsers;
    private Context mContext;
    private OnUserClickListener mListener;

    // Used for computing age
    private DateTime today;

    public UsersAdapter(Context context, List<User> users, OnUserClickListener listener) {
        mUsers = users;
        mContext = context;
        mListener = listener;

        today = new DateTime();
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
        final ViewHolder viewHolder = new ViewHolder(userRowView);

        // Setup click listener
        userRowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (null != mListener) {
                    mListener.onClick(view, viewHolder.getLayoutPosition());
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UsersAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        User user = mUsers.get(position);

        // Set item views based on your views and data model
        final String firstName = Utils.capitalizeFirstLetter(user.name.first);
        final String lastName = Utils.capitalizeFirstLetter(user.name.last);
        TextView textView = viewHolder.nameTextView;
        textView.setText(firstName + " " + lastName);

        viewHolder.countryCodeTextView.setText(user.nat);

        DateTime dateOfBirth = DateTime.parse(
                user.dob,
                DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        );

        // Compute age
        final int age = new Period(dateOfBirth, today).getYears();
        viewHolder.ageTextView.setText(String.valueOf(age));

        viewHolder.time.setText(dateOfBirth.toString("HH:mm"));

        // Create a text placholder Gmail style
        ColorGenerator generator = ColorGenerator.MATERIAL;
        final int randomColor = generator.getRandomColor();
        TextDrawable placeholder = TextDrawable.builder()
                .buildRound(
                        user.name.first.substring(0,1).toUpperCase(),
                        randomColor
                );

        loadUserAvatar(viewHolder, user, placeholder);

//        http://www.geognos.com/api/en/countries/flag/ES.png
        Glide.with(getContext())
                .load("http://www.geognos.com/api/en/countries/flag/" + user.nat + ".png")
                .into(viewHolder.flag);
    }

    private void loadUserAvatar(ViewHolder viewHolder, User user, TextDrawable placeholder) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .transform(new CircleCrop());
        Glide.with(getContext())
                .load(user.picture.thumbnail)
                .apply(options)
                .into(viewHolder.avatar);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
