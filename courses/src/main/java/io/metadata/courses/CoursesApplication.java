package io.metadata.courses;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class CoursesApplication
{
    @Configuration
    @Profile("prod")
    static class TimedConfiguration
    {
        @Bean
        public TimedAspect timedAspect(MeterRegistry registry)
        {
            return new TimedAspect(registry);
        }
    }

    public static void main(String[] args)
    {
        SpringApplication.run(CoursesApplication.class, args);
    }
}
