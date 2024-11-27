package de.hka.ws2425.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.util.UUID;

import de.hka.ws2425.api.RandomJokeAPI;
import de.hka.ws2425.api.RandomJoke;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> result;

    private MutableLiveData<RandomJoke> randomJokeResponse;

    public MainViewModel() {
        this.result = new MutableLiveData<>();
        this.randomJokeResponse = new MutableLiveData<>();
    }

    public void doSomething() {
        Log.d("MainViewModel", "Button clicked!");

        this.result.setValue("Some incredible good value!");
    }

    public void requestRandomJoke() {
        Log.d("MainViewModel", "Requesting random joke from API ...");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://official-joke-api.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RandomJokeAPI randomJokeAPI = retrofit.create(RandomJokeAPI.class);

        Call<RandomJoke> apiCall = randomJokeAPI.getRandomJoke();
        apiCall.enqueue(new Callback<RandomJoke>() {
            @Override
            public void onResponse(Call<RandomJoke> call, Response<RandomJoke> response) {
                randomJokeResponse.postValue(response.body());
            }

            @Override
            public void onFailure(Call<RandomJoke> call, Throwable t) {
                Log.e("MainViewModel", String.format("API Request Failed"));
            }
        });
    }

    public void sendMqttMessage(String topic, String message) {
        Mqtt5BlockingClient mqttClient = MqttClient.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost("test.mosquitto.org")
                .serverPort(1883)
                .useMqttVersion5()
                .buildBlocking();

        mqttClient.connect();
        mqttClient.publishWith().topic(topic).payload(message.getBytes()).send();
        mqttClient.disconnect();
    }

    public LiveData<String> getResult() {
        return this.result;
    }

    public LiveData<RandomJoke> getRandomJokeResponse() { return this.randomJokeResponse; }
}