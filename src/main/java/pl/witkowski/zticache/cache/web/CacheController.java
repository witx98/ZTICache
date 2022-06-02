package pl.witkowski.zticache.cache.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/cache")
public class CacheController {

    private final CacheManager cacheManager;

    @GetMapping("/names")
    @ResponseStatus(HttpStatus.OK)
    public Collection<String> getCacheNames() {
        log.info("invocation of method getCacheNames");
        return cacheManager.getCacheNames();
    }

    @DeleteMapping("/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCache() {
        log.info("invocation of method clearCache");
        cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }

    @GetMapping("/{cacheName}")
    @ResponseStatus(HttpStatus.OK)
    public Cache getCache(@PathVariable String cacheName) {
        log.info("invocation of method getCache");
        return cacheManager.getCache(cacheName);
    }
}
