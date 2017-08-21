#!/bin/bash
docker run \
    --name mvn-postgres \
    --rm \
    -p 127.0.0.1:5432:5432 \
    -e POSTGRES_DB=mvn \
    -e POSTGRES_USER=mvn \
    -e POSTGRES_PASSWORD=mvn \
    postgres:9.6.4