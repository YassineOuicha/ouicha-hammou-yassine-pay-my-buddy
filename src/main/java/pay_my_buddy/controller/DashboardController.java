package pay_my_buddy.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pay_my_buddy.model.User;
import pay_my_buddy.service.TransactionService;
import pay_my_buddy.service.UserService;

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
        }
        return "dashboard";
    }
}
