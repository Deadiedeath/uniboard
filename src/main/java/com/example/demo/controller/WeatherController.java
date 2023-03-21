package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.APIService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/weather")
@RequiredArgsConstructor
@Log4j2
public class WeatherController {

	private final APIService apiService;
	
	@GetMapping("/page")
	public String weather(Model model, HttpServletResponse response, HttpServletRequest request){
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			log.info("로그인이 필요합니다");
			return "redirect:/user/login";
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMdd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
		
		Calendar cal = Calendar.getInstance();//현재시간에서 1시간을 빼는 작업
		cal.add(Calendar.HOUR, -1);
		
		Date now = new Date(cal.getTimeInMillis());
	
		String date = dateFormat.format(now);//날짜를 yyyyMMdd로 바꾸는 작업
		String time = timeFormat.format(now).substring(0,2)+"00";//시간을 정시로 바꿈
		
//		System.out.println(date);
//		System.out.println(time);
		
		JSONObject currentWeather = null;
		try {
			currentWeather = apiService.todayWeather(date, time);
		} catch (IOException e) {
//			e.printStackTrace();
			return "redirect:/board/list";
		}
		currentWeather.put("date", date);//날짜 시간을 json에 삽입
		currentWeather.put("time", time);
//		System.out.println(currentWeather);
		model.addAttribute("weather", currentWeather);
        return "/weather/page";
		
	}
}
