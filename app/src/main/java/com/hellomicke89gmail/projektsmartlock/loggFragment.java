package com.hellomicke89gmail.projektsmartlock;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.*;


/**
 * Created by HadiDeknache on 16-04-25.
 */
public class loggFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView loggRecyclerView;
    private LogRecyclerAdapter loggAdapter;
    private static ArrayList<LogInfo> logList;
    protected SwipeRefreshLayout swipeRefreshLayout;
    private static ApprovedListView listView;
    protected RecyclerView.Adapter myAdapter;
    private View rootView;

    //private tabAdapter adapter;


    public static loggFragment newInstance(ArrayList<LogInfo> logglist, ApprovedListView approvedListView) {
        logList=logglist;
        listView = approvedListView;
        return new loggFragment();
    }

    public loggFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);

        rootView = inflater.inflate(R.layout.fragment_logg, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefreshLogg);
        swipeRefreshLayout.setOnRefreshListener(this);


        myAdapter = new LogRecyclerAdapter(logList);
        loggRecyclerView = (RecyclerView) rootView.findViewById(R.id.logglist);

        final LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());

        loggRecyclerView.setLayoutManager(LinearLayoutManager);
        loggRecyclerView.addItemDecoration(new MyItemDecoration());

        loggRecyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();




        return rootView;
    }

    public void updateAdapter(ArrayList<LogInfo> loggList) {
        this.logList.clear();
        this.logList=loggList;

        if(loggRecyclerView.getAdapter()!=null){
            loggRecyclerView.swapAdapter(new LogRecyclerAdapter(this.logList), false);
        }else{
            loggAdapter=new LogRecyclerAdapter(this.logList);
            loggRecyclerView.setAdapter(loggAdapter);
        }
    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        listView.getLoggList();

        swipeRefreshLayout.setRefreshing(false);
    }



}