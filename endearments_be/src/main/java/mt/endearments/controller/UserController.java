package mt.endearments.controller;

import mt.endearments.model.User;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    final private List<User> users = List.of(
            User.builder().id(1L).name("test1").email("testUser1@gmail.com").build(),
            User.builder().id(2L).name("test2").email("testUser2@gmail.com").build()
    );
}
