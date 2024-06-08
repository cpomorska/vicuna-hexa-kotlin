provider "docker" {
  host  = "unix:///run/user/1000/podman/podman.sock"
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