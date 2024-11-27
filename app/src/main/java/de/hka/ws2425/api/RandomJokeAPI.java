package de.hka.ws2425.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RandomJokeAPI {

    @GET("random_joke")
    Call<RandomJoke> getRandomJoke();

}
