package com.coradec.app.coram;

import com.coradec.coradeck.com.module.CoraComImpl;
import com.coradec.coradeck.conf.module.CoraConfImpl;
import com.coradec.coradeck.dir.model.module.CoraModules;
import com.coradec.coradeck.text.module.CoraTextImpl;
import com.coradec.coradeck.type.module.impl.CoraTypeImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.vaadin.artur.helpers.LaunchUtil;

import java.util.Locale;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        CoraModules.INSTANCE.register(
                new CoraConfImpl(),
                new CoraComImpl(),
                new CoraTextImpl(),
                new CoraTypeImpl()
        );
        Locale.setDefault(new Locale("de", "CH"));
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(Application.class, args));
    }

}
