#!/bin/bash
#
# All CF_* variables are provided externally from this script

set -e -x

pushd scdf-demo/bubble
  ./mvnw package
popd

cp scdf-demo/bubble/target/bubble-0.0.1-SNAPSHOT.jar build-bubble-output

