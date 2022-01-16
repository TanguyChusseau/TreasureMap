# TreasureMap
[Cloner le repo Git]
[Avoir Maven (version r�cente) install� sur votre machine]
[Avoir une version r�cente (12 ou ult�rieure) de Java install�e sur votre machine

Si vous souhaitez modifier le fichier en entrée, allez dans :
src/main/java/carbon/exercise/trasuremap/service/InputFileReaderService, puis :
- modifier la constante inputFileLocation afin de choisir le répertoire contenant votre fichier,
- modifier la constante inputFileName afin de sélectionner le nom de votre fichier.

Si vous souhaitez modifier le fichier en sortie, allez dans :
src/main/java/carbon/exercise/trasuremap/service/OutputFileReaderService, puis :
- modifier la constante outputFileLocation afin de choisir le répertoire de destination pour le fichier,
- modifier la constante outputFileName afin de sélectionner le nom de votre fichier.


Exercice pratique - La carte aux tr�sors
Guidez les aventuriers en qu�te de tr�sors !

Contexte
Le gouvernement p�ruvien vient d�autoriser les aventuriers en qu�te de tr�sors � explorer les 85 182
km� du d�partement de la Madre de Dios. Vous devez r�aliser un syst�me permettant de suivre les
d�placements et les collectes de tr�sors effectu�es par les aventuriers. Le gouvernement p�ruvien
�tant tr�s � cheval sur les bonnes pratiques de code, il est important de r�aliser un code de qualit�,
lisible, et maintenable (oui, �a veut dire avec des tests) !

Donn�es du probl�me

La carte
La carte de la Madre de Dios est de forme rectangulaire, chaque case ayant la m�me taille. On y
trouve des plaines, des montagnes et des tr�sors.
Les dimensions de la carte sont d�finies dans le fichier d�entr�e de l�exercice par la ligne suivante :
# {C comme Carte} - {Nb. de case en largeur} - {Nb. de case en hauteur}
C - 3 - 4
Par d�faut, toutes les cases de la carte sont des plaines que les aventuriers peuvent traverser sans
encombre. Les cases sont num�rot�es d�ouest en est, de nord en sud, en commen�ant par z�ro.

Les montagnes sont des obstacles infranchissables pour les aventuriers. Chaque montagne de la
carte de la Madre de Dios est �galement indiqu�e dans le fichier d�entr�e de l�exercice par la ligne
suivante :
# {M comme Montagne} - {Axe horizontal} - {Axe vertical}
M - 1 - 1

Enfin, le plus important pour les aventuriers, les tr�sors. Plusieurs tr�sors peuvent �tre pr�sents sur
une m�me case; le nombre de tr�sors sur une m�me case est indiqu� dans le fichier d�entr�e de
l�exercice par la ligne suivante :
# {T comme Tr�sor} - {Axe horizontal} - {Axe vertical} - {Nb. de tr�sors}
T - 0 - 3 - 2

Exemple pour une carte de 3 x 4 :
C - 3 - 4
M - 1 - 1
M - 2 - 2
T - 0 - 3 - 2
T - 1 - 3 - 1

Que l�on peut repr�senter sous la forme suivante :
. . .
. M .
. . M
T(2) T(1) .

Les aventuriers
Un aventurier est caract�ris� par sa position sur la carte et son orientation (nord, sud, ...). Il ne peut
se d�placer que d�une case � la fois, dans la direction d�finie par son orientation. Ceci dit, il peut
changer d�orientation en pivotant de 90� vers la droite ou la gauche. Il d�bute son parcours avec une
orientation (Nord, Sud, Est, Ouest), et une s�quence de mouvements (Avancer, tourner � Gauche,
tourner � Droite) pr�d�finies. Ils ne sont pas montagnards pour un sou, et ne peuvent donc pas
traverser une case montagne.
Exemple de s�quence de mouvement :
AGGADADA deviendra : avancer, tourner � gauche, tourner � gauche, avancer, tourner � droite,
avancer, tourner � droite, avancer.
Les aventuriers pr�sents sur la carte sont indiqu�s dans le fichier d�entr�e de l�exercice sous la forme
suivante :
# {A comme Aventurier} - {Nom de l�aventurier} - {Axe horizontal} - {Axe
vertical} - {Orientation} - {S�quence de mouvement}
A - Indiana - 1 - 1 - S - AADADA

Exemple pour une carte de 3 x 4 :
Au d�part :
. . .
. A .
M . .
. . .

A l�arriv�e, �tape indiqu�e entre parenth�ses :
. . .
. . .
M (1) .
A (2) .

On remarquera que l�aventurier reste bloqu� en orientation Nord, � cause de la montagne. Dans ce
cas pr�cis, l�aventurier ignore les mouvements bloquants et poursuit l�ex�cution de la s�quence.
Si l�aventurier passe par dessus une case Tr�sor, il ramasse un tr�sor pr�sent sur la case. Si la case
contient 2 tr�sors, l�aventurier devra quitter la case puis revenir sur celle-ci afin de ramasser le 2�me
tr�sor.

Il ne peut y avoir qu�un aventurier � la fois sur une m�me case. Les mouvements des aventuriers sont
�valu�s tour par tour. En cas de conflit entre mouvements sur un m�me tour, c�est l�ordre d�apparition
de l�aventurier dans le fichier qui donne la priorit� des mouvements.

Ce qu�il faut r�aliser

Lire le fichier d�entr�e
Le programme doit �tre capable de lire le fichier d�entr�e de l�exercice.
Note : une ligne d�butant par un �#� est un commentaire et doit �tre ignor�e.

Exemple :
C - 3 - 4
M - 1 - 0
M - 2 - 1
T - 0 - 3 - 2
T - 1 - 3 - 3
A - Lara - 1 - 1 - S - AADADAGGA
Que l�on peut repr�senter sous la forme suivante :
. M .
. A(Lara) M
. . .
T(2) T(3) .

Simuler les mouvements des aventuriers
Le programme doit �tre capable d�ex�cuter les mouvements des diff�rents aventuriers en respectant
les contraintes de l�exercice, de g�rer la collecte des tr�sors et de restituer le r�sultat final de la
simulation.
Dans l�exemple pr�c�dent, Lara collecte 3 tr�sors et finit son parcours en (0 - 3).

Ecrire le fichier de sortie
Le programme doit �tre capable d��crire un fichier contenant le r�sultat final de la simulation.
Note : une ligne d�butant par un �#� est un commentaire et doit �tre ignor�e.

Voici le format de sortie :
C - 3 - 4
M - 1 - 0
M - 2 - 1
# {T comme Tr�sor} - {Axe horizontal} - {Axe vertical} - {Nb. de tr�sors
restants}
T - 1 - 3 - 2
# {A comme Aventurier} - {Nom de l�aventurier} - {Axe horizontal} - {Axe
vertical} - {Orientation} - {Nb. tr�sors ramass�s}
A - Lara - 0 - 3 - S - 3

Que l�on peut repr�senter sous la forme suivante :
. M .
. . M
. . .
A(Lara) T(2) .