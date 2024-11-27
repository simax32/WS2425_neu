package de.hka.ws2425.api;

import com.google.gson.annotations.SerializedName;

public class RandomJoke {

    @SerializedName("id")
    private int id;

    @SerializedName("type")
    private String type;

    @SerializedName("setup")
    private String setup;

    @SerializedName("punchline")
    private String punchline;

    public RandomJoke() {

    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSetup() {
        return this.setup;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public String getPunchline() {
        return this.punchline;
    }

    public void setPunchline(String punchline) {
        this.punchline = punchline;
    }
}
