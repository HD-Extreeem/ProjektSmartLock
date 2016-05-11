package com.hellomicke89gmail.projektsmartlock;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder>{

            private static final int TYPE_HEADER = 0;
            private static final int TYPE_ITEM = 1;

            private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
            private int mIcons[];
            private String loginLabel;
            private int profileImage;
            private String usernameLabel;
            private ApprovedListView approvedListView;
            private Context context;

             DrawerAdapter(String Titles[], int Icons[], String loginLabel, String usernameLabel, int profileImage, ApprovedListView approvedListView, Context context){ // DrawerAdapter Constructor with titles and icons parameter

                this.mNavTitles = Titles; //have seen earlier
                this.mIcons = Icons;
                this.loginLabel = loginLabel;
                this.usernameLabel = usernameLabel;
                this.approvedListView=approvedListView;
                this.profileImage = profileImage;
                this.context=context;


            }
        public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row_layout,parent,false); //Inflating the layout
                ViewHolder holderItem = new ViewHolder(v,viewType, context, approvedListView); //Creating ViewHolder and passing the object of type view
                return holderItem; // Returning the created object
                //inflate your layout and pass it to view holder
            } else if (viewType == TYPE_HEADER) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false); //Inflating the layout

                ViewHolder holderHeader = new ViewHolder(v,viewType, context, approvedListView); //Creating ViewHolder and passing the object of type view

                return holderHeader; //returning the object created
            }
            return null;
       }


        public void onBindViewHolder(DrawerAdapter.ViewHolder holder, final int position) {
            if(holder.Holderid ==1) {// position by 1 and pass it to the holder while setting the text and image
                holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
                holder.imageView.setImageResource(mIcons[position -1]);// Settimg the image with array of our icons
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (position){
                            case 1: approvedListView.unlock();
                                System.out.print("UNLOCK");
                                break;
                            case 2: approvedListView.emptylist();
                                System.out.print("EMPTYLIST");
                                break;

                        }
                    }
                });



            }else{

                holder.profileImage.setImageResource(profileImage); // Similarly we set the resources for header view
                holder.loginLabel.setText(loginLabel);
                holder.userNameLabel.setText(usernameLabel);
            }
        }



        public int getItemCount() {
            return mNavTitles.length+1; // the number of items in the list will be +1 the titles including the header view.
        }


        public int getItemViewType(int position) {
            if (isPositionHeader(position)) {
                return TYPE_HEADER;
            }
            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;
        TextView textView;
        ImageView imageView;
        ImageView profileImage;
        TextView loginLabel;
        TextView userNameLabel;
        Context context;
        ApprovedListView approvedListView;

        public ViewHolder(View itemView,int ViewType, Context context, ApprovedListView approvedListView) { // Creating ViewHolder Constructor with View and viewType As a parameter

            super(itemView);
            itemView.setClickable(true);

            this.context=context;
            this.approvedListView=approvedListView;


            //Type_Item is used to differentiate headerview from the recyclerview(where to put which components
            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1; // setting holder id as 1 as the object being populated are of type item row
            }else{
                loginLabel = (TextView) itemView.findViewById(R.id.LoginLabel); // Creating Text View object from header.xml for name
                userNameLabel = (TextView) itemView.findViewById(R.id.UsernameLabel); // Creating Text View object from header.xml for email
                profileImage = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profileImage pic
                Holderid = 0; // Setting holder id = 0 as the object being populated are of type header view
            }
        }

    }

}