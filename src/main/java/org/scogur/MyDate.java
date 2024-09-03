package org.scogur;

import java.util.Date;

import static java.lang.Math.abs;

public class MyDate {
    //dd.mm.yy
    private int[] date = new int[3];
    //hh.mm.ss
    private int[] time = new int[2];

    enum month {

    }
    public MyDate(String str) {
        this.date = dateFromString(str);
        this.time = timeFromString("0:0");
    }

    public int[] getDate() {
        return date;
    }

    public void setTime(int[] time) {
        this.time = time;
    }

    public void setTime(String time) {
        this.time = timeFromString(time);
    }

    @Override
    public String toString() {
        String str = Integer.toString(date[0]);
        for (int i = 1; i<3; i++){
            str = String.join(".", str, Integer.toString(date[i]));
        }
        str += " " + time[0];
        for (int i = 1; i<2; i++){
            str = String.join(":", str, Integer.toString(time[i]));
        }
        return str;
    }

    private int[] dateFromString(String str){
        String[] sa = str.split("\\.");
        int[] ar = new int[3];
        for (int i =0; i<3;i++){
            ar[i] = Integer.parseInt(sa[i]);
        }
        return ar;
    }

    private int[] timeFromString(String str){
        String[] sa = str.split(":");
        int[] ar = new int[2];
        for (int i =0; i<2;i++){
            ar[i] = Integer.parseInt(sa[i]);
        }
        return ar;
    }

    public static long subtraction(MyDate date1, MyDate date2){
        Date d1 = new Date(date1.date[2], date1.date[1], date1.date[0], date1.time[0], date1.time[1]);
        Date d2 = new Date(date2.date[2], date2.date[1], date2.date[0], date2.time[0], date2.time[1]);

        //(date1 milliseconds - date2 milliseconds)/ amount of milliseconds in minute = subtraction in minutes
        return abs(d1.getTime()-d2.getTime())/60000;
    }
}
