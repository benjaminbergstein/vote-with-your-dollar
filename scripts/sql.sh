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

psql --port $DATABASE_PORT \
     --host $DATABASE_HOST \
     --user $DATABASE_USER \
     --password \
     places_annotations_$APP_ENV

