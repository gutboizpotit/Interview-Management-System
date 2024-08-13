package btl.g4.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import btl.g4.mock.dto.BaseMessageDto;
import btl.g4.mock.dto.PaginationDto;
import btl.g4.mock.entity.User;
import btl.g4.mock.service.UserService;
import btl.g4.mock.util.SecurityUtil;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String getAll(@RequestParam(name = "text", required = false) String text,
                         @RequestParam(name = "page", defaultValue = "0") Integer page,
                         @RequestParam(name = "status", required = false) Integer status,
                         @RequestParam(name = "message", required = false) String message,
                         Model model) {
        PaginationDto<User> userPage = userService.getAll(text, page, 10);
        model.addAttribute("userPage", userPage);
        model.addAttribute("page", page);

        String username = SecurityUtil.getUsername().orElse("");
        model.addAttribute("username", username);

        model.addAttribute("status", status);
        model.addAttribute("message", message);
        model.addAttribute("text", text);
        return "/UserManagement/user";
    }

    @GetMapping("/detail")
    public String getAll(@RequestParam(name = "id") Integer id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        return "/UserManagement/user-detail";
    }

    @GetMapping("/create")
    public String create(@RequestParam(name = "status", required = false) Integer status,
                         @RequestParam(name = "message", required = false) String message,
                         Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getAllRole());

        model.addAttribute("status", status);
        model.addAttribute("message", message);
        return "/UserManagement/create-user";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        System.out.println("Creating user: " + user.toString());

        BaseMessageDto resp = userService.saveUser(user);

        if (resp != null) {
            redirectAttributes.addAttribute("status", resp.getStatus());
            redirectAttributes.addAttribute("message", resp.getMessage());
            return "redirect:/user/create";
        } else {
            redirectAttributes.addAttribute("status", 1);
            redirectAttributes.addAttribute("message", "User đã được tạo thành công");
        }
        return "redirect:/user";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam(name = "id") Integer id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.getAllRole());
        return "/UserManagement/edit-user";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        BaseMessageDto resp = userService.updateUser(user);

        if (resp != null) {
            redirectAttributes.addAttribute("status", resp.getStatus());
            redirectAttributes.addAttribute("message", resp.getMessage());
        } else {
            redirectAttributes.addAttribute("status", 1);
            redirectAttributes.addAttribute("message", "User đã cập nhật thành công");
        }
        return "redirect:/user";
    }
}
