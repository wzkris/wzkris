package com.wzkris.track;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication
public class TrackServerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TrackServerApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.setApplicationStartup(new BufferingApplicationStartup(2048));
        app.run(args);
    }

}
