package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.SeasonDTO;
import com.hoangminh.entity.Season;
import com.hoangminh.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/season")
public class SeasonController {
    @Autowired
    private SeasonService seasonService;

    @GetMapping("")
    public List<SeasonDTO> getAll() {
        return seasonService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SeasonDTO getById(@PathVariable Integer id) {
        Optional<Season> season = seasonService.findById(id);
        return season.map(this::toDTO).orElse(null);
    }

    @PostMapping("")
    public SeasonDTO create(@RequestBody SeasonDTO dto) {
        Season entity = toEntity(dto);
        return toDTO(seasonService.save(entity));
    }

    @PutMapping("/{id}")
    public SeasonDTO update(@PathVariable Integer id, @RequestBody SeasonDTO dto) {
        dto.setId(id);
        Season entity = toEntity(dto);
        return toDTO(seasonService.save(entity));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        seasonService.deleteById(id);
    }

    private SeasonDTO toDTO(Season entity) {
        SeasonDTO dto = new SeasonDTO();
        dto.setId(entity.getId());
        dto.setTenMua(entity.getTenMua());
        dto.setThangBatDau(entity.getThangBatDau());
        dto.setThangKetThuc(entity.getThangKetThuc());
        return dto;
    }

    private Season toEntity(SeasonDTO dto) {
        Season entity = new Season();
        entity.setId(dto.getId());
        entity.setTenMua(dto.getTenMua());
        entity.setThangBatDau(dto.getThangBatDau());
        entity.setThangKetThuc(dto.getThangKetThuc());
        return entity;
    }
}