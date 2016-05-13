package com.hellomicke89gmail.projektsmartlock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HadiDeknache on 16-04-25.
 */
public class idFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static HashMap<String, Boolean> idMap;
    private static HashMap<String,String> idNameMaps;
    private static ArrayList<Person> keys;
    private static MainActivity listView;
    protected RecyclerView recyclerView;
    private View rootView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected RecyclerView.Adapter myAdapter;


    public static idFragment newInstance(ArrayList<Person> key,HashMap<String, Boolean> idMaps, HashMap<String,String> idNameMap,MainActivity listViews) {
        keys=key;
        idMap=idMaps;
        idNameMaps=idNameMap;
        listView= listViews;
        return new idFragment();
    }

    public idFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rootView = inflater.inflate(R.layout.fragment_id, container, false);
            swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
            swipeRefreshLayout.setOnRefreshListener(this);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView1);
            myAdapter = new IdRecyclerAdapter(keys, idMap, idNameMaps, this.listView);


            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addItemDecoration(new MyItemDecoration());

            recyclerView.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();


        return rootView;
    }



    public void updateAdapter(HashMap<String, Boolean> idMap, HashMap<String, String> idNameMap) {
        keys.clear();
        String key;

        Boolean approved;
        for (Map.Entry<String, Boolean> p : idMap.entrySet()) {
            String name = null;
            if (idNameMap.containsKey(p.getKey())) {
                name = idNameMap.get(p.getKey());
            }
            approved = p.getValue();
            key = p.getKey().toString();
            Person person = new Person(key, name, approved);
            this.keys.add(person);

        }
        idFragment.idMap =idMap;
        idNameMaps=idNameMap;
        if(recyclerView.getAdapter()!=null){
            recyclerView.swapAdapter(new IdRecyclerAdapter(this.keys, idMap,idNameMaps,listView), false);
        }else{
            myAdapter=new IdRecyclerAdapter(this.keys,idMap,idNameMaps,listView);
            recyclerView.setAdapter(myAdapter);
        }
    }


    @Override
    public void onRefresh() {
            swipeRefreshLayout.setRefreshing(true);
            listView.getIdList();

            swipeRefreshLayout.setRefreshing(false);
    }

    public static HashMap<String, String> getIdNameMap(){
        return (HashMap<String, String>)idNameMaps.clone();
    }

    public static HashMap<String, Boolean> getIdMap(){
        return (HashMap<String, Boolean>)idMap.clone();
    }


}