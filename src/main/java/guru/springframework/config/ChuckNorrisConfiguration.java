package guru.springframework.config;

import guru.springframework.norris.chuck.ChuckNorrisInfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChuckNorrisConfiguration {

    @Bean
    public ChuckNorrisInfoContributor chuckNorrisInfoContributor() {
        return new ChuckNorrisInfoContributor();
    }
}
