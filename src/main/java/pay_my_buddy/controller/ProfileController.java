package pay_my_buddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pay_my_buddy.model.User;
import pay_my_buddy.service.UserService;
import org.springframework.ui.Model;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profilePage(Model model){
        User user = userService.getConnectedUser();
        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("username") String username,
                                @RequestParam("email") String email,
                                @RequestParam("password") String password,
                                Model model){

        User user = userService.getConnectedUser();
        if (user == null){
            return "redirect:/login";
        }
        user.setUsername(username);
        user.setEmail(email);
        if (!password.isEmpty()) {
            user.setPassword(userService.getPasswordEncoder().encode(password));
        }
        userService.updateUser(user);

        model.addAttribute("user", user);
        model.addAttribute("success", "Votre profil a été mis à jour avec succès !");
        return "profile";
    }

}
