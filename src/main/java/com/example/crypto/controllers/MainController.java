package com.example.crypto.controllers;


import com.example.crypto.entities.NftEntity;
import com.example.crypto.services.NftService;
import com.example.crypto.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    private final UserService userService;
    private final NftService nftService;

    @Autowired
    public MainController(UserService userService, NftService nftService) {
        this.userService = userService;
        this.nftService = nftService;
    }

    @ModelAttribute
    private void addUser(Model model) {
         model.addAttribute("user", userService.getCurrentUser());
    }
    
    @GetMapping("/")
    public String mainPage(Model model){
        Page<NftEntity> pageOfNftEntity = nftService.getPageOfNftEntity(0, 12);
        model.addAttribute("nfts", pageOfNftEntity);
        return "main";
    }

    @GetMapping("/hello")
    public String testPage(){
        return "hello";
    }

    @GetMapping("/page/{number}")
    public String getPageOfNfts(@PathVariable Integer number, Model model){
        Page<NftEntity> pageOfNftEntity = nftService.getPageOfNftEntity(number - 1, 12);
        model.addAttribute("nfts", pageOfNftEntity);
        return "main";
    }

    @GetMapping("/my")
    public String myPage(){
        return "person/my";
    }

    @GetMapping("/my/nfts/{number}")
    public String getMyPageOfNfts(@PathVariable Integer number, Model model) {
        Page<NftEntity> pageOfNftEntity = nftService.getPageOfNftEntity(number - 1, 12);
        model.addAttribute("nfts", pageOfNftEntity);
        return "main";

    }
}
