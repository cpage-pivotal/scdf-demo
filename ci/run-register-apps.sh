#!/bin/bash
#
# All CF_* variables are provided externally from this script

set -e -x

printf "The value is: %s\n" "$CF_API"
printf "The other value is: %s\n" "$CF_API2"

pushd scdf-demo/register-apps
  ./mvnw package
  java -jar target/register-apps-0.0.1-SNAPSHOT.jar -DCF_API=$CF_API
popd


