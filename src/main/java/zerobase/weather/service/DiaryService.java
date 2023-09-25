package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.error.InvalidDate;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;

    @Value("${openweathermap.key}")
    private String apiKey;


    private final DateWeatherRepository dateWeatherRepository;

    //매일새벽 1시마다 작동
    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveDateWeather(){
        dateWeatherRepository.save(getWeatherFromApi());
    }

    //날씨 데이터 가져오기
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date,String text){
/*        //open weather map에서 날씨 데이터 가져오기 (API통해서)
        String weatherString = getWeatherString();

        //받아온 날씨 json파싱하기
        Map<String, Object> parseWeather = parseWeather(weatherString);
        log.info("weather test ={}",parseWeather);*/
        log.info("started tp create diary");
        //날씨 데이터 가져오기(DB에서)
        DateWeather dateWeather = getDateWeather(date);
        //파싱된 데이터 + 일기 값 우리 db 넣기
        Diary nowDiary=new Diary().builder()
                .text(text)
                .date(date)
                .build();
        nowDiary.setDateWeather(dateWeather);

        diaryRepository.save(nowDiary);
        log.info("end to create diary");
    }
    //내가 쓴 다이어리 가져오기
    @Transactional(readOnly = true)
    public List<Diary> getDiary(LocalDate date){
        if(date.isAfter(LocalDate.ofYearDay(3050,1))){
            throw new InvalidDate();
        }
        List<Diary> readDiaries = diaryRepository.findAllByDate(date);
        return  readDiaries;
    }
    @Transactional(readOnly = true)
    public List<Diary> getDiaries(LocalDate startdate, LocalDate endtdate) {
        List<Diary> allByDateBetween = diaryRepository.findAllByDateBetween(startdate, endtdate);
        return allByDateBetween;
    }


    private DateWeather getDateWeather(LocalDate date){
        List<DateWeather> allByDatefromDB = dateWeatherRepository.findAllByDate(date);
        if(allByDatefromDB.size()==0){
            //새로 API에서 날씨 정보를 가져와야함
           return getWeatherFromApi();
        }else{
            return allByDatefromDB.get(0);
        }
    }
    private DateWeather getWeatherFromApi(){
        //open weather map에서 날씨 데이터 가져오기
        String weatherString = getWeatherString();

        //받아온 날씨 json파싱하기
        Map<String, Object> parseWeather = parseWeather(weatherString);
        DateWeather dateWeather = DateWeather.builder()
                .date(LocalDate.now())
                .weather(parseWeather.get("main").toString())
                .icon(parseWeather.get("icon").toString())
                .temperature((double)parseWeather.get("temp"))
                .build();

        return dateWeather;
    }

    private String getWeatherString(){
        String apiUrl="https://api.openweathermap.org/data/2.5/weather?q=seoul&appid="+apiKey;
        try{

            URL url=new URL(apiUrl);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode=con.getResponseCode();
            BufferedReader br;
            if(responseCode==200){
                br=new BufferedReader(new InputStreamReader(con.getInputStream()));
            }else{
                br=new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder response=new StringBuilder();
            while((inputLine=br.readLine())!=null){
                response.append(inputLine);
            }

            br.close();

            return response.toString();
        }catch (Exception e){
            return "failed get reponse";
        }
    }

    private Map<String,Object> parseWeather(String jsonString){
        JSONParser jsonParser=new JSONParser();
        JSONObject jsonObject;

        try{
            jsonObject=(JSONObject) jsonParser.parse(jsonString);


        }catch (ParseException e){
            throw new RuntimeException(e);
        }

        Map<String,Object> resultMap=new HashMap<>();
        JSONObject mainData=(JSONObject) jsonObject.get("main");
        resultMap.put("temp",mainData.get("temp"));
        JSONArray weatherData= (JSONArray) jsonObject.get("weather");
        JSONObject weatherParse= (JSONObject) weatherData.get(0);
        resultMap.put("main",weatherParse.get("main"));
        resultMap.put("icon",weatherParse.get("icon"));
        return resultMap;
    }



    public void updateDiary(LocalDate date, String text) {
        Diary firstByDate = diaryRepository.findFirstByDate(date);
        firstByDate.setText(text);
        diaryRepository.save(firstByDate);
    }

    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }
}
