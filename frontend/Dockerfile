### STAGE 1: Build ###
FROM node:lts-alpine AS build

#### make the 'app' folder the current working directory
WORKDIR /usr/src/app

#### copy both 'package.json' and 'package-lock.json' (if available)
COPY package*.json ./

#### install angular cli
RUN npm install -g @angular/cli

#### install project dependencies
RUN npm install

#### copy things
COPY . .

#### generate build --prod
RUN npm run build --prod

### STAGE 2: Run ###
FROM nginxinc/nginx-unprivileged

#### copy nginx template
COPY ./nginx.conf.template /etc/nginx/templates/default.conf.template

#### copy artifact build from the 'build environment'
COPY --from=build /usr/src/app/dist/websocket-chat/browser /usr/share/nginx/html

### copy docker-entrypoint.sh
COPY docker-entrypoint.sh /docker-entrypoint.sh
USER root
RUN chmod +x /docker-entrypoint.sh \
    && chown -R 101:101 /usr/share/nginx/html
USER 101

EXPOSE 8081

ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
