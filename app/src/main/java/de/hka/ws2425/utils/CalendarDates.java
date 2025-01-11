package de.hka.ws2425.utils;

import java.time.LocalDate;

public class CalendarDates {
    private String service_id;
    private String date;
    private int exception_type;

    public CalendarDates(String serviceId, String date, int exceptionType){
        this.service_id = serviceId;
        this.date = date;
        this.exception_type = exceptionType;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getException_type() {
        return exception_type;
    }

    public void setException_type(int exception_type) {
        this.exception_type = exception_type;
    }
}
