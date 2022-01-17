# TreasureMap
## Cloner le repo Git
## Avoir Maven (version récente) installée sur votre machine
## Avoir une version récente (12 ou ultérieure) de Java installée sur votre machine

## Se rendre via un terminal ou l'invité de commande Windows à la racine du projet, et entrer la commande :
## `mvn clean install`

## Puis, lancez le programme depuis votre IDE en lançant le serveur Tomcat embarqué de Spring Boot sur la classe src/java/fr/carbon/treasuremap/TreasureMapApplication.java.

-> Si vous souhaitez modifier le fichier en entrée, allez dans :
`src/main/java/fr/carbon/treasuremap/TreasureMapApplication`, puis :
- modifier la constante inputFileLocation afin de choisir le répertoire contenant votre fichier,
- modifier la constante inputFileName afin de sélectionner le nom de votre fichier.

-> Si vous souhaitez modifier le fichier en sortie, allez dans :
`src/main/java/fr/carbon/treasuremap/TreasureMapApplication`, puis :
- modifier la constante outputFileLocation afin de choisir le répertoire de destination pour le fichier,
- modifier la constante outputFileName afin de sélectionner le nom de votre fichier.

Exercice pratique - La carte aux trésors
Guidez les aventuriers en quête de trésors !
A partir d'un fichier en entrée contenant les dimensions d'une matrice représentant la carte aux trésors, des montagnes, des trésors et des aventuriers, déplacez les aventuriers sur la carte et ramassez les trésors sur le chemin !
