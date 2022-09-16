package com.edu.ulab.app.config;

import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.storage.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Storage<UserEntity> userStorage(){
        return new Storage<UserEntity>();
    }

    @Bean
    public Storage<BookEntity> bookStorage(){
        return new Storage<BookEntity>();
    }
}
