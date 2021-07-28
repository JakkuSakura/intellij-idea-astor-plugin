#!/bin/sh
mkdir -p downloaded
wget -q -nc https://www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/idea/ideaIC/2021.2/ideaIC-2021.2.zip -O downloaded/ideaIC-2021.2.zip
wget -q -nc https://www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/idea/ideaIC/2021.2/ideaIC-2021.2-sources.jar -O downloaded/ideaIC-2021.2-sources.jar
unzip -n downloaded/ideaIC-2021.2.zip -d downloaded/ideaIC-2021.2
