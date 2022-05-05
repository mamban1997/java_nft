package com.example.crypto.controllers;

import com.example.crypto.data.NftDto;
import com.example.crypto.data.NftEntity;
import com.example.crypto.data.NftService;
import com.example.crypto.exceptions.NftNotFoundException;
import com.example.crypto.security.service.UserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@Controller
@RequestMapping("/nft/")
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

    @GetMapping("/{numberOrAlias}")
    public String getPageOfArt(@PathVariable String numberOrAlias, Model model) throws NftNotFoundException {
        NftEntity nft = nftService.getNftByUniqNumberOrAlias(numberOrAlias);
        model.addAttribute("nft", nft);
        model.addAttribute("nftDto", nft.getDto());
        return "nft_page";
    }

    @RequestMapping(value = "/upload_nft", method = RequestMethod.POST)
    public String uploadFile(NftDto nftDto) throws Exception {
        NftEntity nft = nftService.safeNewNft(nftDto);
        return "redirect:/nft/" + nft.getNumberOrAlias();
    }


    @RequestMapping(value = "/{numberOrAlias}/edit", method = RequestMethod.POST)
    public String editNft(@PathVariable String numberOrAlias, NftDto nftDto) throws NftNotFoundException {
        NftEntity nft = nftService.getNftByUniqNumberOrAlias(numberOrAlias);
        nftService.updateNftDetails(nft, nftDto);
        return "redirect:/nft/" + nft.getNumberOrAlias();
    }
    @RequestMapping(value = "/{numberOrAlias}/remove", method = RequestMethod.POST)
    public String removeNft(NftEntity nft) {
        //nftService.updateNftDetails(nftDto);
        return "redirect:/";
    }


    @GetMapping(value = "/image/{uuid}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    @ResponseBody
    public byte[] getImage(@PathVariable String uuid) throws IOException {
        File file = new File("C:\\Users\\bombe\\Desktop\\crypto_data\\storage\\loadFiles\\" + uuid);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        return IOUtils.toByteArray(in);
    }
}
