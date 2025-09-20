# Changes (Updated: September 20, 2025)

- Migrated build system from Gradle to Maven for better enterprise support and standardization
- Fixed Maven wrapper script to properly set `maven.multiModuleProjectDirectory` system property
- Updated build commands to use Maven exclusively for consistency
- Standardized on Maven for both development and CI/CD workflows
- Improved project structure alignment with Maven conventions
- Added comprehensive GitHub Actions workflows for Maven-based CI/CD
- Configured Renovate dependency management for automated updates
- Integrated SonarQube support for code quality analysis
- Added JaCoCo code coverage reporting
- Configured Liquibase for database migration management
- Set up Docker container image building via Quarkus Maven plugin
- Added comprehensive test separation (unit tests via Surefire, integration tests via Failsafe)
- Configured GitLab CI/CD pipeline for Maven builds
- Added Terraform infrastructure as code for container deployment
- Integrated Keycloak OIDC authentication with proper configuration
- Set up Kafka messaging with Quarkus integration
- Added comprehensive application configuration via YAML

# vicuna-kotlin-quarkus

This project uses Quarkus with Kotlin, Camel and Kafka. It is a (work in progress) small showcase for DDD
and a single REST Service. The REST Service itself is pure Camel. The backend uses a switch Mann! to talk to database
directly or via Kafka.

# Build System: Why Maven over Gradle

This project uses **Maven** as the primary build system for the following reasons:

## Enterprise Adoption & Standardization
- **Industry Standard**: Maven is the de facto standard in enterprise Java development with wider adoption across organizations
- **Corporate Policies**: Many enterprises have standardized on Maven for compliance and governance reasons
- **Tool Integration**: Better integration with enterprise CI/CD pipelines, security scanning tools, and dependency management systems

## Quarkus & Framework Support
- **Native Quarkus Support**: Quarkus provides first-class Maven support with extensive documentation and examples
- **Extension Ecosystem**: Most Quarkus extensions and guides are Maven-first, ensuring better compatibility
- **Jakarta EE Alignment**: Better alignment with Jakarta EE ecosystem which predominantly uses Maven

## Dependency Management
- **Mature BOM Support**: Superior Bill of Materials (BOM) handling for managing transitive dependencies
- **Version Management**: More predictable and standardized approach to version management across multi-module projects
- **Repository Management**: Better integration with enterprise artifact repositories (Nexus, Artifactory)

## IDE & Tooling Support
- **IntelliJ IDEA**: Superior Maven integration with better project import and dependency resolution
- **CI/CD Integration**: More mature integration with GitHub Actions, Jenkins, and other CI systems
- **Build Reproducibility**: More deterministic builds across different environments

# Current Status

#### Works
- Project builds and (most of the) tests run
- Image is built from Quarkus Maven plugin
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

#### 1. Build image (with container image build enabled and without tests)

   > ./mvnw clean package -Dquarkus.container-image.build=true -DskipTests

#### 2. Build artifact (without container image build and without tests)

   > ./mvnw clean package -DskipTests

#### 3. Run tests

   - Unit tests (Maven Surefire):
     > ./mvnw test
   - Integration tests (Maven Failsafe):
     > ./mvnw verify
   - All tests:
     > ./mvnw clean verify

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
- IntelliJ IDEA - Java and Kotlin Development
- Windows 11 and RHEL 9
- Keycloak 2x.x.x (latest)
- PostgreSQL 15
- Terraform 1.8.0 + docker extension
- Maven 3.9+ - Primary build system



