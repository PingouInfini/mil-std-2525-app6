# mil-std-2525-app6

Repository de centralisation des travaux autour de l'APP6, dans ses versions:
- [APP6-B](./APP6-B)
- [APP6-C](./APP6-C)
- [APP6-D](./APP6-D)

Ainsi qu'un fichier de mapping [MappingCtoD/MappingCtoD.csv](./MappingCtoD/MappingCtoD.csv) *(+ script de génération)* pour effectuer des mappings 
des SIDC entre APP6-C et APP6-D.  

-----------------------------------------------------------------------------------------------------------------------

### Synthèse liens utiles

- APP6-C
  - [milsymbol-APP6c](https://www.spatialillusions.com/milsymbol/docs/milsymbol-2525c.html)

- APP6-D
  - [https://explorer.milsymb.net/#/home](https://explorer.milsymb.net/#/home)
  - [https://sidc.milsymb.net/#/APP6](https://sidc.milsymb.net/#/APP6)
  - [https://github.com/yemikudaisi/joint-military-symbology-java](https://github.com/yemikudaisi/joint-military-symbology-java)
  - [https://www.spatialillusions.com/milsymbol/docs/milsymbol-APP6d.html](https://www.spatialillusions.com/milsymbol/docs/milsymbol-APP6d.html)

-----------------------------------------------------------------------------------------------------------------------

### TSV-tables

- Les données de `tsv-tables` sont très majoritairement issues de
"https://github.com/spatialillusions/mil-std-2525/tree/master/tsv-tables".  
- Les meteorological data sont générés à partir de csv issus de 
"https://github.com/2lambda123/oint-military-symbology-xml/blob/master/samples/simple_symbolset_entity_modifier_codes/All_Entities.csv

-----------------------------------------------------------------------------------------------------------------------

### Symbol selector

Les répertoires [APP6-C/symbol-selector](APP6-C/symbol-selector) et [APP6-D/symbol-selector](APP6-D/symbol-selector) 
contiennent des démos d'IHM en java permettant de sélectionner un SIDC et de visualiser le symbole associé.