package com.moviescloud.movies.controllers;

import com.moviescloud.movies.entities.Profession;
import com.moviescloud.movies.entities.Response;
import com.moviescloud.movies.services.IProfessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profession")
public class ProfessionController {

    IProfessionService professionService;

    @Autowired
    public ProfessionController(IProfessionService professionService) {
        this.professionService = professionService;
    }

    @GetMapping
    public Response<Profession> getAll(@RequestParam(name ="page", required = false, defaultValue = "0")  int page,
                                @RequestParam(name = "size", required = false, defaultValue = "10") int pageSize,
                                @RequestParam(name = "order", required = false, defaultValue = "id") String order) {
        Page<Profession> pages = professionService.findAll(PageRequest.of(page, pageSize, Sort.by(order)));
        return new Response<>(HttpStatus.OK, pages.getContent(), pages.getTotalElements(), pages.getTotalPages());
    }

    @GetMapping("/{id}")
    Profession getById(@PathVariable long id) {
        return professionService.findById(id);
    }

    @PostMapping
    Profession add(@RequestBody Profession profession) {
        return professionService.save(profession);
    }

    @PutMapping
    Profession edit(@RequestBody Profession profession) {
        return professionService.save(profession);
    }

    @DeleteMapping
    ResponseEntity<?> delete(@RequestBody Profession profession) {
        professionService.delete(profession);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable long id) {
        professionService.delete(professionService.findById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
