package com.hellomicke89gmail.projektsmartlock;

import java.util.Map;
import java.util.Objects;

/**
 * Created by hello on 2016-04-18.
 */


public class Person {
    String key;
    String name;
    private boolean approved;

    Person(String key,String name, boolean approved) {
        this.key = key;
        this.approved=approved;
        this.name=name;

    }

    public boolean isSelected() {
        return approved;
    }

    public void setSelected(boolean approved) {
        this.approved = approved;
    }


    public String getKey() {
        return this.key;
    }

    public boolean isApproved() {
        return approved;
    }

    public String getName() {
        return this.name;
    }

    public void setApproved(Boolean approved){
        this.approved=approved;
    }



    public void setName(String name) {
        this.name=name;

    }

    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.




}
