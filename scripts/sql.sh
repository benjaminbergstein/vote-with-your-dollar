#!/bin/bash

if [ -z $APP_ENV ]; then
  APP_ENV="development"
fi

psql --port 65432 \
     --host localhost \
     places_annotations_$APP_ENV

