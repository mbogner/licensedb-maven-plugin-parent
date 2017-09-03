#!/bin/bash
mvn pm.mbo:licensedb-maven-plugin:add-third-party-database \
    -Dlicensedb.projectId=P1 \
    -Dlicensedb.projectName=TEST1 \
    -Dlicensedb.databaseUrl=jdbc:postgresql://127.0.0.1:5432/mvn \
    -Dlicensedb.databaseUser=mvn \
    -Dlicensedb.databasePassword=mvn \
    -Dlicensedb.databaseDriverClass=org.postgresql.Driver \
    -Dlicensedb.databaseDialect=org.hibernate.dialect.PostgreSQL94Dialect
