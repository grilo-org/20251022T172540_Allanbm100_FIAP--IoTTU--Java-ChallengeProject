package br.com.fiap.iottu.tag;

import br.com.fiap.iottu.helper.MessageHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService service;

    @Autowired
    private MessageHelper messageHelper;

    @GetMapping
    public String listTags(Model model) {
        model.addAttribute("tags", service.findAll());
        return "tag/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model, @RequestParam(required = false) String redirectUrl) {
        model.addAttribute("tag", new Tag());
        model.addAttribute("redirectUrl", redirectUrl);
        return "tag/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Tag tag, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam(required = false) String redirectUrl) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("redirectUrl", redirectUrl);
            return "tag/form";
        }
        service.save(tag);
        redirectAttributes.addFlashAttribute("successMessage", "{message.success.tag.created}");
        return "redirect:" + (redirectUrl != null && !redirectUrl.isEmpty() ? redirectUrl : "/tags");
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, @RequestParam(required = false) String redirectUrl) {
        model.addAttribute("tag", service.findById(id).orElseThrow());
        model.addAttribute("redirectUrl", redirectUrl);
        return "tag/form";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute Tag tag, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam(required = false) String redirectUrl) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("redirectUrl", redirectUrl);
            return "tag/form";
        }
        tag.setId(id);
        service.save(tag);
        redirectAttributes.addFlashAttribute("successMessage", messageHelper.getMessage("message.success.tag.updated"));
        return "redirect:" + (redirectUrl != null && !redirectUrl.isEmpty() ? redirectUrl : "/tags");
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes, @RequestParam(required = false) String redirectUrl) {
        service.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", messageHelper.getMessage("message.success.tag.deleted"));
        return "redirect:" + (redirectUrl != null && !redirectUrl.isEmpty() ? redirectUrl : "/tags");
    }
}