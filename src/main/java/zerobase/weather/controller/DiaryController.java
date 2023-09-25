package zerobase.weather.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @ApiOperation(value = "일기 텍스트와 날씨 DB에저장",notes = "이것은 설명")
    @PostMapping("/create/diary")
    public void createDiary(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date
            , @RequestBody String text){
        diaryService.createDiary(date,text);
    }

    @ApiOperation("선택한 날짜의 모든 일기 데이터 가져오기")
    @GetMapping("/read/diary")
    public List<Diary> readDiary(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date){
        List<Diary> diary = diaryService.getDiary(date);
        return diary;
    }

    @ApiOperation("선택한 날짜 사이의 모든 일기 데이터 가져오기")
    @GetMapping("/read/diaries")
    public List<Diary>  readDiaries(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)@ApiParam(value = "날짜 형식 : 조회할 기간의 첫번째 날",example = "2020-02-02") LocalDate startdate,
                            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)@ApiParam(value = "날짜 형식 : 조회할 기간의 마지막 날",example = "2020-02-02") LocalDate endtdate){
        List<Diary> diaries = diaryService.getDiaries(startdate, endtdate);
        return diaries;
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
