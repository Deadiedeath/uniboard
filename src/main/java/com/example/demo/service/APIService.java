package com.example.demo.service;

import java.io.IOException;

import org.json.simple.JSONObject;

public interface APIService {

	JSONObject todayWeather(String date, String time) throws IOException;
}
