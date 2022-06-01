package pl.witkowski.zticache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class ZtiCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZtiCacheApplication.class, args);
    }

}
