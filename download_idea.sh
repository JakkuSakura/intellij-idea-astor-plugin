#!/bin/sh
mkdir -p downloaded

platform=ideaIC
version=2020.2.4
fullname=$platform-$version
echo picked $fullname as IDE
wget -nc https://www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/idea/$platform/$version/$fullname.zip -O downloaded/$fullname.zip
wget -nc https://www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/idea/$platform/$version/$fullname-sources.jar -O downloaded/$fullname-sources.zip
unzip -n downloaded/$fullname.zip -d downloaded/$fullname

