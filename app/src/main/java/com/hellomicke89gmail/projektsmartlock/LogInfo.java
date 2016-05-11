package com.hellomicke89gmail.projektsmartlock;

import android.util.Log;

/**
 * Created by benjo on 2016-05-02.
 */
public class LogInfo {
   String name, date, status, time;


  // public LogInfo(String name,String date,String status, String time){
  //     this.name=name;
  //     this.date=date;
  //     this.status=status;
  //     this.time=time;
  // }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
