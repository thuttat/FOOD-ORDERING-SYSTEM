package duckie.example.backend.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dyupzyqwj");
        config.put("api_key", "497522642724389");
        config.put("api_secret", "1qiLwjHVPTBX9_BYZKsRB2FfWJA");
        return new Cloudinary(config);
    }
}
