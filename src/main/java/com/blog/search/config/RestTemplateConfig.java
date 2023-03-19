package com.blog.search.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfig {
    @Value("${spring.rest-template.conn-timeout}")
    private int connTimeout;
    @Value("${spring.rest-template.read-timeout}")
    private int readTimeout;
    @Value("${spring.rest-template.max-conn-total}")
    private int maxConnTotal;
    @Value("${spring.rest-template.max-per-route}")
    private int maxConnPerRoute;

    @Bean
    public RestTemplate restTemplate() {
        /*
         * https://coding-start.tistory.com/185
         * default 설정으로, Connection pool을 사용하여 커넥션을 재사용 (max-route : 5, max-connection : 10)
         * 멀티쓰레드 환경에서도 Thread-safe 하게 Connection pool을 공유하여 사용
         *
         *  - maxConnTotal : total 최대 connection 갯수
         *  - maxPerRoute : ip 별 최대 connection 갯수
         *  - ConnectionTimeToLive : 쓰레드 풀 내에서 Connection의 TTL
         */
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(connTimeout);                         // ConnectTimeout   : 10*1000 연결 타임아웃 10초
        factory.setReadTimeout(readTimeout);                            // ReadTimeout      : 60*1000*3 연결 후 응답 타임아웃 3분

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setConnectionTimeToLive(300, TimeUnit.SECONDS)
                .setMaxConnTotal(maxConnTotal)                                  // 100  : total 최대 connection 갯수
                .setMaxConnPerRoute(maxConnPerRoute)                            // 10   : ip 별 최대 connection 갯수
                //.evictIdleConnections(10000L, TimeUnit.MILLISECONDS) // 서버에서 keepalive 시간동안 미 사용한 커넥션을 죽이는 등의 케이스 방어로 idle 커넥션을 주기적으로 지움
                .build();
        factory.setHttpClient(httpClient);
        return new RestTemplate(factory);
    }
}
