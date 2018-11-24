#!/bin/bash

if [ -z $APP_ENV ]; then
  APP_ENV="development"
fi

schema_dir=./sql/schema

for file in $(ls -l1 $schema_dir); do
  sql=$(cat $schema_dir/$file)
  psql --port 65432 \
       --host localhost \
       -c "$sql" \
       places_annotations_$APP_ENV
done

