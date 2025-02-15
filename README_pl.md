![Logo](https://raw.githubusercontent.com/MR-98/WebSocketChat/refs/heads/develop/docs/banner.png)
# WebSocket Chat

Aplikacja webowa pozwalająca na komunikację w czasie rzeczywistym. Użytkownik może dołączać do istniejących pokoi lub tworzyć własne i zapraszać do nich innych. Użycie WebSocket'ów powoduje, że nowe wiadomości są dostarczane niemal natychmiast. Cała komunikacja jest zabezpieczona za pomocą JWT i Keycloak'a tak aby nie dało się wysyłać lub odbierać wiadomości z pokoi do których się nie należy. Aplikacja przypomina w użyciu Facebook Messenger lub Discord.


## Funkcjonalności

- Wysyłanie i odbieranie wiadomości
- Tworzenie, edytowanie, usuwanie pokoi
- Mechanizm zaproszeń do pokoi
- Szyfrowanie treści wiadomości
- Responsywność(wsparcie dla urządzeń mobilnych)


## Demo
[Live Demo](https://mr98.site/chat)


## Tech Stack

**Klient:** Angular, Typescript, HTML & SCSS

**Serwer:** Kotlin, Spring Boot, Spring Boot Security, Spring Data JPA

**Baza danych:** PostgreSQL

**Uwierzytelnianie i autoryzacja:** Keycloak

**Komunikacja:** WebSockets, REST


## Uruchomienie lokalne

Aby uruchomić projekt trzeba mieć zainstalowane i skonfigurowane:
- Java 17
- Maven
- Node.js
- Docker

Kroki:
1. Pobierz repozytorium
```bash
git clone https://github.com/MR-98/WebSocketChat.git
cd WebSocketChat
```

## Plany na kolejne wersje

- i18n
- emoji
- jasny motyw kolorystyczny
- wysyłanie zdjęć
- rozmowy wideo

## Licencja

[MIT](https://choosealicense.com/licenses/mit/)

