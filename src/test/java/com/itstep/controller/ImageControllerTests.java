package com.itstep.controller;

import com.itstep.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void saveImageIOExceptionTest() throws Exception {
        CustomMockMultipartFile mockFile = new CustomMockMultipartFile(
                "file",
                "test-image.jpg",
                IMAGE_JPEG_VALUE,
                "some fake content".getBytes()
        );

        mockMvc.perform(multipart("/image/{expenseId}", 1)
                        .file(mockFile)
                        .contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteImageTest() throws Exception {
        doNothing().when(imageService).delete(any());

        mockMvc.perform(delete("/image/{expenseId}", 1))
                .andExpect(status().isAccepted());
    }

    @Test
    public void getImageTest() throws Exception {
        byte[] image = "dummy image content".getBytes();
        when(imageService.getImage(any())).thenReturn(image);

        MvcResult mvcResult = mockMvc.perform(get("/image/{expenseId}", 1))
                .andExpect(status().isOk())
                .andReturn();

        byte[] response = mvcResult.getResponse().getContentAsByteArray();

        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(image);

        verify(imageService, times(1)).getImage(any());
    }

    @Test
    public void getImageNotFoundTest() throws Exception {
        doThrow(IOException.class).when(imageService).getImage(any());

        mockMvc.perform(get("/image/{expenseId}", 1))
                .andExpect(status().isNotFound());

        verify(imageService, times(1)).getImage(any());
    }

    private static class CustomMockMultipartFile extends MockMultipartFile {
        public CustomMockMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            super(name, originalFilename, contentType, content);
        }

        @Override
        public byte[] getBytes() throws IOException {
            throw new IOException();
        }
    }
}
