package com.example.eqoram.alpha;
import java.util.Comparator;

/**
 * Created by Stefan on 16.11.2016.
 */

public class TimeStampComparator implements Comparator<Message> {
    public int compare (Message m1, Message m2){
        return m1.getTime().compareTo(m2.getTime());
    }
}
