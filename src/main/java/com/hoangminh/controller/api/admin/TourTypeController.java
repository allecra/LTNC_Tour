package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.TourTypeDTO;
import com.hoangminh.entity.TourType;
import com.hoangminh.service.TourTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tour-type")
public class TourTypeController {
    @Autowired
    private TourTypeService tourTypeService;

    @GetMapping("")
    public List<TourTypeDTO> getAll() {
        return tourTypeService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TourTypeDTO getById(@PathVariable Integer id) {
        Optional<TourType> tourType = tourTypeService.findById(id);
        return tourType.map(this::toDTO).orElse(null);
    }

    @PostMapping("")
    public TourTypeDTO create(@RequestBody TourTypeDTO dto) {
        TourType entity = toEntity(dto);
        return toDTO(tourTypeService.save(entity));
    }

    @PutMapping("/{id}")
    public TourTypeDTO update(@PathVariable Integer id, @RequestBody TourTypeDTO dto) {
        dto.setId(id);
        TourType entity = toEntity(dto);
        return toDTO(tourTypeService.save(entity));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        tourTypeService.deleteById(id);
    }

    private TourTypeDTO toDTO(TourType entity) {
        TourTypeDTO dto = new TourTypeDTO();
        dto.setId(entity.getId());
        dto.setTenLoai(entity.getTenLoai());
        dto.setMoTa(entity.getMoTa());
        return dto;
    }

    private TourType toEntity(TourTypeDTO dto) {
        TourType entity = new TourType();
        entity.setId(dto.getId());
        entity.setTenLoai(dto.getTenLoai());
        entity.setMoTa(dto.getMoTa());
        return entity;
    }
}