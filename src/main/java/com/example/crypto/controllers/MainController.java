package com.example.crypto.controllers;


import com.example.crypto.data.NftDto;
import com.example.crypto.data.NftEntity;
import com.example.crypto.data.NftService;
import com.example.crypto.exceptions.NftNotFoundException;
import com.example.crypto.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
        Page<NftEntity> pageOfNftEntity = nftService.getPageOfNftEntity(0, 2);
        model.addAttribute("nfts", pageOfNftEntity);
        return "main";
    }

    @GetMapping("/page/{number}")
    public String getPageOfArt(@PathVariable Integer number, Model model){
        Page<NftEntity> pageOfNftEntity = nftService.getPageOfNftEntity(number - 1, 2);
        model.addAttribute("nfts", pageOfNftEntity);
        return "main";
    }

    @GetMapping("/my")
    public String myPage(){
        return "person/my";
    }

}
