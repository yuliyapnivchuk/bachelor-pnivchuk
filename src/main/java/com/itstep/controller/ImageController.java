package com.itstep.controller;

import com.itstep.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/{expenseId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SneakyThrows
    public void saveImage(@PathVariable Integer expenseId, @RequestParam("file") MultipartFile image) {
        imageService.save(expenseId, image.getOriginalFilename(), image.getBytes());
    }

    @DeleteMapping("/{expenseId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SneakyThrows
    public void deleteImage(@PathVariable Integer expenseId) {
        imageService.delete(expenseId);
    }
}
