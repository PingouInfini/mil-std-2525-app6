# Version mapping

Permet de générer un fichier csv de mapping de code APP6-B, APP6-C et APP6-D et diverses informations

# Prérequis

- Python *(testé avec Python 3.13.2)*
- Node *(testé avec Node v23.8.0)*
- NPM *(testé avec NPM 10.9.2)*

Installer les dépendances suivantes:

```
npm install csv-parser json2csv @orbat-mapper/convert-symbology
```

# Usage

Lancer le script python app6-versions-mapping.py`

# Resultat

Un fichier csv `result/versions-mapping.csv` va être généré, avec les entêtes suivantes:

```
APP6-C-Domain;APP6-C-FullPath;APP6-NAME;APP6-NAME-FR;APP6-C-HIERARCHY-NAME;APP6-C-HIERARCHY;APP6-B-SIDC;APP6-C-SIDC;APP6-D-SIDC;APP6-D-Domain;APP6-D-Entity;APP6-D-EntityType;APP6-D-EntitySubtype
```