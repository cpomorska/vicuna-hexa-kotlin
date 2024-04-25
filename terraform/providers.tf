provider "docker" {
  host  = "npipe:////.//pipe//docker_engine"
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