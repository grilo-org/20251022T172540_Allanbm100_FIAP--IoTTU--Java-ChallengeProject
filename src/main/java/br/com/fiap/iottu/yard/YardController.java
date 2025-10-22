package br.com.fiap.iottu.yard;

import br.com.fiap.iottu.helper.MessageHelper;
import br.com.fiap.iottu.user.User;
import br.com.fiap.iottu.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/yards")
public class YardController {

    @Autowired
    private YardService service;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageHelper messageHelper;

    @GetMapping
    public String listYards(Model model) {
        model.addAttribute("yards", service.findAll());
        return "yard/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model, @AuthenticationPrincipal UserDetails userDetails, @RequestParam(name = "origin", required = false) String origin) {
        Yard yard = new Yard();
        if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
            yard.setUser(user);
        }
        model.addAttribute("yard", yard);
        model.addAttribute("users", userService.findAll()); // Admins will use this
        model.addAttribute("origin", origin);
        return "yard/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Yard yard, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails, @RequestParam(name = "origin", required = false) String origin) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            model.addAttribute("origin", origin);
            return "yard/form";
        }
        // Ensure USERs can only create yards for themselves
        if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
            yard.setUser(user);
        }
        service.save(yard);
        redirectAttributes.addFlashAttribute("successMessage", messageHelper.getMessage("message.success.yard.created"));
        return "redirect:" + (origin != null && !origin.isEmpty() ? origin : "/yards");
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, @RequestParam(name = "origin", required = false) String origin) {
        model.addAttribute("yard", service.findById(id).orElseThrow());
        model.addAttribute("users", userService.findAll()); // Admins will use this
        model.addAttribute("origin", origin);
        return "yard/form";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute Yard yard, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails, @RequestParam(name = "origin", required = false) String origin) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            model.addAttribute("origin", origin);
            return "yard/form";
        }
        yard.setId(id);
        // Ensure USERs can only update their own yards
        if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
            yard.setUser(user);
        }
        service.save(yard);
        redirectAttributes.addFlashAttribute("successMessage", messageHelper.getMessage("message.success.yard.updated"));
        return "redirect:" + (origin != null && !origin.isEmpty() ? origin : "/yards");
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        service.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", messageHelper.getMessage("message.success.yard.deleted"));
        return "redirect:/yards";
    }

    @GetMapping("/{id}/map")
    public String showYardMap(@PathVariable Integer id, Model model) {
        Yard yard = service.findById(id).orElseThrow(() -> new IllegalArgumentException("{message.error.yard.invalid}" + id));
        YardMapDTO mapData = service.prepareYardMapData(id);
        model.addAttribute("yard", yard);
        model.addAttribute("mapData", mapData);

        return "yard/map";
    }
}