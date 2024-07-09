package com.sparta.catube.controller;

import com.sparta.catube.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bill")
public class BillController {

    private final BillService billService;

    @PostMapping("/save-daily")
    public void saveDailyBill() throws Exception {
        billService.calculateVideoAmount();
        billService.calculateAdAmount();
    }

    @GetMapping("/search-daily")
    public ResponseEntity<List<Object>> searchDailyBill() throws Exception {
        List<Object> dailyBill = billService.searchDailyTotalAmount();
        return ResponseEntity.status(HttpStatus.OK).body(dailyBill);
    }

    @GetMapping("/search-weekly")
    public ResponseEntity<List<Object>> searchWeeklyBill() throws Exception {
        List<Object> weeklyBill = billService.searchWeeklyTotalAmount();
        return ResponseEntity.status(HttpStatus.OK).body(weeklyBill);
    }

    @GetMapping("/search-monthly")
    public ResponseEntity<List<Object>> searchMonthlyBill() throws Exception {
        List<Object> monthlyBill = billService.searchMonthlyTotalAmount();
        return ResponseEntity.status(HttpStatus.OK).body(monthlyBill);
    }
}
