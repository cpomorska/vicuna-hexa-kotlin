# vicuna-hexa-quarkus

This project uses Quarkus with Kotlin, Camel and Kafka. It is a (work in progress) small showcase for DDD 
and a single REST Service. The REST Service itself is pure Camel. The backend uses a switch Mann! to talk to database 
directly or via Kafka.

# Use Case
The main focus is using Kotlin and Camel with RESTEasy and Quarkus and bringing the application to OpenShift. An Eventlistener from Keycloak 
could use (Project ServiceConsumerEventlistener) this Service as external source for users.

# Why Vicuna
Vicuna is a camel, the national animal of Peru. Because of Camel (the Integration Framework) the Application is named Vicuna. It has nothing to do with AI or Ki Models.

# Tools used for Development
- Podman - for Docker Replacement
- Openshift Local - a local Openshift Installation
- Intellij IDEA - Java and Kotlin Development
- Windows 11 and RHEL 9
- Keycloak 22.01
- PostgreSQL 15



