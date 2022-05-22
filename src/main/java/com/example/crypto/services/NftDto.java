package com.example.crypto.services;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class NftDto {
    private MultipartFile file;
    private String nftName;

    @NotBlank(message = "The city is required.")
    private String description;
    private String UUID;
    private String alias;

    private Boolean Hidden;

    private Double instantBuyPrice;
}
