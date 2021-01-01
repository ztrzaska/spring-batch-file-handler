package com.ztrzaska.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import com.ztrzaska.model.User;
import com.ztrzaska.repository.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDatabaseWriter implements ItemWriter<User> {

    private final UserRepository userRepository;

    @Override
    public void write(List<? extends User> users) {
        userRepository.saveAll(users);
        log.info("Users saved to database");
    }
}
