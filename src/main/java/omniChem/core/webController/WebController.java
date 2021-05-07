package omniChem.core.webController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import omniChem.core.security.UserRepository;
import omniChem.core.security.User;


import java.util.List;

@Controller
public class WebController {
    @Autowired
    private UserRepository userRepo;

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    /* handler method to show the user registration form(sign up) */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "signup_form";
    }

    /* handler method in the controller class to process registration */
    @PostMapping("/process_register")
    public String processRegister(User user) {

        /* we use BCryptPasswordEncoder to encode the user’s password so the password
        itself it not stored in database (for better security) –
        only the hash value of the password is stored.
         */
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepo.save(user);

        return "register_success";
    }



    /* implements the users and logout features */
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }

    @GetMapping("/home")
    public String viewHome() {
        return "home";
    }

}
