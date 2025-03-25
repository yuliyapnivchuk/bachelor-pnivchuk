package com.itstep.controller;

import com.itstep.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/{expenseId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void saveImage(@PathVariable Integer expenseId, @RequestParam("file") MultipartFile image) {
        try {
            imageService.save(expenseId, image.getOriginalFilename(), image.getBytes());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to read image", e);
        }
    }

    @DeleteMapping("/{expenseId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteImage(@PathVariable Integer expenseId) {
        imageService.delete(expenseId);
    }
}
