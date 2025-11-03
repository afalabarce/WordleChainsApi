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

Esta capa está organizada en dos capas principales:

- **common**: En la que implementamos la conexión a la base de datos a través de las configuraciones del
[resources/application.yaml](server/src/main/resources/application.yaml), así como un método que nos
garantice que todas las transacciones a la base de datos se realicen en una corrutina.
- **features**: En esta capa, implementamos para cada feature las definiciones de las tablas de exposed, así
como sus dao. Si bien el objetivo de este README no es mostrar un tutorial sobre Exposed, vamos a mostrar
un ejemplo, que involucre una entidad maestro detalle (quizá de lo más complejo).<br/>
Veamos tanto los ficheros que definen las tablas, como los mapeadores que se van a utilizar a fin de 
mapear los datos desde / hasta las entidades expuestas en los controladores:
```kotlin
object ExposedDailyGame: IntIdTable(
    name = "daily_game",
    columnName = "daily_id"
){
    val date = long(name = "date")
    val language = varchar(name = "language", length = 10)
}

class DailyGameDao(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<DailyGameDao>(ExposedDailyGame)
    var date by ExposedDailyGame.date
    var language by ExposedDailyGame.language
    val words: SizedIterable<DailyGameWordDao> by DailyGameWordDao referrersOn ExposedDailyGameWord.dailyId

}
```
```kotlin
object ExposedDailyGameWord: IntIdTable(
    name = "daily_game_words",
    columnName = "daily_game_word_id"
) {
    val dailyId = reference(name = "daily_id", refColumn = ExposedDailyGame.id)
    val wordId = reference("word_id", refColumn = ExposedWord.id)
    val linkedWordId = reference("linked_word_id", refColumn = ExposedWord.id).nullable()
    val linkingPosition = integer(name = "linking_position").nullable()
    val linkedWordPosition = integer(name = "linked_word_position").nullable()

    init {
        foreignKey(
            dailyId to ExposedDailyGame.id,
            wordId to ExposedWord.id,
            linkedWordId to ExposedWord.id
        )
    }
}

class DailyGameWordDao(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<DailyGameWordDao>(ExposedDailyGameWord)
    var dailyId by ExposedDailyGameWord.dailyId
    var word: WordDao by WordDao referencedOn ExposedDailyGameWord.wordId
    var linkedWord by ExposedDailyGameWord.linkedWordId
    var linkingPosition by ExposedDailyGameWord.linkingPosition
    var linkedWordPosition by ExposedDailyGameWord.linkedWordPosition
}
```
Como podemos ver, tenemos dos entidades, la maestra (ExposedDailyGame) y la que carga el detalle de la 
misma (ExposedDailyGameWord), la cual como podemos apreciar se relaciona con la primera a través de 
una relación uno-a-muchos, garantizando que una palabra, en base a esta relación solo puede repetirse
una vez en una partida diaria.

Si nos fijamos detenidamente, una configuración de exposed para base de datos consta de dos partes:

- La definición de la tabla de forma lógica (que "coincidirá" con la representación de la base de datos).
- El Dao, que será el que implemente la lógica CRUD, así como el mapeo de datos de un registro, incluyendo
la carga automática de los datos relacionados.

Por tanto, y en resumen, la capa de database se compone, visto lo anterior, de:

- La definición de la tabla, mediante entidades *IdTable (en este caso es una entidad cuya clave primaria
es un entero)
- El Dao, en el que se cargarán los datos extraidos de cada registro. Este dao expone métodos para extraer
y/o agregar información de la propia base de datos. 
- Las listas (cuando obtenemos más de un registro, se definen como **SizedIterable**)

### controllers

En esta capa, de nuevo organizando todo por features para mantener coherencia, se organiza el código
de la siguiente forma:

- Carpeta **models**: en esta carpeta definimos los modelos de dominio, es decir, las data class que van 
a ser emitidas cuando se nos realice una petición. En esta carpeta no tiene nada de especial.
- Carpeta **repository**: En la que implementaremos los repositorios desde los cuales obtendremos la
información de la base de datos y la mapearemos contra entidades de dominio. </br>

Como ejemplo mostraremos un repositorio en el que se cargan entidades complejas (siguiendo el mismo
ejemplo de la sección **database**).<br/>
```kotlin
suspend fun Database.getDailyGame(date: LocalDate, language: String): DailyGame? = suspendTransaction {
    DailyGameDao.find {
        ExposedDailyGame.date eq date.toEpochDays() and(
            ExposedDailyGame.language eq language
        )
    }.firstOrNull()?.let(::dailyGameDaoToModel)
}
```

Fijémonos en la extracción de datos, como vemos utilizamos el Dao definido en database, del cual
usamos la función de extensión *find*, gracias a la cual podemos consultar información aplicando 
filtros de forma relativamente sencilla. Como podemos ver por su sintaxis, en el lambda de find
definimos el método de búsqueda, aplicando funciones tanto infix (eq, not eq, etc) como no infix
como sería el caso de and (or y similares) la cual engloba en si misma los criterios que van a aplicar
adicionalmente. Para terminar, si hemos obtenido datos, se ejecuta el mapeo (en el bloque let).

El código anterior generará internamente varias sentencias SQL que cargarán todos los datos necesarios
en las entidades. Grosso modo (y probablemente me deje alguna) las sentencias que se generarán serán
las siguientes:

```sql
select dg.* from daily_game 
where date=:date and language=:language

select dgw.* from daily_game_words dgw
inner join daily_game dg on dg.daily_id=dgw.daily_id
where dg.date=:date and dg.language=:language

select w.* from words w
inner join daily_game_words dgw on dgw.word_id=w.id
inner join daily_game dg on dg.daily_id=dgw.daily_id
where dg.date=:date and dg.language=:language

select w.* from words w
inner join daily_game_words dgw on dgw.linked_word_id=w.id
inner join daily_game dg on dg.daily_id=dgw.daily_id
where dg.date=:date and dg.language=:language
```

- Fichero **Controller.kt**: Este fichero es el responsable de ejecutar las peticiones realizadas al
servicio. Como podemos apreciar en el código siguiente, creamos cada controlador extendiéndolo desde 
RoutingCall, a fin de simplificar la configuración de los enrutamientos. Siguiendo al hilo de los ejemplos
anteriores, mostramos el controlador que extrae la información de dailygame:
```kotlin
suspend fun RoutingCall.getDailyGame(database: Database){
    val dailyGame = this.parameters["dailyGame"]
    val language = this.parameters["language"]

    if (dailyGame.isValidDate() && language.orEmpty().isNotEmpty()) {
        val localDate: LocalDate = LocalDate.parse(dailyGame!!)
        val dailyGame = database.getDailyGame(localDate, language!!)
        if (dailyGame != null)
            respond(dailyGame)
        else
            respond(status = HttpStatusCode.NotFound, "Daily game not found")
    }else {
        this.respond(status = HttpStatusCode.BadRequest, "Invalid date format")
    }
}
```

¿Por qué lo implementamos así? tan solo debemos echar un vistazo a cómo hemos definido las rutas
en nuestro fichero [RoutingConfig.kt](server/src/main/kotlin/dev/afalabarce/wordlechains/api/common/RoutingConfig.kt):
```kotlin
authenticate {
    get(path = "/v1/dailyGame/{dailyGame}/{language}") {
        call.getDailyGame(database)
    }

    post(path = "/v1/hallOfFame") {

        call.updateHallOfFame(database)
    }
}
```

Además hemos aprovechado el ejemplo para mostrar cómo integrar peticiones en un bloque autenticado,
es decir, que las peticiones dentro de authenticate, automáticamente requieren autenticación,
si no emitimos un token mediante la cabecera Authorization, se emitirá de forma automática un error 401
(Unauthorized).

Si nos fijamos detenidamente, podremos ver que al configurar la ruta pasamos los parámetros mediante { } 
en la definición de la misma. Existen otro métodos para definir enrutamientos usando tipos seguros,
para más info, remitirse a la documentación de KTor.

## Despliegue

El despliegue del servicio que hemos creado es relativamente "sencillo", eso sí, debemos tener cuidado en
algunos aspectos importantes.

El proyecto nos proporciona una serie de tareas de gradle que nos van a facilitar enormemente el 
proceso de distribución, para ello, tan solo debemos fijarnos en el árbol de tareas, concretamente en la 
sección **distribution**:

![Gradle tasks tree](art/gradle_tasks_tree.png)

![Gradle tasks tree II](art/gradle_tasks_tree_ii.png)

Para información sobre el uso de las distintas tareas, la información está disponible
en el sitio de [documentación de ktor](https://ktor.io/docs/welcome.html)

Para nuestro ejemplo, hemos utilizado la tarea **distTar** ya que tar es un 
estandar en cualquier unix (macOS, linux, bsd,...).

El resultado tras ejecutar la tarea es el siguiente (flechas rojas y azules):

![distribution path](art/distribution_path.png)

Si nos fijamos, se crean en la carpeta de distributions los tar / zip necesarios
ya sean zips o tar normales o shadow (en último término un fatjar con todo dentro), el resultado 
es el mismo... la creación de una carpeta con la estructura ordenada (**PERO INCOMPLETA PARA NUESTRAS NECESIDADES**)
y preparada para su despliegue en un servidor.

Por ejemplo, al descomprimir el tar generado (llamado con el formato <nombre del proyecto>-<version del proyecto>.tar)
obtenemos lo siguiente:

![tar_distribution_content](art/tar_distribution_content.png)

En cambio, el contenido tras descomprimir el shadow (llamado con el formato <nombre del proyecto>-shadow-<version del proyecto>.tar)

obtenemos lo siguiente:

![shadow_tar_distribution.png](art/shadow_tar_distribution.png)

Como vemos en cada captura de pantalla, la estructura es sustancialmente diferente ya que, mientras que
con una distribución normal está todo separado en las distintas librerias (las externas al proyecto son 
totalmente independientes), en una distribution shadow, todo está incluido en un mismo jar. 

Cada filosofía de distribución tiene sus ventajas e inconvenientes:

Un shadow jar es más pesado para una pequeña actualización, pero nos garantizamos que no nos falta nada en
caso de agregar nuevas dependencias, por el contrario, un jar tan grande puede suponer un problema de rendimiento
en el caso de que crezca de forma importante.  Como podemos deducir,las ventajas e inconvenientes de una 
instalación normal van por el caso contrario, si tenemos una actualización solo debemos actualizar el server-1.0.0.jar
y poco más. 

### Caso de uso real. Despliegue en un servidor Linux

Este servicio está disponible en la url [https://wordleapi.afalabarce.dev](https://wordleapi.afalabarce.dev) con su
correspondiente swagger con el que realizar pruebas.

Este servicio está hospedado en una máquina con las siguientes características generales:

- Sistema Operativo: Debian Linux.
- JDK: Versión mínima 21.
- Servidor Web: nginx.
- Motor de Base de Datos: PostgreSql

En los siguientes apartados se va a mostrar cómo se ha desplegado el proyecto, incluidos los problemas 
encontrados y las soluciones adoptadas.

#### Sistema operativo Linux

En este punto no hay mucha historia que contar, ya que es una instalación estándar del sistema operativo.
Tan solo tener en cuenta que en un servicio DNS se deben definir las entradas necesarias para soportar los 
distintos servicios que pueda ofrecer el servidor, tales como webmail, el propio api rest, otras api rests, etc.

El definir distintos nombres dns para el servidor nos va a ayudar a hacer más eficiente nuestro servicio web
ya que vamos a poder atomizar, simplificar y aislar bastante un servicio de otro.

Crearemos un usuario que será el responsable de ejecutar el servicio, sin privilegios, para evitar posibles
problemas de seguridad. Este usuario tendrá como home la carpeta que elijamos para albergar el servicio, así,
la gestión de permisos será más sencilla.

Una vez cargado en nuestro servidor el tar generado, lo desempaquetaremos en la ruta que estimemos necesario,
por ejemplo y por ser ordenados en /srv/wordleappService.

Una vez descomprimido, deberemos configurar nuestro servicio para que se ejecute al inicio del sistema operativo,
linux nos proporciona un método "sencillo" para realizar esta tarea, que deberemos realizar como root. Veámoslo por pasos:

- **Creamos la carpeta, y descomprimimos el tar**. No hay mucho que explicar, por ejemplo, crearemos la carpeta en
/srv/wordleappService.
- **Localización del "ejecutable" del servicio**. En la carpeta recien creada, veremos que tenemos **un fichero llamado server en la carpeta bin**.
Este fichero es el que ejecutará nuestro servicio y expondrá el puerto configurado en application.yaml.
- Crearemos un fichero para el servicio. ya que debian soporta [systemd](https://systemd.io/) para la ejecución
de servicios del sistema, será el que utilicemos, para ello:
  - Nos movemos a la carpeta /etc/systemd/system.
  - Creamos un nuevo archivo llamado wordleapi.service (por ejemplo utilizando nano, pico, vi, o el editor de texto
  con el que nos sintamos más cómodos). Y agregamos el siguiente contenido: 
```bash
[Unit]
Description=WordleApiService
After=network.target

[Service]
User=wordleapi
WorkingDirectory=/srv/wordleappService/
ExecStart=/srv/wordleappService/bin/server
Restart=always
RestartSec=3

[Install]
WantedBy=default.target
```
- Activamos el servicio en systemd. Ejecutaremos el comando: `systemctl enable wordleapi`
- Lanzamos el servicio para que esté disponible y escuche en el puerto definido en la configuración.
  Ejecutaremos el siguiente comando: `systemctl start wordleapi`

#### Servidor web. nginx

Hemos optado por el despliegue del servicio utilizando nginx, el cual a día de hoy creemos que es un estándar
tanto en rendimiento como seguridad. Debemos recordar que nginx NO es un servidor web al uso 
(como lo es [Apache](https://httpd.apache.org/)) sino que se entiende que nginx es un proxy web, más enfocado
en encapsular cualquier tipo de proyecto que exponga un puerto, encapsulando seguridad, ssl, etc.

nginx es una gran opción para el despliegue de servicios basados en ktor ya que nuestro servicio se ejecuta directamente
exponiendo un puerto. En nuestro caso, y por seguridad, sólo se expone un puerto concreto no estándar contra localhost, por lo
que el acceso a dicho puerto y configuración está absolutamente restringido, dejando la responsabilidad de la comunicación 
desde el exterior a nginx. Utilizando comunicación ssl, garantizando el cifrado de la comunicación y descargando al servicio de 
esa carga y responsabilidad.

Cada sitio en nginx se basa en la configuración de ficheros que van a encapsular la configuración de acceso a cada sitio.

![nginx_folder_content.png](art/nginx_folder_content.png)

Para nuestro caso concreto, veamos la configuración de nginx para nuestro servicio (situado en la carpeta
sites-available):

```bash
server {
        listen 80;
        server_name wordleapi.afalabarce.dev;
        return 301 https://$server_name$request_uri;
}
server {

        # SSL configuration
        #
        listen 443;
        
        server_name wordleapi.afalabarce.dev;
        large_client_header_buffers 4 32k;
        client_max_body_size 50M;
        charset utf-8;

        access_log /srv/wordleappService/logs/nginx.access.log;
        error_log /srv/wordleappService/logs/nginx.error.log;

        # TLS: Configure your TLS following the best practices inside your company
    # Other configurations

    # API
    location /v1 {
        proxy_set_header Host $http_host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Scheme $scheme;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_pass http://127.0.0.1:8085/v1;
        proxy_redirect off;
    }

    # swagger
    location / {
        proxy_set_header Host $http_host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Scheme $scheme;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_pass http://127.0.0.1:8085/swagger;
        proxy_redirect off;
    }

    location /swagger {
        proxy_set_header Host $http_host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Scheme $scheme;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_pass http://127.0.0.1:8085/swagger;
        proxy_redirect off;
    }
}

```

Como podemos apreciar, enrutamos tanto el acceso a la api como los accesos a swagger de forma directa,
usando location. Además forzamos una redirección en el caso de utilizar una llamada no segura.
De este modo, ese puerto 8085 no está expuesto al gran público.

En base a la configuración anterior, vemos que debemos crear una carpeta logs ya que de lo contrario, 
no sabremos que sucede (y aparte nos dará error al iniciar el servicio de nginx).

Una vez configurado el fichero en sites-available, tan solo debemos activarlo en la carpeta sites-enabled,
creando un link simbólico de la siguiente forma:

```bash
sites-enabled # ln -s ../sites-available/wordlechainsapi wordlechainsapi
```
Una vez configurado nginx, y lanzado el servicio (indicado el proceso en el paso anterior), ya sólo nos queda
reiniciar nginx para lo que ejecutaremos el siguiente comando: `systemctl restart nginx`.

Si hemos configurado todo correctamente, el servicio estará disponible y por consola no se nos indicará ningún 
error. En el caso de que exista algún error de configuración en los ficheros del sitio, podremos ver cuál es el error
ejecutando journalctl, un poco de información en el [siguiente hilo](https://unix.stackexchange.com/questions/225401/how-to-see-full-log-from-systemctl-status-service),

Con todo esto, ya tendríamos desplegado nuestro servicio, pero como veremos al probarlo, no va a funcionar
como esperamos ya que hay cosas que "no están".

## Problemas encontrados

### Ejecución del servicio

Cuando desarrollamos en nuestra máquina en local disponemos de una base de datos quizá con menos seguridad que
la que podamos tener en nuestro servidor (por ejemplo, sin contraseña). Y al desplegar, nos damos cuenta de que
se nos ha olvidado configurar los parámetros correspondientes. Entonces, buscamos la carpeta resources en la que
tenemos nuestro application.yaml...

Pero nos damos cuenta de que sólo tenemos las carpetas bin y lib, pero no existe ninguna carpeta resources.

Este es el primer problema que nos encontramos, intentamos darle solución creando la carpeta resources y copiando en 
ella los mismos ficheros que tenemos en nuestro proyecto, el application.yaml y la carpeta openapi y demás.

![remote_server_folder.png](art/remote_server_folder.png)

Con esto podríamos pensar que ya lo tenemos todo solucionado, de nuevo, reiniciamos el servicio
(tanto el de wordle api como nginx) usando el comando de consola `systemctl restart wordleapy && systemctl restart nginx`,
pero nos fijamos que sigue fallando pese a que hemos configurado todo como en el entorno de desarrollo...

**¿qué está sucediendo?**

El problema consiste en que cuando la tarea de gradle que utilizamos para generar el tar, el zip, o lo que sea
**encapsula dentro del jar del server-1.0.0.jar los ficheros de configuración que tengamos en nuestra máquina**,
impidiendo que el servicio se lance y utilice los recursos de nuestro servidor en producción.

**¿cómo hemos solucionado este problema?**

De una forma sencilla y pragmática hemos solucionado el problema observando las características del
proceso servidor que se lanza.

Fijémonos en el contenido del script que ejecuta el servicio (en la carpeta bin/server), si lo editamos
y lo analizamos, veremos que al final se lanza la llamada al jar... y mirando la documentación de ktor
vemos que se le puede pasar un parámetro en concreto que será el que solucionará nuestros problemas...
El parámetro en cuestión es **-config**... el cual nos permite indicar una ubicación alternativa para
el fichero de configuración del servicio.

Así pues, localizaremos en el fichero server de la carpeta bin la siguiente línea (es la última, o debe serlo):
```bash
exec "$JAVACMD" "$@"
```

y la modificaremos como sigue:

```bash
exec "$JAVACMD" "$@" "-config=$APP_HOME/resources/application.yaml"
```

A continuación guardamos los cambios y de nuevo, volvemos a reiniciar el servicio con el comando
`systemctl restart wordleapy && systemctl restart nginx`

### Swagger / openapi

Otro problema, relativo al anterior nos lo encontramos con swagger, el cual tiene una problemática
similar... es incapaz de alimentarse de los datos de resources (en una configuración estándar),
ya que están en el jar, dando errores. En nuestro servicio, hemos optado por agregar en Application.yaml
un parámetro extra indicando la ruta física de la carpeta de openapi, de este modo en el código (lo podemos
ver en el RouteConfig.kt) forzaremos a que coja directamente los datos para pintar correctamente la
página swagger.

El gran problema encontrado con swagger / openapi es que, si no tenemos una licencia ultimate para 
[Intellij Idea](https://www.jetbrains.com/idea/) no tenemos posibilidad de generar la documentación
de forma automatizada, por lo que **deberemos generar el fichero documentation.yaml (en resources/openapi/)
a mano**, lo cual, si bien no es complicado, ya que Android Studio es capaz de autocompletar relativamente
bien los parámetros que necesitamos... sí es preciso tener una nociones básicas de cómo funcionan tanto
la sintaxis de yaml como la estructura que requiere openapi (solventable con la documentación propia de 
openapi, como apoyándonos en agentes IA).

## Conclusión final

En mi caso, siempre había optado por el desarrollo de servicios rest utilizando [.net core](https://learn.microsoft.com/es-es/aspnet/core/overview?view=aspnetcore-9.0)
pero probando ktor con kotlin, creo que a día de hoy poco o nada tiene ktor que envidiar al framework 
creado por microsoft. 

Es sencillo, además de que, si necesitamos crear un proyecto multiplataforma, incluso la 
lógica de negocio podríamos aprovecharla, escribiendo menos código. 

Creo que este pequeño proyecto puede dar bastante luz (con sus defectos, que serán muchos) y ser útil
para todo aquel desarrollador que esté planteándose dar el salto a crear sus propios servicios rest 
utilizando esta tecnología.