# Wordle Chains API

## Introduction

This project implements a REST API example developed with the [Kotlin](https://kotlinlang.org/) programming language and the [Ktor](https://ktor.io/) framework, aimed at being used for the implementation of a Wordle-style word game.

The following paragraphs and sections will describe both how the implementation was carried out and its deployment on a production server (along with the corresponding challenges).

## Technologies Used

For the development of this service, various technologies have been used that greatly facilitate its development and subsequent deployment. Let's list them:

- Programming Language: Kotlin
- ReST Service Engine: Ktor Server
- Visual Testing Engine: [Swagger](https://swagger.io/) + [OpenAPI](https://www.openapis.org/)
- Database Engine: [PostgreSQL](https://www.postgresql.org)
- ORM (Object-Relational Mapper): [Jetbrains Exposed](https://www.jetbrains.com/exposed/)
- Web Server: [nginx](https://nginx.org/)
- Dependency Injection: Not necessary, database access is managed through extension functions.

## Architecture

This project is based on an [MVC (Model-View-Controller)](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller) architecture, which is usually one of the most common in the development of ReST services.

The entire project is distributed into three main packages:
- **common**: Where we host extension functions and the definition of plugins used by Ktor (authentication, routing, serialization, etc.).
- **database**: Where the database structure is defined, attending to the particularities in the definition of types and DAOs of Exposed.
- **controllers**: Where we define the controllers that will define the endpoints that the ReST service will expose.

There is a special folder called **resources** where the different configuration parameters of the service are stored:
- **application.yaml**: contains the execution parameters for the service.
- **openapi folder**: contains the YAML files used by Swagger to display and generate the classic web interface with which we can test our service.

## Layers (common, database, controllers)

In this section, we will describe in detail the how and why of the decisions made when carrying out the implementation.

### common

In this layer, we define everything necessary that will represent and be part of the service's core. We have everything divided into three files:
- **Extensions.kt**: Extension functions are implemented here to provide clarity to the code, for example, a date validator and a decryption function.
- **PluginsInstall.kt**: In this file, we define the different plugins that will be activated in Ktor, so that our service has the desired functionality.<br/>
  For this, a couple of methods are defined with the objective of separating functionality, as one depends on the database engine (through Exposed) having made a successful connection.<br/>
  It is worth highlighting the authentication system, defined by the Authentication plugin, which shows how to perform a custom authentication where the authentication token is encrypted (thus, we ensure that if we provide a public key to an external developer, they can encrypt the token; otherwise, that encryption will be invalid, and therefore the authenticated part cannot be used).
- **RoutingConfig.kt**: In this file, we define all the routes that the service will expose, according to the communication protocol, as well as establishing an authenticated zone, which will only provide data or execute insertions in the DB if a valid authentication token is present. This file also exposes a single method that, dependent on the successful initialization of the database, will expose the routing tables.

### database

This layer manages absolutely everything required for CRUD access to the database.
As mentioned in the introduction, the entire process of connecting, extracting, and updating data has been done using the Jetbrains Exposed ORM, which provides relative simplicity along with power.
