package jonathan_coutinho.CodingChallenge.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class AccountCacheConfig {

    private final AuthenticationService authenticationService;

    private final LoadingCache<String,String> loadingCache = CacheBuilder.newBuilder()
            .recordStats()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {

        @Override
        public String load(String key) throws Exception {
            return authenticationService.failedLogin(key);
        }
    });

    public String getUsername(String key) throws ExecutionException {
        return loadingCache.get(key);
    }

    public String checkCacheHits(String key){
        long cacheHits = loadingCache.stats().hitCount();
        long cacheMisses = loadingCache.stats().missCount();
        System.out.println(cacheHits);
        System.out.println(cacheMisses);

        if(cacheMisses > 1){
            authenticationService.unlockUserAccount(key);
            return "A conta foi desbloquada. Favor tente logar novamente";
        }else if(cacheHits == 4 || !authenticationService.loadUserByUsername(key).isAccountNonLocked()){
            authenticationService.lockUserAccount(key);
            return "A conta foi bloquada após múltiplas tentativas de login sem sucesso. Ela será desbloquada em 5 minutos";
        }else if(cacheMisses == 1 && cacheHits > 4){
            return "A conta foi bloquada após múltiplas tentativas de login sem sucesso. Ela será desbloquada em alguns minutos";
        }

        return "A senha informada está incorreta. A conta será bloquada após 4 tentativas de login sem sucesso.";
    }

    public void onCacheExpiration(String key){
        authenticationService.unlockUserAccount(key);
    }
}
