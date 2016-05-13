package com.hellomicke89gmail.projektsmartlock;

import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.*;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.*;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.*;




public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private Toolbar myToolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    HashMap<String, Boolean> persons=new HashMap<>();
    ArrayList<Person> keys=new ArrayList<>();
    private String key;
    HashMap<String, String> idNameMap=new HashMap<>();
    idFragment idfragment;
    loggFragment loggfragment=new loggFragment();
    ArrayList<LogInfo> loggList=new ArrayList<>();
    private static String authString;

    private BroadcastReceiver broadcastReceiver;
    private String logType;
    private String titles[] = {"Unlock","Clear List","Search"};

    TextView loginLabel;
    private static String usernameLabel;
    int[] icons={R.drawable.unlockicon,R.drawable.empty,R.drawable.search};
    int profileImage = R.drawable.person_icon;
    private DrawerLayout myDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        registercomponents();
        setupDrawer();
        setUpViewPager();
        navigationview();

        getLoggList();
        getIdList();


            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
            if (ConnectionResult.SUCCESS != resultCode) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    errorToast("Googleplay Service is not installed");
                    GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
                } else {
                    errorToast("Device does not support GooglePlay Services");
                }
            } else {
                Intent intent = new Intent(this, GCMRegistrationIntentService.class);
                startService(intent);
            }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("authString",authString);
        outState.putString("username",usernameLabel);
        Log.v("OnsaveInstanceState",usernameLabel);
        getSupportFragmentManager().putFragment(outState,"idFragmentState",idfragment);
        getSupportFragmentManager().putFragment(outState,"logFragmentState",loggfragment);
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        authString=inState.getString("authString");
        usernameLabel=inState.getString("username");
        idfragment=(idFragment) getSupportFragmentManager().getFragment(inState,"idFragmentState");
        loggfragment=(loggFragment) getSupportFragmentManager().getFragment(inState,"logFragmentState");
        Log.v("onRestoreInstance",usernameLabel);
        loginLabel.setText(usernameLabel);
        getLoggList();
        getIdList();


    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
      //  intentFilter.addAction("token");
        intentFilter.addAction("unlock");
        intentFilter.addAction("Change to card id data on server");
        intentFilter.addAction("log");

        broadcastReceiver = new MyBroadCastReciever();
        registerReceiver(broadcastReceiver, intentFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter(GCMRegistrationIntentService.registration_sucsses));
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter(GCMRegistrationIntentService.registration_failed));

    }


    private class MyBroadCastReciever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("unlock")){
                unlock();
                //unregisterReceiver(broadcastReceiver);
            }

            if(intent.getAction().equals(GCMRegistrationIntentService.registration_sucsses)){
                String token=intent.getStringExtra("token");
                errorToast("GCM token: "+ token);
                new AsycTaskPostToken(token,authString).execute();

            }else if(intent.getAction().equals(GCMRegistrationIntentService.registration_failed)){
                errorToast("GCM REGISTRATION FAILED");

            }

            else if(intent.getAction().equals("Change to card id data on server")){
                Log.v("APPROVEDLISTVIEW", "Change to card id data on server!");
                getIdList();
               // unregisterReceiver(broadcastReceiver);
            }
            else if(intent.getAction().equals("log")){
                Log.v("APPROVEDLISTVIEW", "Log data changed!");
                getLoggList();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("ONPAUSE", "Application has PAUSED");
        unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }


    private void setUpViewPager() {
        FragmentManager manager = getSupportFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(manager);
        adapter.addFragment(idfragment = idFragment.newInstance(keys,persons,idNameMap,this), "Id-Lista");
        adapter.addFragment(loggfragment=loggfragment.newInstance(loggList,this),"Logg-Lista");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        setSupportActionBar(myToolbar);
    }

    private void registercomponents() {

        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myDrawerLayout=(DrawerLayout)findViewById(R.id.parent_drawer_layout);
        View v=navigationView.getHeaderView(0);
        loginLabel=(TextView)v.findViewById(R.id.UsernameLabel);


    }
    public void searchLog() {

        showInputSearchDialog();
    }

    public void navigationview(){

        loginLabel.setText(usernameLabel);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(false);

                myDrawerLayout.closeDrawers();

                switch (item.getItemId()){
                    case R.id.unlock:
                        unlock();

                        item.setChecked(false);
                        return true;

                    case R.id.clearlist: emptylist();
                        item.setChecked(false);

                        return true;

                    case R.id.searchLog:
                        searchLog();
                        item.setCheckable(false);

                        return true;

                }
                return false;
            }
        });navigationView.setItemIconTintList(null);
    }
    public void setupDrawer(){

        drawerToggle=new ActionBarDrawerToggle(this, myDrawerLayout,myToolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);        }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        myDrawerLayout.addDrawerListener(drawerToggle);


        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }


    public void showInputDialog(final String key) {
        this.key=key;

        LayoutInflater inflater=LayoutInflater.from(MainActivity.this);
        View promptView=inflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);
        final EditText editext=(EditText)promptView.findViewById(R.id.name_edit_text);

        alertDialogBuilder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int id){
                persons=idFragment.getIdMap();
                idNameMap=idFragment.getIdNameMap();
                String cardName=editext.getText().toString();
                if(cardName.equals("")){
                    idNameMap.remove(key);
                }else{
                    idNameMap.put(key, cardName);
                }

                updateIdAdapter(persons,idNameMap);
                saveToServer();

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        }).setTitle("CardId: "+key);
        AlertDialog alert=alertDialogBuilder.create();
        alert.show();

    }

    public void showInputSearchDialog() {

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View promptView = inflater.inflate(R.layout.search_input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);
        final EditText editSearchInput = (EditText) promptView.findViewById(R.id.editSearch);

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Log.v("OnClick 'ok' :", "");
                logType="";
                logType = editSearchInput.getText().toString();
                Log.v("OnClick type: ", "" + logType);
                new AsyncTaskLogGET( MainActivity.this,authString,logType).execute();

            }
        }).setTitle("Enter Username:");

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Log.v("Cancel", "");
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void updateList(HashMap<String, Boolean> idMap){

        persons=idMap;

        for(Map.Entry<String, Boolean> p: persons.entrySet()){
            System.out.println(p.getKey().toString()+" , "+p.getValue().toString());
        }

    }

    public void emptylist (){
        persons.clear();
        idNameMap.clear();
        updateIdAdapter(persons, idNameMap);
        saveToServer();
        errorToast("List Cleared!");

    }


    public void errorToast(String txt){
        Snackbar snackbar = Snackbar
                .make(this.navigationView, txt,   Snackbar.LENGTH_LONG);

        snackbar.show();
    }


    public void updateIdAdapter(HashMap<String,Boolean> idMap,  HashMap<String, String> idNameMap){
        idfragment.updateAdapter(idMap,idNameMap);
    }

    public void updateLogAdapter(ArrayList<LogInfo> logList){

        loggfragment.updateAdapter(logList);
    }


    public void showPopUp(View v, String key) {

        this.key=key;
        PopupMenu popupMenu=new PopupMenu(this, v);
        MenuInflater inflator=popupMenu.getMenuInflater();
        popupMenu.setOnMenuItemClickListener(MainActivity.this);

        inflator.inflate(R.menu.long_click_popup, popupMenu.getMenu());

        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case(R.id.edit_cardname):
                showInputDialog(key);
                return true;

            case(R.id.remove_card):
                persons = idFragment.getIdMap();
                idNameMap = idFragment.getIdNameMap();
                persons.remove(key);
                idNameMap.remove(key);
                updateIdAdapter(persons,idNameMap);
                saveToServer();
                return true;
            default: return false;
        }
    }

    public void setCredentials(String authString,String usernameLabel) {
        this.authString=authString;
        this.usernameLabel=usernameLabel;



    }


    public void unlock(){
        AsyncTaskUnlock unlock = new AsyncTaskUnlock(authString);
        unlock.execute();
        errorToast("Door Have Been Unlocked!");
    }

    public void saveToServer(){
        persons = idFragment.getIdMap();
        idNameMap = idFragment.getIdNameMap();
        AsyncTaskPOST post = new AsyncTaskPOST(persons, idNameMap,authString);
        post.execute();
        errorToast("List Sent to Server!");
    }

    public void getIdList(){
        new AsyncTaskGET(this, authString).execute();
    }

    public void getLoggList(){
       new AsyncTaskLogGET(this,authString,"").execute();
    }



}
