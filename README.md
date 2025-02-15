![Logo](https://raw.githubusercontent.com/MR-98/WebSocketChat/refs/heads/develop/docs/images/banner.png)
# WebSocket Chat
A web application that allows for real-time communication. The user can join existing rooms or create their own and invite others to them. The use of WebSockets means that new messages are delivered almost immediately. All communication is secured using JWT and Keycloak so that it is not possible to send or receive messages from rooms that you do not belong to. The application resembles Facebook Messenger or Discord in use.
## Features

- Sending and receiving messages
- Creating, editing, deleting rooms
- Room invitation mechanism
- Message content encryption
- Responsive layout


## Demo
[Live Demo](https://mr98.site/chat)

![Example1](https://raw.githubusercontent.com/MR-98/WebSocketChat/refs/heads/develop/docs/images/example1.png)
![Example2](https://raw.githubusercontent.com/MR-98/WebSocketChat/refs/heads/develop/docs/images/example3.png)
![Example3](https://raw.githubusercontent.com/MR-98/WebSocketChat/refs/heads/develop/docs/images/example4.png)

## Tech Stack

**Client:** Angular, Typescript, HTML & SCSS

**Server:** Kotlin, Spring Boot, Spring Boot Security, Spring Data JPA

**Database:** PostgreSQL

**Authorization and authentication:** Keycloak

**Communication:** WebSockets, REST


## Local deployment

To run the project you must have installed and configured:
- Java 17
- Maven
- Node.js
- Docker

Steps:
1. Clone repo
```bash
git clone https://github.com/MR-98/WebSocketChat.git
cd WebSocketChat
```
2. In the `backend` folder, execute
```bash
mvn clean install
```
3. In the `docker` folder, execute
```bash
docker-compose -f .\docker-compose-dev.yml --env-file=dev.env up -d --build
```
4. In the `frontend` folder, execute
```bash
npm i
ng serve --serve-path chat
```
5. The frontend is available at `localhost:4200/chat`

## Roadmap

- i18n
- emoji
- light color theme
- sending photos
- video calls

## License

[MIT](https://choosealicense.com/licenses/mit/)

