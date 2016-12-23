package com.example.eqoram.alpha;

import java.util.Comparator;

/**
 * Created by Stefan on 06.12.2016.
 */

public class TimeStampComparatorAbwaerts implements Comparator<Message> {
    public int compare (Message m1, Message m2){
        return m2.getTime().compareTo(m1.getTime());
    }
}
