#!/bin/bash

if [ -z $APP_ENV ]; then
  APP_ENV="development"
fi

if [ -z $DATABASE_PORT ]; then
  DATABASE_PORT=65432
fi

if [ -z $DATABASE_USER ]; then
  DATABASE_USER=ben
fi

if [ -z $DATABASE_HOST ]; then
  DATABASE_HOST=localhost
fi

if [ -z $DATABASE_PASSWORD ]; then
  DATABASE_PASSWORD=""
fi

schema_dir=./sql/schema

if [ ! -z $1 ]; then
  migrate="false"
else
  migrate="true"
fi

for file in $(ls -l1 $schema_dir); do
  if [ "$migrate" = "false" ]; then
    ready=$(echo $file | grep $1 | wc -l)

    if [ $ready = "1" ]; then
      migrate="true"
    fi
  fi

  if [ "$migrate" = "true" ]; then
    sql=$(cat $schema_dir/$file)
    psql --port $DATABASE_PORT \
         --host $DATABASE_HOST \
         --user $DATABASE_USER \
         --password \
         -c "$sql" \
         places_annotations_$APP_ENV
  fi
done
