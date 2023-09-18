package com.powercoffee.controller;

import com.powercoffee.model.User;
import com.powercoffee.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {

        // Cree un objeto `Pageable` que defina los parámetros de paginación.
        Pageable pageable = PageRequest.of(page, size);

        // Devuelva el objeto `Page` de la consulta en la respuesta HTTP.
        Page<User> users = userService.getAllUsers(pageable);

        // Añada un campo `_links` al objeto `Page`.
        CollectionModel<User> model = CollectionModel.of(users);
        model.add(linkTo(methodOn(this.getClass()).getAllUsers(page + 1, size)).withRel("next"));
        model.add(linkTo(methodOn(this.getClass()).getAllUsers(page - 1, size)).withRel("prev"));
        model.add(linkTo(methodOn(this.getClass()).getAllUsers(0, size)).withRel("first"));
        model.add(linkTo(methodOn(this.getClass()).getAllUsers(users.getTotalPages() - 1, size)).withRel("last"));

        return users;
    }

}
