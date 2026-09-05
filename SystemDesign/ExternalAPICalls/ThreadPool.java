package SystemDesign.ExternalAPICalls;

import SystemDesign.ExternalAPICalls.ThreadPool.Advertisement;
import com.sun.net.httpserver.Authenticator.Retry;

import java.lang.classfile.Signature.TypeArg.Bounded;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Kafka
 *   ↓
 * Consumer
 *   ↓
 * Provider-specific Rate Limiter
 *   ↓
 * Provider-specific ThreadPoolExecutor
 *   ↓
 * HTTP Client with connection pool
 *   ↓
 * External API
 */
public class ThreadPool {

    public class Advertisement {}

    public enum AdPlatform {
        YOUTUBE, FACEBOOK, NETFLIX
    }

//    Each worker pool has its own:
//
//    Bounded ThreadPoolExecutor
//        +
//    Rate Limiter
//        +
//    HTTP Connection Pool
//        +
//    Retry Policy
//        +
//    Circuit Breaker

    class PlatformDispatcher {
        private final ExecutorService youtubeExecutor;
        private final ExecutorService facebookExecutor;

        public CompletableFuture<Result> dispatch(Advertisement ad, ExecutorService executorService) {
            return CompletableFuture.supplyAsync(
                    () -> youtubeClient.send(ad),
                    youtubeExecutor);
        }
    }

    public class ConnectionFactory {
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager();
        ConnectionFactory() {
            connectionManager.setMaxTotal(50);
            connectionManager.setDefaultMaxPerRoute(50);
        }
    }

    public class PoolFactory {

        Map<AdPlatform, ExecutorService> executors = Map.of(
                AdPlatform.YOUTUBE, Executors.newFixedThreadPool(50),
                AdPlatform.FACEBOOK, Executors.newFixedThreadPool(50),
                AdPlatform.NETFLIX, Executors.newFixedThreadPool(30));

        public ThreadPoolExecutor newPool() {
            return new ThreadPoolExecutor(
                    50,                      // corePoolSize
                    200,                     // maximumPoolSize
                    60,                      // keepAliveTime
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(1000),
                    new ThreadFactoryBuilder()
                            .setNameFormat("ad-dispatch-%d")
                            .build(),
                    new ThreadPoolExecutor.CallerRunsPolicy()
            );
        }

        ThreadPoolExecutor get(AdPlatform platform) {
            return executors.computeIfAbsent(platform, e -> newPool());
        }
    }

    /**
     * enable idempotence
     * acks qourum/all?
     * buffer.memory
     * max.block.ms
     */
    public class Addeliveryproducer {
        public void producer() {

        }
    }

    /**
     * segment.bytes
     * segment.ms
     * log.retention.hours
     * unclean.leader.election.false
     */
    public class kafkabroker {

    }

    /**
     * auto.commit false
     * max-attempts 3
     *
     * backoffmaxinterval
     * backoffinitialinterval
     * backoffmultiplier
     *
     * max.poll.records
     * max.poll.interval.ms
     *
     * session.timeout.ms
     * zookeeper.
     *          .sync
     * consumer.
     *
     * rebalance.max.retries
     * rebalance.backoff.ms
     */
    public class AdDeliveryWorker implements KafkaConumer {
        public void onMessage(ConsumerRecord record) {
            /*
            poll()
              ↓
            submit to worker pool
              ↓
            pause partition
              ↓
            API call completes
              ↓
            commit offset
              ↓
            resume partition
             */
        }
    }

    public record AdvertisementEvent(
            String advertisementId,
            String platform,
            String payload
    ) {
    }

    public record PlatformResponse(
            String advertisementId,
            String platform,
            String externalId
    ) {
    }

    @Configuration
    public class WebClientConfig {

        @Bean
        public WebClient youtubeWebClient() {

            ConnectionProvider connectionProvider =
                    ConnectionProvider.builder("youtube-connection-pool")
                            .maxConnections(100)

                            // Maximum requests waiting for a connection
                            .pendingAcquireMaxCount(1000)

                            // Do not keep idle connections forever
                            .maxIdleTime(Duration.ofSeconds(30))

                            // Avoid stale long-lived connections
                            .maxLifeTime(Duration.ofMinutes(5))

                            // Periodically clean idle/expired connections
                            .evictInBackground(Duration.ofSeconds(30))

                            .build();

            HttpClient httpClient =
                    HttpClient.create(connectionProvider)
                            .option(
                                    ChannelOption.CONNECT_TIMEOUT_MILLIS,
                                    5_000
                                   )
                            .responseTimeout(Duration.ofSeconds(10))
                            .doOnConnected(connection ->
                                                   connection.addHandlerLast(
                                                           new ReadTimeoutHandler(
                                                                   10,
                                                                   TimeUnit.SECONDS
                                                           )
                                                                            )
                                          );

            return WebClient.builder()
                    .baseUrl("https://api.youtube.com")
                    .clientConnector(
                            new ReactorClientHttpConnector(httpClient)
                                    )
                    .build();
        }
    }

package com.example.adworker.client;

import com.example.adworker.model.AdvertisementEvent;
import com.example.adworker.model.PlatformResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

    @Component
    public class YoutubeClient {

        private final WebClient webClient;

        public YoutubeClient(WebClient youtubeWebClient) {
            this.webClient = youtubeWebClient;
        }

        public Mono<PlatformResponse> send(
                AdvertisementEvent event
                                          ) {

            return webClient
                    .post()
                    .uri("/advertisements")
                    .bodyValue(event.payload())
                    .retrieve()

                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            response -> response
                                    .bodyToMono(String.class)
                                    .flatMap(body ->
                                                     Mono.error(
                                                             new PermanentApiException(
                                                                     "YouTube 4xx: " + body
                                                             )
                                                               )
                                            )
                             )

                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            response -> response
                                    .bodyToMono(String.class)
                                    .flatMap(body ->
                                                     Mono.error(
                                                             new RetryableApiException(
                                                                     "YouTube 5xx: " + body
                                                             )
                                                               )
                                            )
                             )

                    .bodyToMono(PlatformResponse.class);
        }
    }

    @Component
    public class YoutubeRateLimiter {

        private final Bucket bucket;

        public YoutubeRateLimiter() {

            Bandwidth limit =
                    Bandwidth.classic(
                            100,
                            Refill.greedy(
                                    100,
                                    Duration.ofSeconds(1)
                                         )
                                     );

            this.bucket = Bucket.builder()
                    .addLimit(limit)
                    .build();
        }

        public boolean tryAcquire() {
            return bucket.tryConsume(1);
        }
    }

    public class RetryableApiException
            extends RuntimeException {

        public RetryableApiException(String message) {
            super(message);
        }
    }

    public class PermanentApiException
            extends RuntimeException {

        public PermanentApiException(String message) {
            super(message);
        }
    }


    public class RetryPolicies {

        public static Retry youtubeRetryPolicy() {

            return Retry.backoff(
                            3,
                            Duration.ofMillis(500)
                                )

                    .maxBackoff(Duration.ofSeconds(10))

                    .filter(throwable ->
                                    !(throwable instanceof
                                            PermanentApiException)
                           )

                    .onRetryExhaustedThrow(
                            (retryBackoffSpec, retrySignal) ->
                                    retrySignal.failure()
                                          );
        }
    }

    @Configuration
    public class CircuitBreakerConfiguration {

        @Bean
        public CircuitBreaker youtubeCircuitBreaker() {

            CircuitBreakerConfig config =
                    CircuitBreakerConfig.custom()

                            .failureRateThreshold(50)

                            .waitDurationInOpenState(
                                    Duration.ofSeconds(30)
                                                    )

                            .slidingWindowSize(100)

                            .minimumNumberOfCalls(20)

                            .build();

            return CircuitBreaker.of(
                    "youtube",
                    config
                                    );
        }
    }

    @Service
    public class AdvertisementService {

        private final YoutubeClient youtubeClient;

        private final YoutubeRateLimiter rateLimiter;

        private final CircuitBreaker circuitBreaker;

        public AdvertisementService(
                YoutubeClient youtubeClient,
                YoutubeRateLimiter rateLimiter,
                CircuitBreaker youtubeCircuitBreaker
                                   ) {
            this.youtubeClient = youtubeClient;
            this.rateLimiter = rateLimiter;
            this.circuitBreaker = youtubeCircuitBreaker;
        }

        public Mono<PlatformResponse> process(
                AdvertisementEvent event
                                             ) {

            return waitForRateLimit()

                    .then(
                            youtubeClient
                                    .send(event)
                                    .retryWhen(
                                            RetryPolicies
                                                    .youtubeRetryPolicy()
                                              )
                         )

                    .transformDeferred(
                            CircuitBreakerOperator
                                    .of(circuitBreaker)
                                      );
        }

        private Mono<Void> waitForRateLimit() {

            if (rateLimiter.tryAcquire()) {
                return Mono.empty();
            }

            return Mono.delay(
                            java.time.Duration
                                    .ofMillis(10)
                             )
                    .then(waitForRateLimit());
        }
    }

}
