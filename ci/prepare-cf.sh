#!/bin/bash
#
# All CF_* variables are provided externally from this script

set -e

CF_SERVICE_PROVISION_TIMEOUT=${CF_SERVICE_PROVISION_TIMEOUT:-500}

source $(dirname $0)/common.sh

printf "Logging into PCF"
login $CF_API $CF_USER $CF_PASSWORD $CF_SKIP_SSL

create=true
UUID=$(uuidgen)

target_org $CF_ORG $create
target_space $CF_SPACE $create

# Create Services
if ! service_exists scdf-mysql; then
  cf create-service $DB_SERVICE_NAME $DB_SERVICE_PLAN scdf-mysql
else
  printf "MySQL service already exists; not creating"
fi

if ! service_exists scdf-rabbit; then
  cf create-service $RABBIT_SERVICE_NAME $RABBIT_SERVICE_PLAN scdf-rabbit
else
  printf "Rabbit service already exists; not creating"
fi

if ! service_exists scdf-redis; then
  cf create-service $REDIS_SERVICE_NAME $REDIS_SERVICE_PLAN scdf-redis
else
  printf "Redis service already exists; not creating"
fi
