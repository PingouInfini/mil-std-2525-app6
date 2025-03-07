import os

import csv


# Fonction pour vérifier si une ligne existe dans le fichier TSV avec certains critères
def line_exists(tsv_writer, tsvfile, entity, entity_type, entity_subtype):
    tsvfile.seek(0)  # Revenir au début du fichier
    tsv_reader = csv.DictReader(tsvfile, delimiter='\t')
    for row in tsv_reader:
        if row['Entity'] == entity and row['Entity Type'] == entity_type and row['Entity Subtype'] == entity_subtype:
            return True
    return False


# Fonction pour modifier le code selon les règles
def modify_code(code, replace_last_digits):
    return code[:-len(replace_last_digits)] + replace_last_digits


# Fonction pour lire le fichier CSV et créer le fichier TSV avec les règles spécifiques
def convert_csv_to_tsv(input_csv, output_tsv):
    # Si le fichier TSV existe déjà, le supprimer
    if os.path.exists(output_tsv):
        os.remove(output_tsv)

    # Ouvrir le fichier CSV en mode lecture
    with open(input_csv, mode='r', newline='', encoding='utf-8') as csvfile:
        csv_reader = csv.DictReader(csvfile, delimiter=';')  # Lire avec ";" comme séparateur

        # Ouvrir le fichier TSV en mode écriture
        with open(output_tsv, mode='w+', newline='', encoding='utf-8') as tsvfile:
            fieldnames = ['Entity', 'Entity Type', 'Entity Subtype', 'Code', 'Remarks']
            tsv_writer = csv.DictWriter(tsvfile, fieldnames=fieldnames, delimiter='\t')
            tsv_writer.writeheader()

            # Parcourir chaque ligne du fichier CSV
            for row in csv_reader:
                entity = row['Entity']
                entity_type = row['EntityType']
                entity_subtype = row['EntitySubType']
                code = row['Code']

                # Cas 0 : Si Entity présents, mais pas et Entity Type et Entity Subtype
                if entity and not entity_type and not entity_subtype:
                    tsv_writer.writerow({
                        'Entity': entity,
                        'Entity Type': "",
                        'Entity Subtype': "",
                        'Code': code,
                        'Remarks': ""
                    })

                # Cas 1 : Si Entity et Entity Type présents, mais pas Entity Subtype
                if entity and entity_type and not entity_subtype:
                    if not line_exists(tsv_writer, tsvfile, entity, "", ""):
                        # Insérer une ligne avec Entity seulement et le code modifié (4 derniers digits remplacés par "0000")
                        modified_code = modify_code(code, "0000")
                        tsv_writer.writerow({
                            'Entity': entity,
                            'Entity Type': "",
                            'Entity Subtype': "",
                            'Code': modified_code,
                            'Remarks': ""
                        })

                    # Ajouter la ligne originale avec seulement Entity Type et le Code
                    tsv_writer.writerow({
                        'Entity': "",
                        'Entity Type': entity_type,
                        'Entity Subtype': "",
                        'Code': code,
                        'Remarks': ""
                    })

                # Cas 2 : Si Entity, Entity Type et Entity Subtype sont présents
                elif entity and entity_type and entity_subtype:
                    # Condition : Si une ligne avec la même Entity, pas de Entity Type et pas de Entity Subtype n'existe pas
                    if not line_exists(tsv_writer, tsvfile, entity, "", ""):
                        # Insérer une ligne avec Entity seulement et le code modifié (4 derniers digits remplacés par "0000")
                        modified_code = modify_code(code, "0000")
                        tsv_writer.writerow({
                            'Entity': entity,
                            'Entity Type': "",
                            'Entity Subtype': "",
                            'Code': modified_code,
                            'Remarks': ""
                        })

                    # Condition : Si une ligne avec ce Entity Type mais sans Entity et sans Entity Subtype n'existe pas
                    if not line_exists(tsv_writer, tsvfile, "", entity_type, ""):
                        # Insérer une ligne avec Entity Type seulement et le code modifié (2 derniers digits remplacés par "00")
                        modified_code = modify_code(code, "00")
                        tsv_writer.writerow({
                            'Entity': "",
                            'Entity Type': entity_type,
                            'Entity Subtype': "",
                            'Code': modified_code,
                            'Remarks': ""
                        })

                    # Ajouter la ligne originale avec seulement Entity Subtype et le Code
                    tsv_writer.writerow({
                        'Entity': "",
                        'Entity Type': "",
                        'Entity Subtype': entity_subtype,
                        'Code': code,
                        'Remarks': ""
                    })


# Appel de la fonction avec le chemin du fichier CSV d'entrée et du fichier TSV de sortie
convert_csv_to_tsv('csv/Meteorological - Atmospheric.csv', '../2525d/Meteorological - Atmospheric.tsv')
convert_csv_to_tsv('csv/Meteorological - Oceanographic.csv', '../2525d/Meteorological - Oceanographic.tsv')
convert_csv_to_tsv('csv/Meteorological - Space.csv', '../2525d/Meteorological - Space.tsv')
