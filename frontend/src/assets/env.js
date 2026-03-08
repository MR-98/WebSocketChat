(function (window) {
  window.__env = window.__env || {};

  window.__env.restUrl = "http://host.docker.internal:8080/api/v1";
  window.__env.websocketUrl = "ws://host.docker.internal:8080/ws-chat"
  window.__env.production = false;
})(this);
