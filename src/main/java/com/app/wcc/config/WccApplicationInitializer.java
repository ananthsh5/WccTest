package com.app.wcc.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.app.wcc.WccAppLauncher;

/**
 * @author Ananth Shanmugam
 * Class to define configuration for spring boot
 */
@SpringBootApplication
public class WccApplicationInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WccAppLauncher.class);
    }

}
