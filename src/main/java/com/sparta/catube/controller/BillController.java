package com.sparta.catube.controller;

import com.sparta.catube.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
