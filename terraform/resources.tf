resource "docker_image" "vicuna" {
  name         = "cpomorska/vicuna-kotlin-quarkus:latest"
  keep_locally = true
}

resource "docker_image" "kafka" {
  name         = "apache/kafka"
  keep_locally = true
}

resource "docker_image" "keycloak" {
  name         = "quay.io/keycloak/keycloak:latest"
  keep_locally = true
}

resource "docker_image" "postgres_keycloak" {
  name         = "postgres:17"
  keep_locally = true
}

resource "docker_volume" "import_volume" {
  name = var.volume_name_keycloak_import
}

resource "docker_network" "vicuna_network" {
  name            = "vicuna-network"
  check_duplicate = true
  internal        = true
}

resource "time_sleep" "wait_keycloak_for_postgres" {
  depends_on      = [docker_container.postgres-keycloak]
  create_duration = "20s"
}

resource "time_sleep" "wait_for_keycloak" {
  depends_on      = [docker_container.keycloak]
  create_duration = "30s"
}