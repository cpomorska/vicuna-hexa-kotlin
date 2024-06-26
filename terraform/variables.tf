variable "container_name_vicuna" {
  type        = string
  default     = "vicun-hexa-quarkus"
  description = "Value of the name for the vicuna container"
}

variable "container_name_vicuna_kafka" {
  default = "vicuna-kafka"
  type = string
  description = "default name for kafka container"
}

variable "container_name_keycloak" {
  default     = "vicuna-keycloak"
  type        = string
  description = "Value of the name for the keycloak container"
}

variable "container_name_vicuna_kotlin" {
  default     = "vicuna-kotlin"
  type        = string
  description = "Value of the name for the vicuna container"
}

variable "volume_name_keycloak_import" {
  default     = "import-keycloak"
  type        = string
  description = "Value of the name for the keycloak import volume"
}

variable "container_name_postgres_keycloak" {
  default     = "vicuna-postgres-keycloak"
  type        = string
  description = "Value of the name for the postgres keycloak db container"
}

variable "env_kafka_broker_id" {
  default = 1
  type = number
}
variable "env_kafka_voter_id" {
  default = "1@vicuna-kafka:9093"
  type = any
}
variable "env_kafka_security_protocal" {
  default = "CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT"
  type = any
}
variable "env_kafka_advertised_listeners" {
  default = "PLAINTEXT://vicuna-kafka:29092,PLAINTEXT_HOST://localhost:29092"
  type = any
}
variable "env_kafka_listeners" {
  default = "PLAINTEXT://:19092,CONTROLLER://:9093,PLAINTEXT_HOST://:9092"
  type = any
}
variable "env_kafka_conroller_listener_names" {
  default = "CONTROLLER"
  type = any
}
variable "env_kafka_inter_broker_listener" {
  default = "PLAINTEXT"
  type = any
}
variable "env_kafka_log_dirs" {
  default = "/tmp/kraft-combined-logs"
  type = any
}
variable "env_kafka_topic_replication_factor" {
  default = 1
  type = number
}
variable "env_kafka_transaction_state_log_min_isr" {
  default = 1
  type = number
}
variable "env_kafka_transaction_state_log_repication_factor" {
  default = 1
  type = number
}

variable "env_keycloak_admin" {
  default     = "keycloak"
  type        = string
  description = "The admin environment variable for Keycloak."
}

variable "env_import_path_keycloak" {
  default     = "/opt/keycloak/data/import"
  type        = string
  description = "The import path environment variable for Keycloak."
}

variable "env_keycloak_db" {
  default     = "postgres"
  type        = string
  description = "The keycloak_db environment variable for Keycloak."
}

variable "env_keycloak_db_url_mann" {
  default     = "jdbc:postgresql://host.docker.internal:25432/keycloak"
  type        = string
  description = "The keycloak_db_url path environment variable for Keycloak."
}

variable "env_db_username" {
  default     = "keycloak"
  type        = string
  description = "The kc_db_url path environment variable for Keycloak."
}

variable "env_keycloak_admin_password" {
  default     = "keycloak"
  type        = string
  description = "The keycloak_admin_password environment variable for Keycloak."
}

variable "keycloak_db_password" {
  default     = "keycloak"
  type        = string
  description = "The kc_db_password environment variable for Keycloak."
}

variable "keycloak_db" {
  default     = "keycloak"
  type        = string
  description = "The postgres_db environment variable for Keycloak."
}

variable "container_name_postgres_vicuna" {
  default     = "vicuna-postgres"
  type        = string
  description = "The name variable for vicuna-postgres."
}

variable "postgres_db_vicuna" {
  default = "vicuna_user"
  type        = string
  description = "The postgres_db environment variable for vicuna."
}

variable "postgres_db_password_vicuna" {
  default     = "vicuna_pw"
  type        = string
  description = "The postgres_password environment variable for vicuna."
}

variable "postgres_db_username_vicuna" {
  default     = "vicuna_user"
  type        = string
  description = "The postgres_user environment variable for vicuna."
}