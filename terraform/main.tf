terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0.1"
    }
    vault = {
      source  = "hashicorp/vault"
      version = "~> 4.0.0"
    }
    time = {
      source  = "hashicorp/time"
      version = "~> 0.11.0"
    }
  }
}

resource "docker_container" "vicuna_kotlin" {
  name       = var.container_name_vicuna_kotlin
  image      = docker_image.vicuna.image_id
  depends_on = [time_sleep.wait_for_keycloak]

  hostname = "vicuna-kotlin-quarkus"

  host {
    host = "host.docker.internal"
    ip   = "host-gateway"
  }

  ports {
    internal = 8089
    external = 28089
  }
}

resource "docker_container" "vicuna_kafka" {
  name       = var.container_name_vicuna_kafka
  image      = docker_image.kafka.image_id

  hostname = "vicuna-kafka"

  env = [
    "KAFKA_NODE_ID=${var.env_kafka_broker_id}",
    "CLUSTER_ID='4L6g3nShT-eMCtK--X86sw'",
    "KAFKA_ADVERTISED_LISTENERS=${var.env_kafka_advertised_listeners}",
    "KAFKA_CONTROLLER_LISTENER_NAMES=${var.env_kafka_conroller_listener_names}",
    "KAFKA_CONTROLLER_QUORUM_VOTERS=${var.env_kafka_voter_id}",
    "KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS=0",
    "KAFKA_INTER_BROKER_LISTENER_NAME=${var.env_kafka_inter_broker_listener}",
    "KAFKA_LISTENERS=${var.env_kafka_listeners}",
    "KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=${var.env_kafka_security_protocal}",
    "KAFKA_LOG_DIRS=${var.env_kafka_log_dirs}",
    "KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=${var.env_kafka_topic_replication_factor}",
    "KAFKA_PROCESS_ROLES=broker,controller",
    "KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=${var.env_kafka_transaction_state_log_min_isr}",
    "KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=${var.env_kafka_transaction_state_log_repication_factor}",
    "KAFKA_CREATE_TOPICS=user-out",
    "ADVERTISED_HOST=host.docker.internal"
  ]

  host {
    host = "host.docker.internal"
    ip   = "host-gateway"
  }

  ports {
    internal = 9092
    external = 29092
  }
}

resource "docker_container" "keycloak" {
  name       = var.container_name_keycloak
  image      = docker_image.keycloak.image_id
  entrypoint = ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm", "--health-enabled=true", "--http-port=8180"]
  depends_on = [docker_container.postgres-keycloak]

  env = [
    "KEYCLOAK_ADMIN=${var.env_keycloak_admin}",
    "KEYCLOAK_ADMIN_PASSWORD=${var.env_keycloak_admin_password}",
    "KEYCLOAK_IMPORT=${var.env_import_path_keycloak}",
    "KC_DB=${var.env_keycloak_db}",
    "KC_DB_URL=${var.env_keycloak_db_url_mann}",
    "KC_DB_USERNAME=${var.env_db_username}",
    "KC_DB_PASSWORD=${var.keycloak_db_password}"
  ]

  healthcheck {
    test     = ["CMD-SHELL", "exec 3<>/dev/tcp/localhost/8080 && echo -e 'GET /health/ready HTTP/1.1\\r\\nHost: localhost\\r\\nConnection: close\\r\\n\\r\\n' >&3 && cat <&3 | grep -q '200 OK'"]
    interval = "5s"
    timeout  = "2s"
    retries  = 4
  }

  mounts {
    target    = "/opt/keycloak/data/import/"
    source    = "${path.cwd}/config/"
    type      = "bind"
    read_only = true
  }

  hostname = "keycloak"

  host {
    host = "host.docker.internal"
    ip   = "host-gateway"
  }

  ports {
    internal = 8180
    external = 8180
  }
}

resource "docker_container" "postgres-keycloak" {
  name  = var.container_name_postgres_keycloak
  image = docker_image.postgres_keycloak.image_id

  env = [
    "POSTGRES_DB=${var.keycloak_db}",
    "POSTGRES_PASSWORD=${var.keycloak_db_password}",
    "POSTGRES_USER=${var.env_db_username}"
  ]

  healthcheck {
    test     = ["CMD-SHELL", "pg_isready -U keycloak"]
    interval = "5s"
    timeout  = "5s"
    retries  = 5
  }

  hostname = "postgres-kc"

  host {
    host = "host.docker.internal"
    ip   = "host-gateway"
  }

  ports {
    internal = 5432
    external = 25432
  }
}

resource "docker_container" "postgres-vicuna" {
  name  = var.container_name_postgres_vicuna
  image = docker_image.postgres_keycloak.image_id

  env = [
    "POSTGRES_DB=${var.postgres_db_vicuna}",
    "POSTGRES_PASSWORD=${var.postgres_db_password_vicuna}",
    "POSTGRES_USER=${var.postgres_db_username_vicuna}"
  ]

  healthcheck {
    test     = ["CMD-SHELL", "pg_isready -U vicuna_user"]
    interval = "5s"
    timeout  = "5s"
    retries  = 5
  }

  hostname = "postgres-kc"

  host {
    host = "host.docker.internal"
    ip   = "host-gateway"
  }

  ports {
    internal = 5432
    external = 15432
  }
}