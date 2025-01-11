package de.hka.ws2425.utils;

public class Routes {

    private String agency_id;
    private String route_id;
    private String route_short_name;
    private String route_long_name;
    private int route_type;
    private String route_color;         //evtl. falscher Typ
    private String route_text_color;    //evtl. falscher Typ

    private Routes(String agencyId,String routeId,String routeShortName,String routeLongName,int routeType,String routeColor,String routeTextColor){
        this.agency_id = agencyId;
        this.route_id = routeId;
        this.route_short_name = routeShortName;
        this.route_long_name = routeLongName;
        this.route_type = routeType;
        this.route_color = routeColor;
        this.route_text_color = routeTextColor;
    }

    public String getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(String agency_id) {
        this.agency_id = agency_id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getRoute_short_name() {
        return route_short_name;
    }

    public void setRoute_short_name(String route_short_name) {
        this.route_short_name = route_short_name;
    }

    public String getRoute_long_name() {
        return route_long_name;
    }

    public void setRoute_long_name(String route_long_name) {
        this.route_long_name = route_long_name;
    }

    public int getRoute_type() {
        return route_type;
    }

    public void setRoute_type(int route_type) {
        this.route_type = route_type;
    }

    public String getRoute_color() {
        return route_color;
    }

    public void setRoute_color(String route_color) {
        this.route_color = route_color;
    }

    public String getRoute_text_color() {
        return route_text_color;
    }

    public void setRoute_text_color(String route_text_color) {
        this.route_text_color = route_text_color;
    }
}
