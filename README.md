# vicuna-kotlin-quarkus

This project uses Quarkus with Kotlin, Camel and Kafka. It is a (work in progress) small showcase for DDD
and a single REST Service. The REST Service itself is pure Camel. The backend uses a switch Mann! to talk to database
directly or via Kafka.

# Current Status

#### Works
- Project builds and (most of the) tests run
- Image ist build from quarkus (gradle imagebuild)
- The application runs in containers
- A local docker-compose setup exists
- Dependent software like Postgres, Kafka and keycloak are configured
- Terraform with docker and podman works
- REST services are accesible and secured with OIDC
- Kafka connection and Topic creation

### Possible does not work
- Terraform destroys containers to early (only with podman) 
- Kafka spams without consumer services
- No vault connection for secrets
- some tests fail (30 from 69)

# Build the project

#### 1. Build image (all with imagebuild enabled and without tests)

   > gradle clean build imagebuild -Dquarkus.container-image.build=true -x test

#### 2. Build artifact (all without imagebuild and without tests)

   > gradle clean build -x test

# Run the project (in docker containers)

#### 1. Run with docker compose (all containers)

* Requires: docker or podman, **artifact was build**
* Spans 2 x Postgres, Kafka, Keycloak, vicuna-hexa-kotlin
* Accessible on http://localhost:28089/vicuna/q/hui/
* Keycloak is accessible on http://localhost:8180/realms/development

> - start with: **docker compose -f docker-compose.local.yml up -d --build --force-recreate**
> - shutdown with: **docker compose -f docker-compose.local.yml down**

#### 2. Run with terraform (all containers)

* Requires: docker or podman, **image was build**
* Spans 2 x Postgres, Kafka, Keycloak, vicuna-hexa-kotlin
* Accessible on http://localhost:28089/vicuna/q/hui/
* Keycloak is accessible on http://localhost:8180/realms/development

> * Open terminal and change to the projectroot -> terraform directory
> * Init with terraform -> **terraform init**
> * Validate tf files -> **terraform validate**
> * Plan with terraform -> **terraform plan -out "main.tfplan"**
> * Execute with terraform -> **terraform apply "main.tfplan"**
 
* Destroy deployed terraform resources
> * Plan for resources to destroy -> **terraform plan -destroy -out "destroy.main.tfplan"**
> * Destroy resources -> **terraform apply "destroy.main.tfplan"**


#### 2. Run with OpenTofu (all containers)

* Requires: docker or podman, opentofu >= 1.7 **image was build**
* Spans 2 x Postgres, Kafka, Keycloak, vicuna-hexa-kotlin
* Accessible on http://localhost:28089/vicuna/q/hui/
* Keycloak is accessible on http://localhost:8180/realms/development

> * Open terminal and change to the projectroot -> terraform directory
> * Init with opentofu -> **tofu init**
> * Validate tf files -> **tofu validate**
> * Plan with opentofu -> **tofu plan -out "main.tfplan"**
> * Execute with opentofu -> **tofu apply "main.tfplan"**

* Destroy deployed opentofu resources
> * Plan for resources to destroy -> **tofu plan -destroy -out "destroy.main.tfplan"**
> * Destroy resources -> **tofu apply "destroy.main.tfplan"**


# Use Case

The main focus is using Kotlin and Camel with RESTEasy and Quarkus and bringing the application to OpenShift. An
Eventlistener from Keycloak
could use (Project ServiceConsumerEventlistener) this Service as external source for users.

# Why Vicuna

Vicuna is a camel, the national animal of Peru. Because of Camel (the Integration Framework) the Application is named
Vicuna. It has nothing to do with AI or Ki Models.

# Tools used for Development

- Docker
- Podman - for Docker ReplacementDocker
- Openshift Local - a local Openshift Installation
- Intellij IDEA - Java and Kotlin Development
- Windows 11 and RHEL 9
- Keycloak 2x.x.x (latest)
- PostgreSQL 15
- Terraform 1.8.0 + docker extension



