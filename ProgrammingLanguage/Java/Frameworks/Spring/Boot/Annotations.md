# Pagination 
```java
    ResponseEntity<Page<RoleDTO>> getAllRoles(@SortDefault(sort = "priRole") @PageableDefault(size = 20) final Pageable pageable) {}
```

# order
@Order
@DependsOn

### GlobalExceptionHandling
@ControllerAdvice
@ExceptionHandler

### Async
@EnableAsync
@Async

@SpringBootApplication // Main application class (combines @Configuration, @EnableAutoConfiguration, @ComponentScan)
@Component // Generic Spring component
@Service // Service layer component
@Configuration // Configuration class
@Bean // Define a bean
@Value("${property.name}") // Inject property value
@Autowired // Dependency injection
@Qualifier("beanName") // Specify which bean to inject
@Primary // Mark preferred bean when multiple candidates exist
@Profile("dev") // Activate for specific profile
@Conditional // Conditional bean registration
@EnableAsync // Enable async processing
@Async // Mark method as asynchronous
@EnableScheduling // Enable scheduled tasks
@Scheduled(cron = "0 0 * * * *") // Schedule method execution
@EventListener // Listen to application events
@PostConstruct // Execute after dependency injection
@PreDestroy // Execute before bean destruction


### Actuator
    spring-boot-starter-actuator
        @EnableScheduling // Enable scheduled tasks monitoring

## Development
    org.springframework.boot:spring-boot-devtools

## Web
    spring-boot-starter-web
        @RestController // RESTful web services
        @Controller // MVC controllers
        @RequestMapping // Map HTTP requests
        @GetMapping, @PostMapping, @PutMapping, @DeleteMapping, @PatchMapping
        @PathVariable // Extract URI template variables
        @RequestParam // Extract query parameters
        @RequestBody // Bind request body to method parameter
        @ResponseBody // Bind return value to response body
        @CrossOrigin // Enable CORS

    spring-boot-starter-webflux
        @EnableWebFlux // Enable WebFlux configuration (usually auto-configured)

## Data
    spring-boot-starter-data-jpa
        @Entity // Mark class as JPA entity
        @Table(name = "table_name") // Specify table name
        @Id // Primary key
        @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID
        @Column(name = "column_name") // Map field to column
        @ManyToOne, @OneToMany, @OneToOne, @ManyToMany // Relationships
        @JoinColumn // Specify foreign key column
        @Transactional // Transaction management
        @EnableJpaRepositories // Enable JPA repositories (auto-configured)
        @Repository // Mark repository interface
    spring-boot-starter-data-redis
        @EnableCaching // Enable caching support
        @Cacheable // Cache method results
        @CacheEvict // Remove entries from cache
        @CachePut // Update cache
        @RedisHash // Mark entity for Redis storage
        @EnableRedisRepositories // Enable Redis repositories
    org.flywaydb:flyway-core

## Kafka
    org.springframework.kafka:spring-kafka

## Configuration
    org.springframework.boot:spring-boot-configuration-processor

## Security
    spring-boot-starter-security
        @EnableWebSecurity // Enable Spring Security
        @Configuration // Security configuration class
        @EnableGlobalMethodSecurity(prePostEnabled = true) // Enable method security
        @PreAuthorize("hasRole('ROLE_ADMIN')") // Method-level authorization
        @PostAuthorize // Post-invocation authorization
        @Secured("ROLE_USER") // Method security
        @RolesAllowed("ADMIN") // JSR-250 annotation
        @WithMockUser // Test with mock user
        @EnableOAuth2Client // Enable OAuth2 client (auto-configured)
        @EnableResourceServer // Enable OAuth2 resource server
    spring-boot-starter-oauth2-client
    spring-boot-starter-oauth2-resource-server

## Logging
    spring-boot-starter-log4j2
        @Slf4j
    com.lmax:disruptor:3.3.6

### DistributedTracing
    micrometer-tracing-bridge-brave
    zipkin-reporter-brave
    micrometer-tracing-bridge-otel
        @Observed
        @NewSpan
        @ContinueSpan
        @SpanTag

### ServiceDiscovery
    spring-cloud-starter-netflix-eureka-server
        @EnableEurekaServer
    spring-cloud-starter-netflix-eureka-client
        @EnableEurekaClient
        @EnableDiscoveryClient
    spring-cloud-starter-kubernetes-client
        @EnableDiscoveryClient

### CircuitBreaker
    spring-cloud-starter-netflix-hystrix
        @EnableHystrix // Enable Hystrix
        @EnableCircuitBreaker // Enable circuit breaker
        @HystrixCommand(fallbackMethod = "fallbackMethodName") // Define circuit breaker
        @HystrixProperty // Configure Hystrix properties
    spring-cloud-starter-circuitbreaker-resilience4j
        @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
        @RateLimiter(name = "backendA")
        @Retry(name = "backendA")
        @Bulkhead(name = "backendA")
        @TimeLimiter(name = "backendA")