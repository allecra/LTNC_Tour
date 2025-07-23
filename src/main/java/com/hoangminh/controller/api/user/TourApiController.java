package com.hoangminh.controller.api.user;

import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.dto.TourDTO;
import com.hoangminh.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tours")
public class TourApiController {

    @Autowired
    private TourService tourService;

    @GetMapping("/season/{seasonId}")
    public ResponseEntity<ResponseDTO> getToursBySeason(@PathVariable Long seasonId) {
        List<TourDTO> tours = tourService.findBySeason(seasonId);
        return ResponseEntity.ok(new ResponseDTO("Lấy danh sách tour theo mùa thành công", tours));
    }

    @GetMapping("/month/{month}")
    public ResponseEntity<ResponseDTO> getToursByMonth(@PathVariable int month) {
        List<TourDTO> tours = tourService.findByMonth(month);
        return ResponseEntity.ok(new ResponseDTO("Lấy danh sách tour theo tháng thành công", tours));
    }
}