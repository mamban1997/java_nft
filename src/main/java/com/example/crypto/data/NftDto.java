package com.example.crypto.data;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class NftDto {
    private MultipartFile file;
    private String name;
    private String description;

}
