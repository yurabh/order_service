                                              order_service
1)About the project.

This is an order microservice project that communicates with an invertory microservice by openfeign and also uses circuitbreaker-resilience4j 
for fault tolerance in communication between microservices.

And client config and netflix evreka client are also used.

2)Start the project locally.

2.1 Required to install the project.

Java 11

2.2 How to run project.

You should create environmental variables that are defined in the resources package like:

application.properties.

2.2.1 For application.properties you should set the value like:

* server.port=${Value}
* spring.application.name=${Value}
* eureka.instance.instance-id=${Value}
* spring.cloud.loadbalancer.ribbon.enabled=${Value}
* spring.cloud.config.uri=${Value}
