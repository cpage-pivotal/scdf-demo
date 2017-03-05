#!/usr/bin/env bash

function login() {
  local api_endpoint=$1
  local cf_user=$2
  local cf_pass=$3
  local skip_ssl_validation=$4

  local cf_skip_ssl_validation=""
  if [ "$skip_ssl_validation" = "true" ]; then
    cf_skip_ssl_validation="--skip-ssl-validation"
  fi

  cf api $api_endpoint $cf_skip_ssl_validation

  cf auth $cf_user $cf_pass
}

function target_org() {
  local org=$1
  local create=$2

  if [ "$create" = "true" ] && ! (cf orgs | grep -q ^$org$); then
    cf create-org $org
  fi

  cf target -o $org
}

function target_space() {
  local space=$1
  local create=$2

  if [ "$create" = "true" ] && ! (cf spaces | grep -q ^$space$); then
    cf create-space $space
  fi

  cf target -s $space
}

function service_exists() {
  local service_instance=$1
  cf curl /v2/service_instances | jq -e --arg name $service_instance '.resources[] | select(.entity.name == $name) | true' >/dev/null
}

function wait_for_service_instance() {
  local service_instance=$1
  local timeout=${2:-300}

  local guid=$(cf service $service_instance --guid)

  local start=$(date +%s)
  while true; do
    # Get the service instance info in JSON from CC and parse out the async 'state'
    local state=$(cf curl /v2/service_instances/$guid | jq -r .entity.last_operation.state)

    if [ "$state" = "succeeded" ]; then
      echo "Service $service_instance is ready"
      return
    elif [ "$state" = "failed" ]; then
      echo "Service $service_instance failed to provision"
      exit 1
    fi

    local now=$(date +%s)
    local time=$(($now - $start))
    if [[ "$time" -ge "$timeout" ]]; then
      echo "Timed out waiting for service instance to provision: $service_instance"
      exit 1
    fi
    sleep 5
  done
}