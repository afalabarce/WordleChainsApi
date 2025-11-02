# Wordle Chains API

## Introducción

Este proyecto implementa un ejemplo de api rest desarrollado con el lenguaje de programación
[Kotlin](https://kotlinlang.org/) y el framework [Ktor](https://ktor.io/), orientado a su uso para la implementación de
un juego de palabras tipo Wordle.

En los siguientes párrafos y secciones se describirá tanto cómo se ha realizado la implementación
así como su despliegue en un servidor en producción (con sus correspondientes problemas).

## Tecnologías utilizadas

Para el desarrollo de este servicio se han utilizado diversas tecnologías que facilitan enormemente
su desarrollo y posterior despliegue, enumerémoslas:

- Lenguaje de programación: Kotlin
- Motor para servicio ReST: Ktor Server
- Motor para pruebas visuales: [Swagger](https://swagger.io/) + [OpenAPI](https://www.openapis.org/)
- Motor de base de datos: [PostgreSQL](https://www.postgresql.org)
- ORM (Object Relational Mapper): [Jetbrains Exposed](https://www.jetbrains.com/exposed/)
- Servidor Web: [nginx](https://nginx.org/)
- Inyección de dependencias: No necesaria, se gestionan los accesos a bd mediante funciones de extensión

## Arquitectura

Este proyecto está basado en una arquitectura [MVC](https://es.wikipedia.org/wiki/Modelo%E2%80%93vista%E2%80%93controlador),
que suele ser de las más usuales en el desarrollo de servicios ReST.

Todo el proyecto se distribuye en tres paquetes principales:
- **common**: Donde albergamos funciones de extensión, definición de plugins utilizados por ktor
  (autenticación, enrutamiento, serialización, etc).
- **database**: donde se define la estructura de la base de datos, atendiendo a las particularidades
en la definición de tipos y dao de Exposed.
- **controllers**: Donde definimos los controladores que van a definir a los endpoints que va a exponer
el servicio ReST.

Existe una carpeta especial llamada **resources** en la que se almacenan los distintos parámetros
de configuración del servicio:
- **application.yaml**: contiene los parámetros de ejecución del servicio
- **Carpeta openapi**: contiene los ficheros yaml utilizados por swagger para mostrar y generar
la web clásica con la que podremos probar nuestro servicio.

## Capas (common, database, controllers)

En esta sección describiremos detalladamente el cómo y el porqué de las decisiones adoptadas
a la hora de llevar a cabo la implementación.

### common

En esta capa definimos todo lo necesario que va a representar y formar parte del core del servicio,
lo tenemos todo dividido en tres archivos:
- **Extensions.kt**: Se implementan funciones de extensión que nos van a aportar claridad al código,
por ejmplo un validador de fechas y una función de desencriptación.
- **PluginsInstall.kt**: En este fichero definimos los distintos plugins que se van a activar en ktor, a fin de que 
nuestro servicio albergue la funcionalidad deseada.<br/>
Para ello, se definen un par de métodos con el objetivo de separar funcionalidad, ya que uno depende
de que el motor de base de datos (a través de Exposed) haya realizado una conexión satisfactoria.<br/>
Cabe destacar el sistema de autenticación, definido mediante el plugin Authentication, el cual muestra 
cómo realizar una autenticación customizada, en la que el token de autenticación viene cifrado (de este modo
nos garantizamos que, si proporcionamos una clave pública a un desarrollador externo, podrá cifrar el token,
en caso contrario, ese cifrado no valdrá y por tanto no se podrá utilizar la parte autenticada).
- **RoutingConfig.kt**: En este archivo definimos todas las rutas que va a exponer el servicio, atendiendo
al protocolo de comunicación, además de establecer una zona autenticada, la cual sólo proporcionará datos
o ejecutará inserciones en la BD en el caso de tener un token de autenticación válido. Este fichero, también expone
un único méetodo que, dependiente de la inicialización satisfactoria de la base de datos expondrá las tablas
de enrutamiento.

### database

En esta capa se gestiona absolutamente todo lo requerido para el acceso CRUD a la base de datos.
Como se comentó en la introducción, todo el proceso de conexión, extracción y actualización de datos
se ha realizado utilizando el ORM Jetbrains Exposed, el cual proporciona una relativa simplicidad a la par
que potencia.