# Spring Boot starter JSON-RPC

JSON-RPC support to Spring Boot 3

###### maven central publishing in progress now

## Features

* [JSON-RPC](https://www.jsonrpc.org/specification) protocol support
* Multiple-parameter methods support
* Forwarding the request identifier to the method
* Forwarding a separate request parameters to the method
* Forwarding `HttpServletRequest` and `Principal` Spring parameters to the method
* Full customization support at all levels possible
* Ability to support your own protocol
* Spring Boot easy 3 integration
* Fully Swagger JSON-RPC support (Information about methods, DTO and the ability to send test requests)

## Limitations

* Works only for Spring Web (Servlet), doesn't work with Spring Web Flux
* JSON-RPC batch requests not supported
* Only springdoc-openapi Swagger support

## In plans

1. JSON-RPC batch requests
2. Passing `@CookieValue` and `@RequestParam` Spring parameters to the method
3. Request id auto generation

## How to use

You can try the [Example project](https://github.com/kibberpunk/spring-boot-starter-jsonrpc-example)

#### Add spring-boot-starter-jsonrpc Maven dependency to project

###### see example [pom.xml](https://github.com/kibberpunk/spring-boot-starter-jsonrpc-example/blob/master/pom.xml)

```xml

<dependency>
    <groupId>com.kibberpunk</groupId>
    <artifactId>spring-boot-starter-jsonrpc</artifactId>
    <version>${spring-boot-starter-jsonrpc.version}</version>
</dependency>
```

#### Add `JsonRpcAutoConfiguration` and `JsonRpcSwaggerAutoConfiguration` to `@SpringBootApplication` or enable auto configuration

###### see example [Runner](https://github.com/kibberpunk/spring-boot-starter-jsonrpc-example/blob/master/src/main/java/com/kibberpunk/spring/boot/starter/jsonrpc/example/Example.java)

```java

@SpringBootApplication
@Import(value = {
        ExampleConfiguration.class,
        JsonRpcAutoConfiguration.class,
        JsonRpcSwaggerAutoConfiguration.class
})
public class Example {

    /**
     * Starter spring method.
     *
     * @param args Program arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);
    }
}
```

#### Create controller with `@JsonRpcController` annotation

###### see example [NemesisController](https://github.com/kibberpunk/spring-boot-starter-jsonrpc-example/blob/master/src/main/java/com/kibberpunk/spring/boot/starter/jsonrpc/example/controller/NemesisController.java)

```java

@JsonRpcController
public class NemesisController {
    /*...*/
}
```

#### Create method with `@JsonRpcMethod` annotation and parameters

###### see example [NemesisController](https://github.com/kibberpunk/spring-boot-starter-jsonrpc-example/blob/master/src/main/java/com/kibberpunk/spring/boot/starter/jsonrpc/example/controller/NemesisController.java)

```java

@JsonRpcController
public class NemesisController {

    /**
     * 'Attack' JSON-RPC method
     */
    @JsonRpcMethod
    public TyrantCommandResponse attack(
            final @JsonRpcRequestId UUID id,
            final @JsonRpcRequestObject Target target,
            final @JsonRpcRequestObjectParameter("coordinates") Coordinates coordinates,
            final HttpServletRequest httpServletRequest,
            final Principal principal
    ) {
        return TyrantCommandResponse.builder()
                .status(Status.ACCEPTED)
                .description("Target captured! by coordinates: " + coordinates.toString())
                .build();
    }
}
```

#### Start application

#### Perform test JSON-RPC call

Perform test JSON-RPC call to
controller [NemesisController](/src/main/java/com/kibberpunk/spring/boot/starter/jsonrpc/example/controller/NemesisController.java)

```shell
curl --location 'http://localhost:8080/api' \
--header 'Content-Type: application/json' \
--data '{
  "id": "68998eaf-dee3-4652-90fe-776a397ed1ab",
  "method": "nemesisController.attack",
  "params": {
    "coordinates": {
      "x": 10,
      "y": 20,
      "z": 30
    },
    "description": "goal - Alice project" 
  },
  "jsonrpc": "2.0"
}'
```

## Swagger

#### Add `springdoc-openapi-starter-webmvc-ui` Maven dependency

```xml

<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>${springdoc-openapi-starter-webmvc-ui.version}</version>
</dependency>
```

Swagger configuration has default profile `Swagger`. To start Swagger you need to set `Swagger` profile to run command
and open [Local Swagger URL](http://localhost:8080/swagger-ui/index.html)\
![swagger](/images/Swagger.jpg "swagger")

## Settings

### Change default /api path

Add to application.properties next property

```properties
spring.json-rpc.consumer.path=you custom JSON-RPC api path
```

By default

```properties
spring.json-rpc.consumer.path=/api
```

## Method parameters notation

The target method for the call accepts only parameters marked with the
annotations `@JsonRpcRequestId`, `@JsonRpcRequestObject`, `@JsonRpcRequestObjectParameter` or has
type `HttpServletRequest` or `Principal`. If the parameter is not
marked with annotations, it will be ignored.

#### If this parameter is of primitive type, then its value will be set to the default value.