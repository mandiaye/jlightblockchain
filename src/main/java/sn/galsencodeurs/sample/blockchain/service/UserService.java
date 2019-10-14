package sn.galsencodeurs.sample.blockchain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import sn.galsencodeurs.sample.blockchain.model.User;

@Service
@Slf4j
public class UserService {

    @Getter
    private List<User> users;

    public UserService() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void print() {
        log.info("Block Chain users : {} \n\t", users);
    }
}
