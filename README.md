# spring-boot-actuator notes

## Endpoints

#### info
Info can be added by adding info. properties
```properties
info.app.encoding=UTF-8
info.app.java.source=1.8
info.app.java.target=1.8
info.author.name=Jakub Vrana
info.author.email=jvrana@monetplus.cz
```
or implement _**InfoContributor**_:
```java
public interface InfoContributor {

	/**
	 * Contributes additional details using the specified {@link Info.Builder Builder}.
	 * @param builder the builder to use
	 */
	void contribute(Info.Builder builder);

}
```
example:
```java
@Component
public class JvrInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("JVR", "just an example of InfoContributoer");
    }
}
```

to add git information use maven plugin
```xml
<build>
    <plugins>
        <!-- add git information inot the info endpoint -->
        <plugin>
            <groupId>pl.project13.maven</groupId>
            <artifactId>git-commit-id-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```


#### metrics
To add custom metrics implement **_CounterService_** or **_GaugeService_** interfaces (they are some spring implementations)
```java
public interface CounterService {

	/**
	 * Increment the specified counter by 1.
	 * @param metricName the name of the counter
	 */
	void increment(String metricName);

	/**
	 * Decrement the specified counter by 1.
	 * @param metricName the name of the counter
	 */
	void decrement(String metricName);

	/**
	 * Reset the specified counter.
	 * @param metricName the name of the counter
	 */
	void reset(String metricName);

} 
```

```java
public interface GaugeService {

	/**
	 * Set the specified gauge value.
	 * @param metricName the name of the gauge to set
	 * @param value the value of the gauge
	 */
	void submit(String metricName, double value);

}
```

example:
```java
@Service
public class ProductServiceImpl implements ProductService {

    ...
    private final CounterService counterService;
    private final GaugeService gaugeService;

    @Autowired
    public ProductServiceImpl(CounterService counterService,
                              GaugeService gaugeService) {
        this.counterService = counterService;
        this.gaugeService = gaugeService;
    }

    @Override
    public Product getProduct(Integer id) {
        ...
        counterService.increment("guru.springframework.services.ProductService.getProduct");
        ...
    }

    @Override
    public List<Product> listProducts() {
        ...
        counterService.increment("guru.springframework.services.ProductService.listProducts");
        gaugeService.submit("guru.springframework.services.ProductService.listProducts.pageViewsPerMin", 6);
        ...
    }

}
```

will produce:
```json
{
  ...
  "gauge.guru.springframework.services.ProductService.listProducts.pageViewsPerMin": 6.0,
  "counter.guru.springframework.services.ProductService.listProducts": 2,
  "counter.guru.springframework.services.ProductService.getProduct": 3
  ...
}
```

#### health
To add custom health data implement HealthIndicator
```java
public interface HealthIndicator {

	/**
	 * Return an indication of health.
	 * @return the health for
	 */
	Health health();

}
```
example:
```java
@Component
public class CustomHealthIndicator implements HealthIndicator {

    private Random random = new Random();

    @Override
    public Health health() {

        if (random.nextBoolean())
            return Health.down().withDetail("ERR-001", "Random Failure").build();

        return Health.up().withDetail("message", "This is my custom health indicator").build();
    }
}
```

#### custom endpoint
```java
@Component
public class CustomGuruEndpoint extends AbstractEndpoint<List<String>> {
    public CustomGuruEndpoint() {
        // id (path) / is sensitive
        super("customGuruEndpoint", false);
    }

    @Override
    public List<String> invoke() {
        List<String> list = new ArrayList<>(3);

        list.add("Umphrey's");
        list.add("McGee");
        list.add("Zonkey rocks!");

        return list;
    }
}
```
```java

@Component
public class CustomGuruMvcEndpoint extends EndpointMvcAdapter {

    public CustomGuruMvcEndpoint(CustomGuruEndpoint endpoint) {
        super(endpoint);
    }
}
```
_**http://{{ hostname  }}/customGuruEndpoint**_
produces:
```json
[
  "Umphrey's",
  "McGee",
  "Zonkey rocks!"
]
```

### properties
```properties
# add prefix to all endpoints
management.context-path=/endpoints

# setup custom path to endpoint
endpoints.info.path=/system/info

#port used to expose actuator
management.port=8081
```

### remote shell:
`ssh -p 2000 -l user localhost`

#### changing logging level
`ssh -p 2000 -l user localhost`

`repl groovy`

`logger = org.slf4j.LoggerFactory.getLogger("guru.springframework.services.LogOutputGeneratorService")`

`logger.setLevel(ch.qos.logback.classic.Level.ERROR`
