package com.itstep.controller;

import com.itstep.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
public class ImageControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ImageService imageService;

    @Test
    public void saveImageTest() throws Exception {

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test-image.jpg",
                IMAGE_JPEG_VALUE,
                "dummy image content".getBytes()
        );

        doNothing().when(imageService).save(any(), any(), any());

        mockMvc.perform(multipart("/image/{expenseId}", 1)
                        .file(mockFile)
                        .contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted());
    }

    @Test
    public void deleteImageTest() throws Exception {
        doNothing().when(imageService).delete(any());

        mockMvc.perform(delete("/image/{expenseId}", 1))
                .andExpect(status().isAccepted());
    }
}
