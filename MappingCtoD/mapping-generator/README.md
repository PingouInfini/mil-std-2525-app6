# Version mapping

Permet de générer un fichier csv de mapping de code APP6-B, APP6-C et APP6-D et diverses informations

# Remerciements

Script et données de mapping issus de https://github.com/orbat-mapper/convert-symbology  

# Prérequis

- Python *(testé avec Python 3.13.2)*


# Usage

Lancer le script python `mapping-c-to-d-generator.py`

# Resultat

Un fichier csv `mil-std-2525-app6/MappingCtoD/MappingCtoD.csv` va être généré, avec les entêtes suivantes:

```
APP6-C-Domain;APP6-C-FullPath;APP6-NAME;APP6-NAME-FR;APP6-C-HIERARCHY-NAME;APP6-C-HIERARCHY;APP6-B-SIDC;APP6-C-SIDC;APP6-D-SIDC;APP6-D-Domain;APP6-D-Entity;APP6-D-EntityType;APP6-D-EntitySubtype
```