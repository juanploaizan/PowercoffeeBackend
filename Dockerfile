# Etapa de compilación usando una imagen Gradle
FROM gradle:8.2.1 AS build

# Establece el directorio de trabajo dentro del contenedor como /app
WORKDIR /app

# Copia el archivo build.gradle al directorio de trabajo
COPY build.gradle /app

# Ejecuta la tarea 'gradle dependencies' para descargar las dependencias
RUN gradle dependencies

# Copia all the contenido del proyecto al directorio de trabajo
COPY . /app

# Limpia el proyecto utilizando 'gradle clean'
RUN gradle clean

# Compila la aplicación con 'gradle build', excluyendo las pruebas con '-x test'
RUN gradle build -x test

# Etapa final usando una imagen OpenJDK para la ejecución de la aplicación
FROM openjdk:17-jdk-alpine

# Copia el archivo JAR generado en la etapa anterior desde 'build/libs' al directorio raíz del contenedor y nómbralo 'app.jar'
COPY --from=build /app/build/libs/*.jar app.jar

# Expone el puerto 8080 para permitir conexiones a la aplicación
EXPOSE 8080

# Ejecuta la aplicación Java dentro del contenedor usando el comando 'java -jar app.jar'
CMD ["java", "-jar", "app.jar"]
