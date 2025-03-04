# Hotel Reservation System

## Proyecto: Sistema de Gestión de Reservas de Hoteles

### Objetivo
Aplicar conocimientos de calidad del software mediante la implementación de pruebas unitarias para validar el correcto funcionamiento de los componentes del sistema.

## Introducción
El propósito de este proyecto es comprobar el correcto funcionamiento de los componentes del sistema de información a través de pruebas unitarias. Se realizarán verificaciones conforme al plan de pruebas y se documentarán los resultados obtenidos.

Para cada verificación establecida, se ejecutarán pruebas con casos de prueba asociados, se analizarán los resultados y se registrarán conforme a los criterios del plan de pruebas. Si los resultados no son los esperados, se deberán realizar las correcciones necesarias.

## Descripción
El sistema permite la gestión de reservas de hoteles a través de diez historias de usuario. Se han desarrollado pruebas unitarias para cada función del sistema, utilizando mocks cuando ha sido necesario.

Este proyecto evalúa la capacidad de desarrollar software modular y la correcta implementación de pruebas unitarias. No se requiere una interfaz gráfica.

## Historias de Usuario
- **Registro de Habitaciones**: Como administrador, quiero registrar nuevas habitaciones en el sistema para mantener un inventario actualizado.
- **Búsqueda de Habitaciones**: Como cliente, quiero buscar habitaciones por tipo, precio o disponibilidad.
- **Reserva de Habitaciones**: Como cliente, quiero reservar una habitación para asegurar mi estadía.
- **Cancelación de Reservas**: Como cliente, quiero cancelar una reserva para liberar una habitación.
- **Registro de Clientes**: Como administrador, quiero registrar nuevos clientes en el sistema.
- **Historial de Reservas**: Como cliente, quiero ver mi historial de reservas.
- **Notificaciones de Check-In**: Como administrador, quiero enviar notificaciones de check-in a los clientes.
- **Consulta de Disponibilidad**: Como cliente, quiero ver la disponibilidad de habitaciones en fechas específicas.
- **Generación de Facturas**: Como administrador, quiero generar facturas al realizar el check-out.
- **Reporte de Ocupación**: Como administrador, quiero generar un reporte de ocupación del hotel.

## Requisitos del Proyecto
- Implementar cada historia de usuario.
- Escribir pruebas unitarias para cada función implementada.
- Utilizar mocks para dependencias externas y base de datos.
- Asegurar que todas las pruebas unitarias pasen correctamente.
- Documentar el código y las pruebas unitarias adecuadamente.
- Escribir al menos 2 casos de prueba por método (caso de éxito y caso fallido).

## Tecnologías Utilizadas
- **Java**
- **Maven**
- **PostgreSQL**
- **TestNG**
- **Mockito**
- **HikariCP**
- **dotenv-java**

## Dependencias Principales
El proyecto utiliza las siguientes dependencias en su archivo `pom.xml`:

```xml
<dependencies>
    <!-- PostgreSQL -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.6.0</version>
    </dependency>

    <!-- Manejo de .env -->
    <dependency>
        <groupId>io.github.cdimascio</groupId>
        <artifactId>dotenv-java</artifactId>
        <version>3.0.0</version>
    </dependency>

    <!-- TestNG -->
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>7.7.0</version>
        <scope>test</scope>
    </dependency>

    <!-- Mockito -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.5.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Estructura del Proyecto
```
HotelReservationSystem
└───src
    ├───main
    │   ├───java
    │   │   └───com
    │   │       └───hotel
    │   │           ├───controllers
    │   │           ├───models
    │   │           ├───services
    │   │           └───utils
    │   └───resources
    └───test
        └───java
            └───com
                └───hotel
                    ├───controllers
                    ├───models
                    ├───services
                    └───utils
```

## Instalación y Ejecución

### Prerrequisitos
- Tener instalado **Java 11+**.
- Instalar **Maven**.
- Tener acceso a una base de datos **PostgreSQL**.

### Pasos de Instalación
1. Clonar el repositorio:

   ```sh
   git clone https://github.com/usuario/HotelReservationSystem.git
   ```

2. Ingresar al directorio del proyecto:

   ```sh
   cd HotelReservationSystem
   ```

3. Compilar el proyecto:

   ```sh
   mvn clean install
   ```

4. Ejecutar pruebas:

   ```sh
   mvn test
   ```

## Notas Finales
- El proyecto se desarrolla en grupos de 3 personas.
- La copia de trabajos resultará en una calificación de **0**.
- **Fecha de entrega del plan de pruebas**: Martes 4 de Marzo del 2025.
- Se debe realizar una **presentación del proyecto** y los **casos de prueba**.
- Se debe enviar un archivo comprimido con la documentación y código en el espacio asignado en el **campus virtual**.

