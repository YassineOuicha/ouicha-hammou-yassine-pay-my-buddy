package pay_my_buddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pay_my_buddy.model.User;
import pay_my_buddy.service.UserService;

import java.util.Optional;

@RequestMapping("/friends")
@Controller
public class FriendController {

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String showAddFriendPage(){
        return "friend";
    }

    @PostMapping("/add")
    public String addFriend(@RequestParam("email") String email, Model model){
        User currentUser = userService.getConnectedUser();
        if(currentUser == null){
            return "redirect:/login";
        }
        Optional<User> friendOpt = userService.findByEmail(email);
        if(friendOpt.isPresent()){
            userService.addFriend(currentUser.getId(), friendOpt.get().getId());
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "No corresponding user for this email, please enter a valid email!");
            return "friend";
        }
    }
}
