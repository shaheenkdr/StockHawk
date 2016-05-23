package com.sam_chordas.android.stockhawk.data;

/**
 * Created by oblivion on 5/23/2016.
 */

public class EventBusValue
{
    private static int VALUE ;

    public EventBusValue(int val)
    {
        this.VALUE = val;
    }

    public static int getVal()
    {
        return VALUE;
    }
}
