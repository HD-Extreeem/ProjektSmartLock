package com.hellomicke89gmail.projektsmartlock;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class IdRecyclerAdapter extends RecyclerView.Adapter<IdRecyclerAdapter.PersonViewHolder>{
    HashMap<String,Boolean> idMap;
    HashMap<String, String> idNameMap;
    ArrayList<Person> keys;
    ApprovedListView listview;



    IdRecyclerAdapter(ArrayList<Person> keys, HashMap<String, Boolean> idMap, HashMap<String, String> idNameMap, ApprovedListView listview){
        this.idMap=idMap;
        this.keys = keys;
        this.idNameMap=idNameMap;
        this.listview=listview;

    }

    public IdRecyclerAdapter.PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        PersonViewHolder pvh=new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, final int position) {

        if(keys.get(position).getName() == null){

            holder.card_Id.setText(keys.get(position).getKey());
        }else {

            holder.card_Id.setText(keys.get(position).getName());
        }

        // holder.card_Id.setText(keys.get(position).getName());
        holder.card_Id.setOnClickListener(clickListener);
        holder.card_Id.setOnLongClickListener(longClickListener);

        holder.chkSelected.setChecked(idMap.get(keys.get(position).getKey()));

        holder.chkSelected.setTag(keys.get(position).getKey());

        holder.chkSelected.setOnClickListener(boxclickListener);

        holder.card_Id.setTag(holder);
        //  holder.card_Id.setText(keys.get(position));


    }

    public HashMap<String, Boolean> getIdMap(){
        return this.idMap;
    }

    public void onAttachedToRecyclerView(RecyclerView rcView){
        super.onAttachedToRecyclerView(rcView);
    }

    @Override
    public int getItemCount() {

        return keys.size();
    }


    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView card_Id;
        SwitchCompat chkSelected;


        PersonViewHolder(View itemView) {
            super(itemView);

            card_Id = (TextView)itemView.findViewById(R.id.card_id);
            chkSelected = (SwitchCompat) itemView.findViewById(R.id.chkSelected);

        }

    }

    View.OnClickListener clickListener=new View.OnClickListener(){
        @Override
        public void onClick(View view) {

            PersonViewHolder holder=(PersonViewHolder) view.getTag();
            int position=holder.getAdapterPosition();
            String key=keys.get(position).getKey();

            listview.errorToast(key);
            if(idMap.get(key)){
                idMap.put(key, false);
            }else{
                idMap.put(key, true);
            }

            listview.updateList(idMap);
            listview.updateIdAdapter(idMap, idNameMap);
            listview.saveToServer();


        }


    };

    View.OnClickListener boxclickListener=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            SwitchCompat cb = (SwitchCompat) v;

            String key=cb.getTag().toString();

            idMap.put(key,cb.isChecked());
            listview.updateList(idMap);
            listview.saveToServer();
        }
    };

    View.OnLongClickListener longClickListener=new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            PersonViewHolder holder = (PersonViewHolder) v.getTag();
            int position = holder.getAdapterPosition();
            String key = keys.get(position).getKey();
            listview.showPopUp(v, key);
            System.out.println(key);


            return true;
        }

    };




}