package cr.ac.una.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cr.ac.una.app.model.Usuario;

@Controller
@RequestMapping(path = "/view")
public class ViewController {
    
    @GetMapping("account")
    public String getAccountPage(Model model) {
        Usuario loggedUser = Usuario.getLoggedUser();
        model.addAttribute("usuario", loggedUser);
        return "view/account";
    }

}