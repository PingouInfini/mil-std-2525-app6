import csv
import os
import subprocess


def delete_existing_file(filepath):
    """Supprime le fichier s'il existe déjà."""
    if os.path.exists(filepath):
        os.remove(filepath)


def read_tsv_file(filepath):
    """Lit un fichier TSV et retourne les lignes en tant que dictionnaire."""
    with open(filepath, 'r', encoding='utf-8') as file:
        reader = csv.DictReader(file, delimiter='\t')
        return list(reader)


def concatenate_sidc(row):
    """Concatène les valeurs des colonnes spécifiques pour former l'APP6-SIDC."""
    return f"{row['codingscheme']}{row['affiliation']}{row['battledimension']}{row['status']}{row['functionid']}"


def process_2525c_files(input_dir, output_file):
    """Parcourir les fichiers TSV de 2525c et génère le fichier versions-mapping.csv trié par APP6-C-HIERARCHY."""

    # Lire le fichier icon-files-mapping.csv et créer un dictionnaire pour y accéder rapidement
    app6_mapping = {}
    app6_file_path = "dataset/icon-files-mapping.csv"

    with open(app6_file_path, mode='r', newline='', encoding='utf-8') as icon_file:
        icon_reader = csv.DictReader(icon_file, delimiter=';')
        for row in icon_reader:
            # Utiliser "name" du mapping comme clé pour le dictionnaire
            app6_mapping[row['name']] = {
                'fullpath': row['fullpath'],
                'hierarchy': row['hierarchy'],
                'nameFR': row['nameFR']
            }

    rows_to_write = []

    # Parcourir les fichiers dans le répertoire d'entrée
    for filename in os.listdir(input_dir):
        if filename.endswith('.tsv'):
            filepath = os.path.join(input_dir, filename)
            rows = read_tsv_file(filepath)

            # Parcourir chaque ligne dans le fichier TSV
            for row in rows:
                # Concaténer le SIDC de la ligne
                app6_c_sidc = concatenate_sidc(row)

                # Chercher la correspondance dans le fichier icon-files-mapping.csv
                app6_data = app6_mapping.get(row['name'])

                if app6_data:
                    # Récupérer les valeurs correspondantes pour "fullpath", "hierarchy" et "nameFR"
                    fullpath = app6_data['fullpath']
                    hierarchy = app6_data['hierarchy']
                    nameFR = app6_data['nameFR']
                else:
                    # Si aucune correspondance n'est trouvée, mettre des valeurs vides
                    fullpath = ''
                    hierarchy = ''
                    nameFR = ''

                # Ajouter la ligne à la liste des lignes à écrire
                rows_to_write.append({
                    'APP6-C-Domain': os.path.splitext(filename)[0].upper(),
                    'APP6-C-FullPath': fullpath,
                    'APP6-NAME': row['name'],
                    'APP6-NAME-FR': nameFR,
                    'APP6-C-HIERARCHY-NAME': row['hierarchy'].strip(),
                    'APP6-C-HIERARCHY': hierarchy.strip(),
                    'APP6-B-SIDC': '',
                    'APP6-C-SIDC': app6_c_sidc,
                    'APP6-D-SIDC': '',
                    'APP6-D-Domain': '',
                    'APP6-D-Entity': '',
                    'APP6-D-EntityType': '',
                    'APP6-D-EntitySubtype': ''
                })

    # Trier les lignes par "APP6-C-HIERARCHY"
    rows_to_write_sorted = sorted(
        rows_to_write,
        key=lambda x: (
            x['APP6-C-HIERARCHY'] == '',  # Place les lignes avec APP6-C-HIERARCHY vide à la fin
            [int(part) if part.isdigit() else part for part in x['APP6-C-HIERARCHY'].split('.')]  # Tri numérique
            if x['APP6-C-HIERARCHY'] else [],
            x['APP6-C-SIDC']  # Tri secondaire pour les lignes avec APP6-C-HIERARCHY vide
        )
    )

    # Ouvrir le fichier de sortie et écrire les lignes triées
    with open(output_file, 'w', newline='', encoding='utf-8') as csvfile:
        # Définir les noms de colonnes dans l'ordre spécifié
        fieldnames = ['APP6-C-Domain', 'APP6-C-FullPath', 'APP6-NAME', 'APP6-NAME-FR', 'APP6-C-HIERARCHY-NAME',
                      'APP6-C-HIERARCHY', 'APP6-B-SIDC', 'APP6-C-SIDC', 'APP6-D-SIDC', 'APP6-D-Domain', 'APP6-D-Entity',
                      'APP6-D-EntityType', 'APP6-D-EntitySubtype']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames, delimiter=';')
        writer.writeheader()

        # Écrire les lignes triées dans le fichier de sortie
        writer.writerows(rows_to_write_sorted)


def update_with_2525b_files(input_dir, output_file):
    """Parcourir les fichiers TSV de 2525b-change2 et met à jour le fichier versions-mapping.csv."""
    # Lire le fichier versions-mapping.csv existant
    with open(output_file, 'r', newline='', encoding='utf-8') as csvfile:
        reader = list(csv.DictReader(csvfile, delimiter=';'))

    # Traiter les fichiers 2525b
    for filename in os.listdir(input_dir):
        if filename.endswith('.tsv'):
            filepath = os.path.join(input_dir, filename)
            rows = read_tsv_file(filepath)

            for row in rows:
                app6_b_sidc = concatenate_sidc(row)
                # Rechercher si le nom existe déjà
                for csv_row in reader:
                    if csv_row['APP6-C-SIDC'] == app6_b_sidc:
                        csv_row['APP6-B-SIDC'] = app6_b_sidc

    # Réécrire le fichier CSV mis à jour
    with open(output_file, 'w', newline='', encoding='utf-8') as csvfile:
        fieldnames = ['APP6-C-Domain', 'APP6-C-FullPath', 'APP6-NAME', 'APP6-NAME-FR', 'APP6-C-HIERARCHY-NAME',
                      'APP6-C-HIERARCHY', 'APP6-B-SIDC', 'APP6-C-SIDC', 'APP6-D-SIDC', 'APP6-D-Domain', 'APP6-D-Entity',
                      'APP6-D-EntityType', 'APP6-D-EntitySubtype']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames, delimiter=';')
        writer.writeheader()
        writer.writerows(reader)


def update_with_2525d_files_with_javascript():
    # Sauvegarder le répertoire courant
    current_dir = os.getcwd()

    try:
        # Aller dans le répertoire "lib"
        os.chdir("utils")

        # Exécuter le script Node.js dans "lib"
        subprocess.run(["node", "convert-symbology.js"], check=True)
        print("Node.js script 'convert-symbology.js' executed in 'lib'.")

    finally:
        # Revenir au répertoire parent
        os.chdir(current_dir)
        print("Returned to parent directory.")


def update_with_2525d_files(input_dir, csv_file_path):
    update_with_2525d_files_with_javascript()

    # Dictionnaire pour associer les noms de fichiers aux préfixes
    file_prefixes = {
        'Air.tsv': '01',
        'Air missile.tsv': '02',
        'Space.tsv': '05',
        'Space missile.tsv': '06',
        'Land unit.tsv': '10',
        'Land civilian.tsv': '11',
        'Land equipment.tsv': '15',
        'Land installation.tsv': '20',
        'Control Measures.tsv': '25',
        'Sea surface.tsv': '30',
        'Signals Intelligence - Space.tsv': '35',
        'Mine warfare.tsv': '36',
        'Activities.tsv': '40',
        'Meteorological - Atmospheric.tsv': '45',
        'Meteorological - Oceanographic.tsv': '46',
        'Meteorological - Space.tsv': '47',
        'Signals Intelligence - Space.tsv': '50',
        'Signals Intelligence - Air.tsv': '51',
        'Signals Intelligence - Land.tsv': '52',
        'Signals Intelligence - Surface.tsv': '53',
        'Signals Intelligence - Subsurface.tsv': '54',
        'Cyberspace.tsv': '60'
    }

    # Lire le fichier CSV
    with open(csv_file_path, mode='r', newline='', encoding='utf-8') as csv_file:
        csv_reader = list(csv.DictReader(csv_file, delimiter=';'))
        csv_headers = csv_reader[0].keys() if csv_reader else []

    # Parcourir les fichiers TSV de la version 2525d
    for filename in os.listdir(input_dir):
        if filename.endswith('.tsv') and filename in file_prefixes:
            with open(os.path.join(input_dir, filename), mode='r', newline='', encoding='utf-8') as tsv_file:
                tsv_reader = csv.DictReader(tsv_file, delimiter='\t')

                # Initialisation des dernières valeurs non vides
                last_entity = ""
                last_entity_type = ""
                last_entity_subtype = ""

                # Parcourir chaque ligne du fichier TSV
                for row in tsv_reader:
                    code = f"{row['Code']}"

                    # Mettre à jour les dernières valeurs non vides
                    if row['Entity']:
                        last_entity = row['Entity']
                        last_entity_type = ""
                        last_entity_subtype = ""
                    if row['Entity Type']:
                        last_entity_type = row['Entity Type']
                        last_entity_subtype = ""
                    if row['Entity Subtype']:
                        last_entity_subtype = row['Entity Subtype']

                    # Rechercher le code dans le fichier CSV
                    code_found = False
                    for csv_row in csv_reader:
                        app6_d_sidc = csv_row['APP6-D-SIDC']

                        if len(app6_d_sidc) >= 16 and app6_d_sidc[10:16] == code and app6_d_sidc[4:6] == file_prefixes[
                            filename]:
                            # Si le code est trouvé, mettre à jour les colonnes correspondantes
                            csv_row['APP6-D-Domain'] = os.path.splitext(filename)[0].upper()
                            csv_row['APP6-D-Entity'] = last_entity
                            csv_row['APP6-D-EntityType'] = last_entity_type
                            csv_row['APP6-D-EntitySubtype'] = last_entity_subtype
                            code_found = True

                    # Si le code n'est pas trouvé, ajouter une nouvelle ligne dans le CSV
                    if not code_found:
                        new_sidc = f"1003{file_prefixes[filename]}0000{code}0000"
                        new_row = {
                            'APP6-C-Domain': '',
                            'APP6-C-FullPath': '',
                            'APP6-NAME': '',
                            'APP6-NAME-FR': '',
                            'APP6-C-HIERARCHY-NAME': '',
                            'APP6-C-HIERARCHY': '',
                            'APP6-B-SIDC': '',
                            'APP6-C-SIDC': '',
                            'APP6-D-SIDC': new_sidc,
                            'APP6-D-Domain': os.path.splitext(filename)[0].upper(),
                            'APP6-D-Entity': last_entity,
                            'APP6-D-EntityType': last_entity_type,
                            'APP6-D-EntitySubtype': last_entity_subtype
                        }
                        csv_reader.append(new_row)

    # Écrire les données mises à jour dans le fichier CSV
    with open(csv_file_path, mode='w', newline='', encoding='utf-8') as csv_file:
        csv_writer = csv.DictWriter(csv_file, fieldnames=csv_headers, delimiter=';')
        csv_writer.writeheader()
        csv_writer.writerows(csv_reader)

    print(f"Le fichier CSV '{csv_file_path}' a été mis à jour avec succès.")


# Définir les répertoires d'entrée et le fichier de sortie
tsv_dir_2525c = 'dataset/tsv-tables/2525c'
tsv_dir_2525b = 'dataset/tsv-tables/2525b-change2'
tsv_dir_2525d = 'dataset/tsv-tables/2525d'
output_csv = 'result/versions-mapping.csv'

# Supprimer le fichier s'il existe
delete_existing_file(output_csv)

# Processus complet
process_2525c_files(tsv_dir_2525c, output_csv)
update_with_2525b_files(tsv_dir_2525b, output_csv)
update_with_2525d_files(tsv_dir_2525d, output_csv)

print(f"Fichier '{output_csv}' généré et mis à jour avec succès.")
