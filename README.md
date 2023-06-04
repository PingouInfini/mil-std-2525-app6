
# MIL-STD-2525-APP6


**[EN] APP6 symbols are available in the directory `APP6-icons`.**
**[FR] Les symboles APP6 sont disponibles dans le répertoire `APP6-icons`.**

L'objectif est de récupérer l'ensemble des icones APP6B issus de [milsymbol-APP6b](https://www.spatialillusions.com/milsymbol/docs/milsymbol-APP6b.html)

Ces icones seront stockées dans des répertoires selon leur format : `svg` ou `png`
Un fichier de mapping permet de raccrocher chaque hierarchie, code symbole (SIDC) à chaque fichier APP6.


# Récupération

## Récupération des images

Depuis la page [milsymbol-APP6b](https://www.spatialillusions.com/milsymbol/docs/milsymbol-APP6b.html), à l'aide du plugin de chrome [SVG Export](https://chrome.google.com/webstore/detail/svg-export/naeaaedieihlkmdajjefioajbbdbdjgp?hl=fr)

- On enregistre l'ensemble des images au format `svg` dans le répertoire `rawdata`
  > MIL-STD-2525-APP6\rawdata\svg\svgexport-[x].svg

- On enregistre la page web en html -> Page web COMPLETE également dans `rawdata`
  > MIL-STD-2525-APP6\rawdata\Milsymbol APP6-B.html

Puis :
- On remplace le nom des fichiers grâce au script python

```python
python -m pip install --upgrade pip
pip3 install -r requirements.txt
pip install lib\cairocffi-1.3.0-cp310-cp310-win_amd64.whl
python generate-APP6-icons.py
```


## Renommage en python

Pour chaque **hiérarchie** (ie:`1.X.2.1.1.2.1`), on récupère :
- le **SIDC** (*Symbol identification coding*) associé (ie: `S*A*MFFI--`)
- le **nom** (ie `AIR TRACK - MILITARY - FIXED WING - FIGHTER - INTERCEPTOR`)
- Si la hiérarchie possède une balise `<svg>`, on renomme les fichiers exportés par le plugin par sa hiérarchie