
# MIL-STD-2525-APP6


**[EN] APP6 symbols are available in the directory `APP6-icons`.**  
**[FR] Les symboles APP6 sont disponibles dans le répertoire `APP6-icons`.**

L'objectif est de récupérer l'ensemble des codes APP6B issus de [milsymbol-APP6b](https://www.spatialillusions.com/milsymbol/docs/milsymbol-APP6b.html) et des svg présents dans `rawdata/symbols``et de construire un répertoire d'images APP6b

Ces icones seront stockées dans des répertoires selon leur format : `svg` ou `png`  
Un fichier de mapping permet de raccrocher chaque hiérarchie, code symbole (SIDC) à chaque fichier APP6.  
Un fichier d'arborescence des codes est également présent dans `APP6-icons`


# Récupération

## Récupération des images

Depuis la page [milsymbol-APP6b](https://www.spatialillusions.com/milsymbol/docs/milsymbol-APP6b.html)

- On enregistre la page web en html -> Page web COMPLETE également dans `rawdata`
  > MIL-STD-2525-APP6\rawdata\Milsymbol APP6-B.html

Puis :
- On remplace le nom des fichiers grâce au script python et on crée les fichiers png associés

## Lancement du programme

### Windows
```bash 
python -m pip install --upgrade pip
pip3 install -r requirements.txt
pip3 install lib\cairocffi-1.3.0-cp310-cp310-win_amd64.whl
python extract-APP6-icons.py
```  

### Linux
```bash 
python3 -m pip install --upgrade pip
pip3 install -r requirements.txt
pip3 install lib/cairocffi-1.6.0-py3-none-any.whl
python3 extract-APP6-icons.py
```


## Renommage/Création des images en python

À partir du fichier **html**, on va créer un csv contenant les informations permettant de mapper la hierarchy, le SIDC et leurs noms respectifs (`icon-files-mapping.csv`), ainsi qu'un fichier d'arborescence des hierarchies (`hierarchy-tree.txt`).  
À partir du fichier **csv** nouvellement créé, on va itérer sur les symboles (images **svg**) présents dans le répertoire `rawdata/symbols`, les renommer en utilisant la hiérarchie, et les stocker dans le répertoire (`APP6-icons`)  
Pour terminer, on génère les **png** équivalents.  

Pour chaque **hiérarchie** (ie:`1.X.2.1.1.2.1`), on récupère :
- le **SIDC** (*Symbol identification coding*) associé (ie: `S*A*MFFI--`)
- le **FullPath** (ie `AIR TRACK - MILITARY - FIXED WING - FIGHTER - INTERCEPTOR`)
- le **Nom** (ie `INTERCEPTOR`)