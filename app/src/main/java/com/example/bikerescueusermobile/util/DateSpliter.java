package com.example.bikerescueusermobile.util;

import lombok.Data;

@Data
public class DateSpliter {
    private int date;
    private int month;
    private int year;

    public DateSpliter(String strDate) {
        String[] fromDateSplit = strDate.split("-");
        this.year = Integer.parseInt(fromDateSplit[2]);
        this.month = Integer.parseInt(fromDateSplit[1]);
        this.date = Integer.parseInt(fromDateSplit[0]);
    }
}
