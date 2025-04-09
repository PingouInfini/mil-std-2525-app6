import csv
import os


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
            app6_mapping[row['SIDC']] = {
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
                app6_data = app6_mapping.get(app6_c_sidc)

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

    # Ouvrir le fichier de sortie et écrire les lignes triées
    with open(output_file, 'w', newline='', encoding='utf-8') as csvfile:
        # Définir les noms de colonnes dans l'ordre spécifié
        fieldnames = ['APP6-C-Domain', 'APP6-C-FullPath', 'APP6-NAME', 'APP6-NAME-FR', 'APP6-C-HIERARCHY-NAME',
                      'APP6-C-HIERARCHY', 'APP6-B-SIDC', 'APP6-C-SIDC', 'APP6-D-SIDC', 'APP6-D-Domain', 'APP6-D-Entity',
                      'APP6-D-EntityType', 'APP6-D-EntitySubtype']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames, delimiter=';', quotechar='"', quoting=csv.QUOTE_ALL)
        writer.writeheader()

        # Écrire les lignes triées dans le fichier de sortie
        writer.writerows(rows_to_write)


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
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames, delimiter=';', quotechar='"', quoting=csv.QUOTE_ALL)
        writer.writeheader()
        writer.writerows(reader)


def get_informations_from_tsv(tsv_file_path, code):
    # Ouvrir le fichier TSV et lire son contenu
    with open(tsv_file_path, mode='r', encoding='utf-8') as tsv_file:
        tsv_reader = csv.DictReader(tsv_file, delimiter='\t')

        # Chercher la ligne dont le code correspond
        for row in tsv_reader:
            if row['Code'] == code:
                # Si la ligne correspond, extraire les informations Entity, Entity Type, Entity Subtype
                entity = row['Entity']
                entity_type = row['Entity Type']
                entity_subtype = row['Entity Subtype']

                return entity, entity_type, entity_subtype


def update_with_2525d_files(tsv_dir_2525d, legacy_mapping, output_csv):
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
        'Sea subsurface.tsv': '35',
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

    # Charger le fichier legacy_mapping
    with open(legacy_mapping, mode='r', encoding='utf-8') as legacy_file:
        legacy_reader = csv.DictReader(legacy_file, delimiter=';')
        legacy_rows = list(legacy_reader)

    # Créer un dictionnaire pour stocker les correspondances APP6-C-SIDC -> Ligne complète du fichier tsv_dir_2525d
    sidc_dict = {}

    with open(output_csv, mode='r', encoding='utf-8') as mapping_output_file:
        tsv_reader = csv.DictReader(mapping_output_file, delimiter=';')
        for row in tsv_reader:
            sidc = row['APP6-C-SIDC']
            sidc_dict[sidc] = row

    # Créer une liste pour stocker les nouvelles lignes avec la colonne APP6-D-SIDC modifiée
    updated_rows = []

    seen_charlie1st_ten = set()

    # Parcourir chaque ligne du legacy_mapping et chercher les correspondances
    for legacy_row in legacy_rows:
        charlie1st_ten = legacy_row['2525Charlie1stTen']

        # Si la valeur a déjà été traitée, on passe à la suivante pour éviter les doublons
        if charlie1st_ten in seen_charlie1st_ten:
            continue

        seen_charlie1st_ten.add(charlie1st_ten)

        # Modifier le 4e caractère de 2525Charlie1stTen par '*' si nécessaire
        if len(charlie1st_ten) >= 4 and charlie1st_ten[3] == 'P':
            charlie1st_ten = charlie1st_ten[:3] + '*' + charlie1st_ten[4:]

        # Rechercher la correspondance dans le fichier CSV tsv_dir_2525d
        if charlie1st_ten in sidc_dict:
            corresponding_row = sidc_dict[charlie1st_ten]

            # Obtenir le fichier TSV à partir du numéro de DeltaSymbolSet
            delta_symbol_set = legacy_row['2525DeltaSymbolSet']
            if delta_symbol_set in file_prefixes.values():
                # Trouver le fichier correspondant
                tsv_filename = next(key for key, value in file_prefixes.items() if value == delta_symbol_set)
                tsv_file_path = os.path.join(tsv_dir_2525d, tsv_filename)

                code_with_00 = legacy_row['2525DeltaEntity'][:-2] + '00'
                code_with_0000 = legacy_row['2525DeltaEntity'][:-4] + '0000'

                # Ouvrir le fichier TSV correspondant et chercher le code DeltaEntity
                try:
                    entity_info, entity_type_info, entity_subtype_info = get_informations_from_tsv(tsv_file_path,
                                                                                                   legacy_row[
                                                                                                       '2525DeltaEntity'])
                except Exception as e:
                    entity_info = entity_type_info = entity_subtype_info = None

                if entity_subtype_info is not None:
                    # Recherche pour Entity Type (remplacer les 2 derniers chiffres par "00")
                    entity_info, entity_type_info, _ = get_informations_from_tsv(tsv_file_path, code_with_00)
                    entity_info, _, _ = get_informations_from_tsv(tsv_file_path, code_with_0000)
                elif entity_type_info is not None:
                    entity_info, _, _ = get_informations_from_tsv(tsv_file_path, code_with_0000)

            else:
                if legacy_row['Remarks'] == 'Retired':
                    entity_info = entity_type_info = entity_subtype_info = 'Retired'
                else:
                    # Si le DeltaSymbolSet ne correspond à aucun code valide dans file_prefixes
                    entity_info = entity_type_info = entity_subtype_info = 'Not Found'

            # Construire la nouvelle valeur de APP6-D-SIDC
            new_d_sidc = '1003' + legacy_row['2525DeltaSymbolSet'] + '0000' + legacy_row['2525DeltaEntity'] + \
                         legacy_row['2525DeltaMod1'] + legacy_row['2525DeltaMod2']

            # Mettre à jour la colonne APP6-D-SIDC dans la ligne correspondante
            corresponding_row['APP6-D-SIDC'] = new_d_sidc
            # Ajouter les nouvelles informations (Entity, Entity Type, Entity Subtype) dans la ligne
            corresponding_row['APP6-D-Domain'] = os.path.splitext(os.path.basename(tsv_filename))[0]
            corresponding_row['APP6-D-Entity'] = entity_info
            corresponding_row['APP6-D-EntityType'] = entity_type_info
            corresponding_row['APP6-D-EntitySubtype'] = entity_subtype_info

            # Ajouter la ligne modifiée à updated_rows
            updated_rows.append(corresponding_row)

    # Écrire les résultats dans un fichier output_csv
    with open(output_csv, mode='w', newline='', encoding='utf-8') as output_file:
        if updated_rows:
            fieldnames = updated_rows[0].keys()  # Les noms des colonnes sont les clés des dictionnaires
            writer = csv.DictWriter(output_file, fieldnames=fieldnames, delimiter=';', quotechar='"',
                                    quoting=csv.QUOTE_ALL)
            writer.writeheader()
            writer.writerows(updated_rows)


def order_csv_by_column_name(csv_file, column_name):
    # Lire le fichier CSV et charger les données
    with open(csv_file, mode='r', encoding='utf-8') as file:
        reader = csv.DictReader(file, delimiter=';')
        rows = list(reader)

    # Supprimer les guillemets dans les noms de colonnes si présents
    column_name = column_name.strip('"')

    # Vérifier si la colonne existe après avoir retiré les guillemets
    if column_name not in [col.strip('"') for col in reader.fieldnames]:
        raise KeyError(f"Colonne '{column_name}' introuvable dans le fichier CSV.")

    # Fonction pour déterminer si une valeur est numérique ou non
    def convert_to_number(value):
        try:
            # Essayer de convertir en nombre
            return float(value) if '.' in value else int(value)
        except ValueError:
            # Si la conversion échoue, renvoyer la valeur telle qu'elle est (pour le tri alphabétique)
            return value

    # Trier les lignes : d'abord celles avec une valeur non vide dans la colonne spécifiée, puis les autres
    rows_sorted = sorted(
        rows,
        key=lambda x: (
            x[column_name].strip('"') == '',  # Place les lignes avec une valeur vide à la fin
            convert_to_number(x[column_name].strip('"')) if x[column_name].strip('"') else None,  # Tri principal
            x['APP6-C-SIDC'].strip('"')  # Tri secondaire pour les lignes avec valeur vide dans la colonne spécifiée
        )
    )

    # Écrire les lignes triées dans le même fichier CSV
    with open(csv_file, mode='w', newline='', encoding='utf-8') as file:
        writer = csv.DictWriter(file, fieldnames=reader.fieldnames, delimiter=';')
        writer.writeheader()
        writer.writerows(rows_sorted)


# Définir les répertoires d'entrée et le fichier de sortie
tsv_dir_2525c = '../../APP6-C/tsv-tables/2525c'
tsv_dir_2525b = '../../APP6-B/tsv-tables/2525b-change2'
tsv_dir_2525d = '../../APP6-D/tsv-tables/2525d'
legacy_mapping = 'dataset/LegacyMappingTableCtoD.csv'
output_csv = '../MappingCtoD.csv'

# Supprimer le fichier s'il existe
delete_existing_file(output_csv)

# Processus complet
process_2525c_files(tsv_dir_2525c, output_csv)
update_with_2525b_files(tsv_dir_2525b, output_csv)
update_with_2525d_files(tsv_dir_2525d, legacy_mapping, output_csv)
order_csv_by_column_name(output_csv, 'APP6-C-HIERARCHY')

print(f"Fichier '{output_csv}' généré et mis à jour avec succès.")
