package zerobase.weather.domain;

import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diary {

    @Id @GeneratedValue
    private int id;

    private String weather;

    private String icon;

    private double temperature;

    private String text;

    @CreatedDate
    private LocalDate date;

    public void setDateWeather(DateWeather dateWeather){
        this.date=dateWeather.getDate();
        this.weather=dateWeather.getWeather();
        this.icon=dateWeather.getIcon();
        this.temperature=dateWeather.getTemperature();
    }

}
