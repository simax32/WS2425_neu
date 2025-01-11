package de.hka.ws2425.utils;

public class Stop {
        private String stop_id;
        private String stop_name;
        private double stop_lat;
        private double stop_lon;


        public Stop(String stopId, String stopName, double stopLat, double stopLon) {
            this.stop_id = stopId;
            this.stop_name = stopName;
            this.stop_lat = stopLat;
            this.stop_lon = stopLon;
        }

        public String getStop_id() {
            return stop_id;
        }

        public String getStop_name() {
            return stop_name;
        }

        public double getStop_lat() {
            return stop_lat;
        }

        public double getStop_lon() {
            return stop_lon;
        }
    }

