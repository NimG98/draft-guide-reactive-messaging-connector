FROM open-liberty

ARG VERSION=1.0
ARG REVISION=SNAPSHOT

LABEL \
  org.opencontainers.image.authors="My Name" \
  org.opencontainers.image.vendor="Open Liberty" \
  org.opencontainers.image.url="local" \
  org.opencontainers.image.source="https://github.com/OpenLiberty/guide-microprofile-reactive-messaging" \
  org.opencontainers.image.version="$VERSION" \
  org.opencontainers.image.revision="$REVISION" \
  vendor="Open Liberty" \
  name="bar" \
  version="$VERSION-$REVISION" \
  summary="The bar microservice from the MicroProfile Reactive Messaging guide" \
  description="This image contains the bar microservice running with the Open Liberty runtime."

COPY --chown=1001:0 src/main/liberty/config /config/
COPY --chown=1001:0 target/bar.war /config/apps

EXPOSE 9084 9447

# It is recommended to run the configure.sh when build image for production.
# RUN configure.sh