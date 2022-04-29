package com.example.crypto.controllers;

import com.example.crypto.data.NftDto;
import com.example.crypto.data.NftService;
import com.example.crypto.security.service.UserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@Controller
public class NftController {
    private final UserService userService;
    private final NftService nftService;

    @Autowired
    public NftController(UserService userService, NftService nftService) {
        this.userService = userService;
        this.nftService = nftService;
    }

    @ModelAttribute
    private void addUser(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
    }

    @GetMapping("/publish")
    public String getPublishPage() {
        return "person/nft_publish";
    }

    @RequestMapping(value = "/upload_nft", method = RequestMethod.POST)
    public String uploadFile(NftDto nftDto) throws IOException {
        nftService.safeNewNft(nftDto);
        return "redirect:/";
    }

    @GetMapping(value = "/image/123", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public @ResponseBody
    byte[] getImage() throws IOException {

        File file = new File("C:\\Users\\bombe\\Desktop\\crypto_data\\storage\\loadFiles\\720_616d32dc82682c4d66f58f6e.jpg");
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        return IOUtils.toByteArray(in);
    }
}
