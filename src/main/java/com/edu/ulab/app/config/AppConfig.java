package com.edu.ulab.app.config;

import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.PersonEntity;
import com.edu.ulab.app.storage.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Storage<PersonEntity> userStorage(){
        return new Storage<PersonEntity>();
    }

    @Bean
    public Storage<BookEntity> bookStorage(){
        return new Storage<BookEntity>();
    }
}
