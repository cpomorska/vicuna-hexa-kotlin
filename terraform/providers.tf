provider "docker" {
  host  = "unix:///var/run/docker.sock"
  alias = "condainer"
}

provider "vault" {
  address   = "https://localhost:8200"
  alias     = "local_vault"
  namespace = "kv/keycloak"
}

provider "time" {
  alias = "hashitime"
}