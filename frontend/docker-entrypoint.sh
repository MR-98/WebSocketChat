#!/bin/sh
set -e

HOSTNAME_WITHOUT_PROTOCOL=${HOSTNAME#https://}

cat <<EOF > /usr/share/nginx/html/assets/env.js
(function (window) {
  window.__env = window.__env || {};

  window.__env.restUrl = "${HOSTNAME:-https://api.example.com}/api/v1";
  window.__env.websocketUrl = "wss://${HOSTNAME_WITHOUT_PROTOCOL:-api.example.com}/ws/ws-chat";
  window.__env.production = true;
})(this);
EOF

envsubst '${HOSTNAME_WITHOUT_PROTOCOL}' < /etc/nginx/templates/default.conf.template > /etc/nginx/conf.d/default.conf

exec "$@"
