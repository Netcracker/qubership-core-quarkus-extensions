package com.netcracker.cloud.quarkus.routesregistration.deployment;

import com.netcracker.cloud.quarkus.routesregistration.deployment.config.RequestRecorder;
import com.netcracker.cloud.quarkus.routesregistration.deployment.config.TestConfig;
import com.netcracker.cloud.quarkus.routesregistration.deployment.resource.CustomerResourceWithRepeatableAnnotation;
import com.netcracker.cloud.routesregistration.common.annotation.Route;
import com.netcracker.cloud.routesregistration.common.annotation.Routes;
import com.netcracker.cloud.routesregistration.common.gateway.route.Constants;
import com.netcracker.cloud.routesregistration.common.gateway.route.rest.CommonRequest;
import com.netcracker.cloud.routesregistration.common.gateway.route.rest.CompositeRequest;
import com.netcracker.cloud.routesregistration.common.gateway.route.rest.RegistrationRequest;
import com.netcracker.cloud.routesregistration.common.gateway.route.v3.CompositeRequestV3;
import com.netcracker.cloud.routesregistration.common.gateway.route.v3.RegistrationRequestV3;
import com.netcracker.cloud.routesregistration.common.gateway.route.v3.domain.*;
import io.quarkus.test.QuarkusUnitTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.*;

public class RoutesRegistrationWithRepeatableAnnotationProcessorTest extends AbstractRoutesRegistrationProcessorTest {
    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClass(TestConfig.class)
                    .addClass(RoutesRegistrationProcessor.class)
                    .addClass(CustomerResourceWithRepeatableAnnotation.class)
                    .addClass(RequestRecorder.class)
                    .addClass(Route.class)
                    .addClass(Routes.class)
                    .addAsResource("application.properties")
            );

    @Test
    public void mustSendRoutesOnStart() {
        Set<RouteConfigurationRequestV3> v3Requests = retrieveReqV3Body();
        CompositeRequest<CommonRequest> expectedRequests = buildExpectedRegistrationRequestsV3(cloudNamespace);
        assertRequestEquals(expectedRequests, v3Requests);
    }

    private CompositeRequest<CommonRequest> buildExpectedRegistrationRequestsV3(String namespace) {
        List<RegistrationRequest> registrationRequests = new ArrayList<>();

        registrationRequests.add(new RegistrationRequestV3(RouteConfigurationRequestV3.builder()
                .namespace(namespace)
                .gateways(Collections.singletonList(Constants.PUBLIC_GATEWAY_SERVICE))
                .virtualServices(Collections.singletonList(
                        VirtualService.builder()
                                .name(Constants.PUBLIC_GATEWAY_SERVICE)
                                .routeConfiguration(RouteConfig.builder()
                                        .version("v1")
                                        .routes(Collections.singletonList(
                                                RouteV3.builder()
                                                        .destination(RouteDestination.builder()
                                                                .cluster(microserviceName)
                                                                .endpoint("http://quarkus-quickstart-test-v1:8080")
                                                                .build())
                                                        .rules(Arrays.asList(
                                                                Rule.builder()
                                                                        .allowed(true)
                                                                        .match(RouteMatch.builder().prefix("/testRouteController/testRouteMethod").build())
                                                                        .prefixRewrite("/testRouteController/testRouteMethod")
                                                                        .build(),
                                                                Rule.builder()
                                                                        .allowed(false)
                                                                        .match(RouteMatch.builder().prefix("/testRouteController").build())
                                                                        .prefixRewrite("/testRouteController")
                                                                        .build()
                                                        ))
                                                        .build()))
                                        .build())
                                .build()
                ))
                .build()
        ));

        registrationRequests.add(new RegistrationRequestV3(RouteConfigurationRequestV3.builder()
                .namespace(namespace)
                .gateways(Collections.singletonList(Constants.PRIVATE_GATEWAY_SERVICE))
                .virtualServices(Collections.singletonList(
                        VirtualService.builder()
                                .name(Constants.PRIVATE_GATEWAY_SERVICE)
                                .routeConfiguration(RouteConfig.builder()
                                        .version("v1")
                                        .routes(Collections.singletonList(
                                                RouteV3.builder()
                                                        .destination(RouteDestination.builder()
                                                                .cluster(microserviceName)
                                                                .endpoint("http://quarkus-quickstart-test-v1:8080")
                                                                .build())
                                                        .rules(Arrays.asList(
                                                                Rule.builder()
                                                                        .allowed(true)
                                                                        .match(RouteMatch.builder().prefix("/testRouteController/testRouteMethod").build())
                                                                        .prefixRewrite("/testRouteController/testRouteMethod")
                                                                        .build(),
                                                                Rule.builder()
                                                                        .allowed(true)
                                                                        .match(RouteMatch.builder().prefix("/testRouteController").build())
                                                                        .prefixRewrite("/testRouteController")
                                                                        .build()
                                                        ))
                                                        .build()))
                                        .build())
                                .build()
                ))
                .build()
        ));

        registrationRequests.add(new RegistrationRequestV3(RouteConfigurationRequestV3.builder()
                .namespace(namespace)
                .gateways(Collections.singletonList(Constants.INTERNAL_GATEWAY_SERVICE))
                .virtualServices(Collections.singletonList(
                        VirtualService.builder()
                                .name(Constants.INTERNAL_GATEWAY_SERVICE)
                                .routeConfiguration(RouteConfig.builder()
                                        .version("v1")
                                        .routes(Collections.singletonList(
                                                RouteV3.builder()
                                                        .destination(RouteDestination.builder()
                                                                .cluster(microserviceName)
                                                                .endpoint("http://quarkus-quickstart-test-v1:8080")
                                                                .build())
                                                        .rules(Arrays.asList(
                                                                Rule.builder()
                                                                        .allowed(true)
                                                                        .match(RouteMatch.builder().prefix("/testRouteController/testRouteMethod").build())
                                                                        .prefixRewrite("/testRouteController/testRouteMethod")
                                                                        .build(),
                                                                Rule.builder()
                                                                        .allowed(true)
                                                                        .match(RouteMatch.builder().prefix("/testRouteController").build())
                                                                        .prefixRewrite("/testRouteController")
                                                                        .build()
                                                        ))
                                                        .build()))
                                        .build())
                                .build()
                ))
                .build()
        ));

        registrationRequests.add(new RegistrationRequestV3(RouteConfigurationRequestV3.builder()
                .namespace(namespace)
                .gateways(Collections.singletonList("testFirstGateway"))
                .virtualServices(Collections.singletonList(
                        VirtualService.builder()
                                .name(microserviceName)
                                .hosts(Collections.singletonList("testFirstVHost"))
                                .routeConfiguration(RouteConfig.builder()
                                        .version("v1")
                                        .routes(Collections.singletonList(
                                                RouteV3.builder()
                                                        .destination(RouteDestination.builder()
                                                                .cluster(microserviceName)
                                                                .endpoint("http://quarkus-quickstart-test-v1:8080")
                                                                .build())
                                                        .rules(List.of(
                                                                Rule.builder()
                                                                        .allowed(true)
                                                                        .match(RouteMatch.builder().prefix("/testRouteController").build())
                                                                        .prefixRewrite("/testRouteController")
                                                                        .build()
                                                        ))
                                                        .build()))
                                        .build())
                                .build()
                ))
                .build()
        ));

        registrationRequests.add(new RegistrationRequestV3(RouteConfigurationRequestV3.builder()
                .namespace(namespace)
                .gateways(Collections.singletonList("testSecondGateway"))
                .virtualServices(Collections.singletonList(
                        VirtualService.builder()
                                .name(microserviceName)
                                .hosts(Collections.singletonList("testSecondVHost"))
                                .routeConfiguration(RouteConfig.builder()
                                        .version("v1")
                                        .routes(Collections.singletonList(
                                                RouteV3.builder()
                                                        .destination(RouteDestination.builder()
                                                                .cluster(microserviceName)
                                                                .endpoint("http://quarkus-quickstart-test-v1:8080")
                                                                .build())
                                                        .rules(List.of(
                                                                Rule.builder()
                                                                        .allowed(true)
                                                                        .match(RouteMatch.builder().prefix("/testRouteController/testRouteMethod").build())
                                                                        .prefixRewrite("/testRouteController/testRouteMethod")
                                                                        .build()
                                                        ))
                                                        .build()))
                                        .build())
                                .build()
                ))
                .build()
        ));

        return new CompositeRequestV3(registrationRequests, Collections.emptyList());
    }
}
