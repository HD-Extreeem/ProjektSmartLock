package com.hellomicke89gmail.projektsmartlock;

/**
 * Created by hello on 2016-04-18.
 */
import java.util.HashMap;

public class IdMap {
    private HashMap<String, Boolean> personlist=new HashMap<>();

    public IdMap(){

    }

    public HashMap<String,Boolean> getList(){
        return this.personlist;
    }


    public void addId(String name,Boolean approved){
        personlist.put(name, true);
    }

    public void clearList(){
        personlist.clear();
    }
  /*  public void remove(HashMap<String,String> persons){
        Iterator it=persons.entrySet().iterator();
        while(it.hasNext()){


        }

    }*/



    public void updateList(HashMap<String, Boolean> idMap){
        this.personlist=idMap;
    }

}


