FROM clojure:lein

RUN mkdir -p /app
WORKDIR /app

COPY project.clj /app/
RUN lein deps

COPY ./config /app/config
COPY ./src /app/src
COPY ./sql /app/sql
COPY ./scripts /app/scripts
COPY ./resources /app/resources

RUN mv "$(lein ring uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" app-standalone.jar

CMD ["java", "-jar", "app-standalone.jar"]
