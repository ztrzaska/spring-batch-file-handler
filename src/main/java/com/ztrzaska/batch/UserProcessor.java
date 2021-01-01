package com.ztrzaska.batch;

import com.ztrzaska.model.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Component
public class UserProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(User user) {
        return user;
    }
}
