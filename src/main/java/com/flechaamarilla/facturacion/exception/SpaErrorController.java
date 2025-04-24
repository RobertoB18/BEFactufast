package com.flechaamarilla.facturacion.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaErrorController implements ErrorController {
    //This controller, solves throubles with redirects, solves the angular routes with springboot
    @RequestMapping("/error")
    public String handleError() {
        return "forward:/index.html";
    }
}