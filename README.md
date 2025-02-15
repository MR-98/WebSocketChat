![Logo](https://raw.githubusercontent.com/MR-98/WebSocketChat/refs/heads/develop/docs/banner.png)
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


## Tech Stack

**Client:** Angular, Typescript, HTML & SCSS

**Server:** Kotlin, Spring Boot, Spring Boot Security, Spring Data JPA

**Database:** PostgreSQL

**Authorization and authentication:** Keycloak

**Communication:** WebSockets, REST


## Uruchomienie lokalne

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

## Roadmap

- i18n
- emoji
- light color theme
- sending photos
- video calls

## License

[MIT](https://choosealicense.com/licenses/mit/)

