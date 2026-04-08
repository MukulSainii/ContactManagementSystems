package com.smart.controller;

import com.smart.DTO.UserRegisterDTO;
import com.smart.enums.Gender;
import com.smart.helper.ImageUtil;
import com.smart.helper.Message;
import com.smart.service.serviceInterface.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RequiredArgsConstructor
@Controller
public class HomeController {
    private final UserService userService;
    // Home page mapping
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home-page manager");
        return "home";
    }

    // About page mapping
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About-page manager");
        return "about";
    }

    // Signup page mapping
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "signup-page manager");
        model.addAttribute("user", new UserRegisterDTO());
        model.addAttribute("genderList",Gender.values());
        return "signup";
    }

    // Handling user registration
    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") UserRegisterDTO userRegisterDTO, BindingResult result, Model model,
                               @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
                               HttpSession session,@RequestParam("profileImage") MultipartFile file)
    {
        if (!agreement) {
            result.rejectValue("agreement", "You must accept Terms & Conditions");
        }
        if(result.hasErrors()){
            model.addAttribute("genderList",Gender.values());
            return "/signup";
        }
        if(file != null && !file.isEmpty()){
            String fileName = ImageUtil.uploadImage(file);
            userRegisterDTO.setImageURL(fileName);
        }
        userService.saveUser(userRegisterDTO);
        session.setAttribute("message", new Message("Successfully registered!", "alert-success"));
        return "redirect:/signup";
    }

    // Custom login handler
    @GetMapping("/signin")
    public String customLogin(Model model) {
        model.addAttribute("title", "Login page");
        return "login";
    }
}
