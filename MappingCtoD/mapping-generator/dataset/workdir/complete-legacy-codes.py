import csv

# Fichiers d'entrée/sortie
legacy_file = "LegacyMappingTableCtoD.csv"
all_id_file = "All_ID_Mapping_Latest.csv"
output_legacy_file = "../LegacyMappingTableCtoD.csv"

# Lire les clés déjà présentes dans LegacyMappingTable
existing_legacy_keys = set()
existing_rows = []
with open(legacy_file, newline='', encoding='utf-8') as f:
    reader = csv.DictReader(f, delimiter=',')
    for row in reader:
        existing_legacy_keys.add(row['2525Charlie1stTen'])
        existing_rows.append(row)

# Fonction pour parser le Name + Mod1Name + Mod2Name
def split_name(full_name, mod1, mod2):
    parts = [p.strip() for p in full_name.split(':')]
    mod2name = ''
    mod1name = ''
    name = full_name.strip()

    if mod2:
        mod2name = ' : '.join(parts[-2:]) if len(parts) >= 2 else parts[-1]
    if mod1:
        if mod2:
            mod1name = ' : '.join(parts[-4:-2]) if len(parts) >= 4 else ''
        else:
            mod1name = ' : '.join(parts[-2:]) if len(parts) >= 2 else parts[-1]
    if mod1 or mod2:
        cut = -2 if mod2 else -2
        if mod1 and mod2 and len(parts) >= 4:
            name = ' : '.join(parts[:-4])
        elif mod1 and not mod2 and len(parts) >= 2:
            name = ' : '.join(parts[:-2])
        elif not mod1 and mod2 and len(parts) >= 2:
            name = ' : '.join(parts[:-2])
    return name.strip(), mod1name.strip(), mod2name.strip()

# Construction des nouvelles lignes
new_rows = []
with open(all_id_file, newline='', encoding='utf-8') as f:
    reader = csv.DictReader(f, delimiter=',')
    for row in reader:
        legacy_key = row['LegacyKey']
        if not legacy_key or len(legacy_key) < 5:
            continue

        modified_key = legacy_key[:1] + '*' + legacy_key[2:3] + 'P' + legacy_key[4:]

        if modified_key in existing_legacy_keys:
            continue

        main_icon = row['MainIcon']
        if len(main_icon) != 8:
            continue

        symbol_set = main_icon[:2]
        entity = main_icon[2:]

        mod1 = row['Modifier1']
        mod2 = row['Modifier2']
        delta_mod1 = mod1[2:4] if mod1 and len(mod1) >= 4 else "00"
        delta_mod2 = mod2[2:4] if mod2 and len(mod2) >= 4 else "00"

        full_name = row['Name']
        name, mod1name, mod2name = split_name(full_name, mod1, mod2)

        new_row = {
            '2525Charlie1stTen': modified_key,
            '2525Charlie': modified_key.ljust(15, '-'),
            '2525DeltaSymbolSet': symbol_set,
            '2525DeltaEntity': entity,
            '2525DeltaMod1': delta_mod1 if mod1 else "00",
            '2525DeltaMod2': delta_mod2 if mod2 else "00",
            '2525DeltaName': name,
            '2525DeltaMod1Name': mod1name,
            '2525DeltaMod2Name': mod2name,
            'DeltaToCharlie': '',
            'Remarks': row.get('Notes', '')
        }
        new_rows.append(new_row)

# Merge des nouvelles lignes avec les existantes
all_rows = existing_rows + new_rows

# Écriture dans le fichier LegacyMappingTableCtoD
with open(output_legacy_file, 'w', newline='', encoding='utf-8') as f:
    if all_rows:
        fieldnames = all_rows[0].keys()
        writer = csv.DictWriter(f, fieldnames=fieldnames, delimiter=';')
        writer.writeheader()
        writer.writerows(all_rows)
    else:
        print("Aucune donnée à écrire.")

print(f"{len(existing_rows) + len(new_rows)} lignes ajoutées à {legacy_file}")