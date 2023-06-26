# vicuna-hexa-quarkus

This project uses Quarkus with Kotlin, Camel and Kafka. It is a (work in progress) small showcase for DDD 
and a single REST Service. The REST Service itself is pure Camel. The backend uses switch Mann! to talk to database 
directly or via Kafka.

# Use Case
The main focus is using Kotlin and Camel with RESTEasy and Quarkus and bringing the application to OpenShift. An Eventlistener from Keycloak 
could use (Project ServiceConsumerEventlistener) this Service as external source for users.


