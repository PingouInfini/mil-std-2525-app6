import { convertLetterSidc2NumberSidc } from "@orbat-mapper/convert-symbology";
import fs from 'fs';
import csv from 'csv-parser';
import { parse } from 'json2csv';

// Le chemin d'accès au fichier CSV
const inputFilePath = '../result/versions-mapping.csv';

// Tableau pour stocker les données du fichier CSV
const data = [];

// Lire le fichier CSV avec un séparateur de colonnes personnalisé
fs.createReadStream(inputFilePath)
  .pipe(csv({ separator: ';' })) // Utiliser ';' comme séparateur
  .on('data', (row) => {

    // Pour chaque ligne, récupérer la valeur de "APP6-C-SIDC" et la convertir
    const app6CSidc = row['APP6-C-SIDC'];

    if (app6CSidc) {
      try {
        const { sidc: app6DSidc } = convertLetterSidc2NumberSidc(app6CSidc);
        // Ajouter la colonne "APP6-D-SIDC" avec le résultat de la conversion
        row['APP6-D-SIDC'] = app6DSidc;
      } catch (error) {
        console.error(`Erreur lors de la conversion pour ${app6CSidc}:`, error);
        row['APP6-D-SIDC'] = ''; // Laisser vide en cas d'erreur
      }
    } else {
      row['APP6-D-SIDC'] = ''; // Laisser vide si aucune valeur à convertir
    }

    data.push(row); // Stocker la ligne modifiée dans le tableau
  })
  .on('end', () => {
    // Conversion du tableau modifié en CSV avec le bon séparateur
    const csvOutput = parse(data, { delimiter: ';' });

    // Écrire le contenu mis à jour dans le même fichier CSV
    fs.writeFileSync(inputFilePath, csvOutput, 'utf-8');
  });
