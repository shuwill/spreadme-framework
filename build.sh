#!/bin/sh
projects=(
spreadme-dependencies
spreadme-parent
spreadme-test
spreadme-api
spreadme-commons
spreadme-boot
spreadme-data
spreadme-component
spreadme-web
spreadme-security
)
for project in "${projects[@]}"
do
  cd $project
  echo "into $project"
  if [ -f "pom.xml" ]; then
     mvn clean install
  fi
  cd ..
done
