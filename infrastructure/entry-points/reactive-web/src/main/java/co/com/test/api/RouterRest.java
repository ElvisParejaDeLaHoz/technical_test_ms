package co.com.test.api;

import co.com.test.api.branch.BranchHandler;
import co.com.test.api.franchise.FranchiseHandler;
import co.com.test.api.product.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(FranchiseHandler franchiseHandler, BranchHandler branchHandler,
                                                         ProductHandler productHandler) {
        return route(POST("/franchise"), franchiseHandler::create)
                .andRoute(PUT("/franchise/{id}"), franchiseHandler::update)
                .andRoute(POST("/branch"), branchHandler::create)
                .andRoute(PUT("/branch/{id}"), branchHandler::update)
                .andRoute(POST("/product"), productHandler::create)
                .andRoute(GET("/product/{franchiseId}/top"), productHandler::getTopProducts)
                .andRoute(PUT("/product/{id}/stock"), productHandler::updateStock)
                .andRoute(DELETE("/product/{id}"), productHandler::delete);
    }
}
