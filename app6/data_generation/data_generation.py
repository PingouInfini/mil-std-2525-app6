import csv
import os
import re

from tqdm import tqdm

from app6.utils.translater import translate_to_french_from_csv

rawdata_html_filename_app6c = "rawdata/Milsymbol 2525C.html"
rawdata_html_filename_app6b = "rawdata/Milsymbol APP6-B.html"
csv_data_output_name = ''

map_attributes_by_lvl = {}
hierarchy_prefix = ""

distinct_previous_svg = {}


# Read Milsymbol APP6-B.html and generate csv with hierarchy', 'SIDC', 'Name', 'icon-png', 'icon-svg'
def generate_data_from_milsymbol_app6(mapping_file):
    global csv_data_output_name
    csv_data_output_name = mapping_file

    print("### generate_icon_files_mapping_app6c")
    generate_icon_files_mapping_app6c()
    print("### generate_icon_files_mapping_tactical")
    generate_icon_files_mapping_tactical()

    return distinct_previous_svg


def compute_hierarchy_from_fullpath(fullpath):
    global map_attributes_by_lvl
    global hierarchy_prefix

    # dirty...but hey...
    if fullpath == "SPACE TRACK":
        hierarchy_prefix = "1.X."
    if fullpath == "TACTICAL GRAPHICS":
        hierarchy_prefix = "2.X."

    hierarchy = hierarchy_prefix
    pieces = fullpath.split('|')
    cpt = 0

    for piece in pieces:
        cpt += 1
        if cpt not in map_attributes_by_lvl:
            map_attributes_by_lvl[cpt] = set()

        if piece in map_attributes_by_lvl[cpt]:
            hierarchy += str(len(map_attributes_by_lvl[cpt])) + "."
        else:
            keys_to_remove = [key for key in map_attributes_by_lvl.keys() if key > cpt]
            for key in keys_to_remove:
                del map_attributes_by_lvl[key]

            hierarchy += str(len(map_attributes_by_lvl[cpt]) + 1) + "."
            map_attributes_by_lvl[cpt].add(piece)

    hierarchy = hierarchy[:-1] if hierarchy.endswith(".") else hierarchy
    return hierarchy


def generate_icon_files_mapping_app6c():
    with open(rawdata_html_filename_app6c, "r") as file:
        file_begin = True

        # init files from "rawdata" folder to "APP6-icons" folder
        init_csv_mapping()

        for line in file:
            # Ignore lines up to ">SPACE</h2>"
            if ">SPACE</h2>" in line:
                file_begin = False

            if not file_begin:
                pattern = r'(?:.)+?<br>(.+?)<em>SIDC:<\/em>[ ]?(.+?)<\/td>(?:.)+?(<svg(?:.)+?<\/svg>)'
                matches = re.findall(pattern, line)

                if matches:
                    for match in tqdm(matches):
                        fullpath = get_clear_name(match[0])
                        hierarchy = compute_hierarchy_from_fullpath(fullpath)
                        name = re.search(r"(?<=\|)([^|]+)$", fullpath).group(0) \
                            if re.search(r"(?<=\|)([^|]+)$", fullpath) else fullpath
                        name = name.replace("\"", "")

                        nameFR = translate_to_french_from_csv(name).strip()
                        sidc = match[1].strip().replace("'", "") if match[1] else None
                        has_svg = bool(match[2])

                        if fullpath == "TASKS":
                            return

                        if has_svg:
                            add_csv_row(
                                [hierarchy, sidc, fullpath, name, nameFR, hierarchy + '.png', hierarchy + '.svg'])


def generate_icon_files_mapping_tactical():
    with open(rawdata_html_filename_app6b, "r") as file:
        file_begin = True

        for line in file:
            # Ignore lines up to ">SPACE</h2>"
            if ">SPACE</h2>" in line:
                file_begin = False

            if not file_begin:
                pattern = (r'(\d.X.[\d\.]+)[ ]?(?:.)+?<br>(.+?)<em>SIDC:<\/em>[ ]?(.+?)<\/td>(?:<td>)+?(<svg('
                           r'?:.)+?<\/svg>)?')
                matches = re.findall(pattern, line)

                if matches:
                    for match in tqdm(matches):
                        hierarchy = match[0].replace("'", "")

                        values_to_check = ["2.x.1", "2.x.2"]
                        if not any(hierarchy.lower().startswith(value) for value in values_to_check):
                            continue

                        fullpath = get_clear_name(match[1])
                        name = re.search(r"(?<=\|)([^|]+)$", fullpath).group(1) \
                            if re.search(r"(?<=\|)([^|]+)$", fullpath) else fullpath
                        name = get_clear_name(name)
                        nameFR = translate_to_french_from_csv(name)
                        sidc = match[2].strip().replace("'", "") if match[2] else None
                        has_svg = bool(match[3])

                        if has_svg:
                            # a hierarchy has the same symbol, reuse its icon
                            if match[3] in distinct_previous_svg:
                                distinct_previous_svg[match[3]].append(hierarchy)
                            else:
                                distinct_previous_svg[match[3]] = [hierarchy]

                            add_csv_row(
                                [hierarchy, sidc, fullpath, name, nameFR, hierarchy + '.png', hierarchy + '.svg'])


def generate_tree_structure(csv_file):
    tree = {}
    with open(csv_file, 'r') as file:
        reader = csv.reader(file, delimiter=';')
        next(reader)  # Skip the header row
        for row in reader:
            hierarchy = row[0]
            name = row[3]
            folders = hierarchy.split('.')
            current_level = tree
            for folder in folders:
                if folder not in current_level:
                    current_level[folder] = {}
                current_level = current_level[folder]
            current_level[name] = {}

    tree_string = build_tree_string(tree)
    save_tree_to_file(tree_string, os.path.join("APP6-icons", "hierarchy-tree.txt"))


def build_tree_string(tree, previous_key='', indent='', is_name=False):
    tree_string = ''
    for key, value in tree.items():
        if key.isdigit() or key == 'X':  # value of the hierarchy
            if not is_name:
                tree_string += f"\n"
            tree_string += f"{indent}├───{key}"
            is_name = False
        else:  # Name of the hierarchy
            tree_string += f"\t-\t{key}\t-\t[{previous_key[1:]}]\n"
            is_name = True
        if value:
            tree_string += build_tree_string(value, previous_key + '.' + key, indent + "│   ", is_name)

    last_index = tree_string.rfind("├")
    if last_index != -1:
        tree_string = tree_string[:last_index] + "└" + tree_string[last_index + 1:]

    if indent:
        tree_string += indent[:-4] + "└───\n"
    return tree_string


def save_tree_to_file(tree_string, file_path):
    lines = []
    for line in tree_string.split('\n'):
        line = line.strip()  # Supprimer les espaces en début et fin de ligne
        if any(char.isalnum() for char in line):  # Vérifier si la ligne contient des caractères alphanumériques
            lines.append(line)

    with open(file_path, 'w', encoding='utf-8') as file:
        file.write('\n'.join(lines))


def init_csv_mapping():
    # Add the CSV file header
    header = ['hierarchy', 'SIDC', 'fullpath', 'name', 'nameFR', 'icon-png', 'icon-svg']
    add_csv_row(header, False)


def add_csv_row(row, append=True):
    global csv_data_output_name
    mode = 'a' if append else 'w'

    with open(csv_data_output_name, mode, encoding="utf-8", newline='') as csv_file:
        writer = csv.writer(csv_file, delimiter=';')
        writer.writerow(row)


def extract_svg(string):
    regex = r'(<svg[\w\d=":/.><,\-# ()	]+)'
    found = re.search(regex, string)

    if found:
        svg_value = found.group(1)
        return svg_value
    else:
        return None


def get_clear_name(input_string):
    # Replace all but the last <br> with |.
    modified_string = re.sub(r"(<br>(?![^<]*$))", '|', input_string)
    modified_string = modified_string.replace("\"", "")
    # Remove all html tags
    clean_string = re.sub('<.*?>', '', modified_string)
    clean_string = clean_string.replace("&amp;", "&")

    if clean_string.startswith("GROUND TRACK EQUIPMENT|") or clean_string == "GROUND TRACK EQUIPMENT":
        clean_string = clean_string.replace("GROUND TRACK EQUIPMENT", "GROUND TRACK|EQUIPMENT", 1)

    if clean_string.startswith("INSTALLATION|") or clean_string == "INSTALLATION":
        clean_string = clean_string.replace("INSTALLATION", "GROUND TRACK|INSTALLATION", 1)

    return clean_string
