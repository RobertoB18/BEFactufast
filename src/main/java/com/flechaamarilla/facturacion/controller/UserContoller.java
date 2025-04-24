package com.flechaamarilla.facturacion.controller;

import com.flechaamarilla.facturacion.model.User;
import com.flechaamarilla.facturacion.pojo.Response;
import com.flechaamarilla.facturacion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
public class UserContoller extends BaseController{

    @Autowired
    private UserService userService;

    @GetMapping("/user/{rfc}")
    public ResponseEntity<Response> getUsuario(@PathVariable("rfc") String rfc){
        Optional<User> user;
        try {
            user = userService.getUser(rfc);
            if(!user.isPresent()){
                return new ResponseEntity<Response>(new Response(false, "Error usuario no encontrado " , null), HttpStatus.OK);
            }
            return new ResponseEntity<Response>(new Response(true, "Success", user.get()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Response>(new Response(false, "Error " + e.getMessage(), null), HttpStatus.OK);
        }
    }

    @PostMapping("/user")
    public ResponseEntity<Response> postUser(@RequestBody Map<String, Object> request){
        try {
            User userToSave = new User();
            userToSave.setRfc((String) request.get("rfc"));
            userToSave.setZip((Integer) request.get("zip"));
            userToSave.setEmail((String) request.get("email"));
            userToSave.setTaxregime((String) request.get("taxregime"));
            userToSave.setCompanyname((String) request.get("companyname"));
            userService.saveUser(userToSave);
            return new ResponseEntity<Response>(new Response(true, "Success", userToSave), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Response>(new Response(false, "Error " + e.getMessage(), null), HttpStatus.OK);
        }
    }

    @DeleteMapping("/user/{rfc}")
    public ResponseEntity<Response> deleteUser(@PathVariable("rfc") String rfc){
        try {
            userService.deleteUser(rfc);
            return new ResponseEntity<Response>(new Response(true, "Success", rfc), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Response>(new Response(false, "Error " + e.getMessage(), null), HttpStatus.OK);
        }
    }
}
