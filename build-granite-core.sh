#!/bin/bash
set -e
ant build.jar;ant build.maven.extra;ant -f maven/maven-deploy.xml install.to.repo