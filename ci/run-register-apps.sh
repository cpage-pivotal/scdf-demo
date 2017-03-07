#!/bin/bash
#
# All CF_* variables are provided externally from this script

set -e -x

pushd scdf-demo/register-apps
  ./mvn package
  java -jar target/register-apps-0.0.1-SNAPSHOT.jar
popd


