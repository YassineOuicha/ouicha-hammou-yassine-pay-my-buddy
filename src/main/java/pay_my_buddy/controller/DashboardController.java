package pay_my_buddy.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pay_my_buddy.model.User;
import pay_my_buddy.service.TransactionService;
import pay_my_buddy.service.UserService;

import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        User connectedUser = userService.getConnectedUser();
        if (connectedUser!=null){
            model.addAttribute("user", connectedUser);
            model.addAttribute("transactions", transactionService.getTransactionsForUser(connectedUser.getId()));
            model.addAttribute("friends", connectedUser.getFriends());
        }
        return "dashboard";
    }

    @PostMapping("/dashboard/payment")
    public String handlePayment(@RequestParam("receiverId") long receiverId,
                                @RequestParam("description") String description,
                                @RequestParam("amount") double amount,
                                Model model){
        User sender = userService.getConnectedUser();

        if(sender == null){
            model.addAttribute("error", "User not connected");
            return "dashboard";
        }
        Optional<User> receiverOpt = userService.findById(receiverId);
        if(receiverOpt.isEmpty()){
            model.addAttribute("error", "receiver not found");
            return "dashboard";
        }

        User receiver = receiverOpt.get();

        try {
            transactionService.createTransaction(sender.getId(), receiver.getId(), description, amount);
            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error during payment : " + e.getMessage());
            return "dashboard";
        }
    }
}
