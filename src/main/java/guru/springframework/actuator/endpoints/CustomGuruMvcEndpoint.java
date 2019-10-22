package guru.springframework.actuator.endpoints;

import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.stereotype.Component;

@Component
public class CustomGuruMvcEndpoint extends EndpointMvcAdapter {

    public CustomGuruMvcEndpoint(CustomGuruEndpoint endpoint) {
        super(endpoint);
    }
}
