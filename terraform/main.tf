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


resource "docker_container" "keycloak" {
  name       = var.container_name_keycloak
  image      = docker_image.keycloak.image_id
  entrypoint = ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm", "--health-enabled=true"]
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
    source    = "C://Users//darkstar2021//source//repos//vicuna-kotlin-quarkus//config//"
    type      = "bind"
    read_only = true
  }

  hostname = "keycloak"

  host {
    host = "host.docker.internal"
    ip   = "host-gateway"
  }

  ports {
    internal = 8080
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
