package zerobase.weather.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/create/diary")
    public void createDiary(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date
            , @RequestBody String text){
        diaryService.createDiary(date,text);
    }

    @GetMapping("/read/diary")
    public void readDiary(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date){
        diaryService.getDiary(date);
    }

    @GetMapping("/read/diaries")
    public void readDiaries(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startdate,
                            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endtdate){
        diaryService.getDiaries(startdate,endtdate);
    }

    @PutMapping("/update/diary")
    public void updateDiary(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date,@RequestBody String text){
        diaryService.updateDiary(date,text);

    }

    @DeleteMapping("/delete/diary")
    public void deleteDiary(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date){
        diaryService.deleteDiary(date);
    }
}
