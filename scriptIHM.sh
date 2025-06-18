#!/bin/bash

javac -d bin src/*.java --module-path /usr/share/openjfx/lib/ --add-modules javafx.controls
java -cp ./bin:/usr/share/java/mariadb-java-client.jar --module-path /usr/share/openjfx/lib/ --add-modules javafx.controls MenuConnexion
