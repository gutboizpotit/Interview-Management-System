package btl.g4.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import btl.g4.mock.dto.BaseMessageDto;
import btl.g4.mock.security.AuthService;
import btl.g4.mock.util.SecurityUtil;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private AuthService authService;

    @GetMapping
    public String home(Model model) {
        String username = SecurityUtil.getUsername().orElse("");
        model.addAttribute("username", username);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "/auth/login";
    }

    @GetMapping("/reset-password/request")
    public String requestResetPassword() {
        return "/auth/forgot";
    }

    @PostMapping("/reset-password/request")
    public String requestResetPassword(@RequestParam(name = "email") String email, Model model) {
        BaseMessageDto ret = authService.sendResetPasswordEmail(email);
        if(ret == null) {
            model.addAttribute("status", 1);
            model.addAttribute("message", "Username và new password đã được gửi qua mail.");
        } else {
            model.addAttribute("status", ret.getStatus());
            model.addAttribute("message", ret.getMessage());
        }
        return "/auth/login";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam(name = "email") String email,
                                @RequestParam(name = "secret") String secret) {
        return "/auth/login";
    }
}
