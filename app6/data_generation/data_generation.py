import csv
import os
import re

rawdata_html_filename = "rawdata/Milsymbol APP6-B.html"
csv_data_output_name = ''


# Read Milsymbol APP6-B.html and generate csv with hierarchy', 'SIDC', 'Name', 'icon-png', 'icon-svg'
def generate_data_from_milsymbol_app6b(mapping_file):
    global csv_data_output_name
    csv_data_output_name = mapping_file

    generate_icon_files_mapping()
    generate_tree_structure(csv_data_output_name)


def generate_icon_files_mapping():
    with open(rawdata_html_filename, "r") as file:
        file_begin = True

        # init files from "rawdata" folder to "APP6-icons" folder
        # copy_files_init_app6_icon_folder()
        init_csv_mapping()

        for line in file:
            # Ignore lines up to ">SPACE</h2>"
            if ">SPACE</h2>" in line:
                file_begin = False

            if not file_begin:
                pattern = r'(\d.X.[\d\.]+)[ ]?(?:.)+?<br>(.+?)<em>SIDC:<\/em>[ ]?(.+?)<\/td>(?:.)+?(<svg(?:.)+?<\/svg>)'
                matches = re.findall(pattern, line)

                if matches:
                    for match in matches:
                        hierarchy = match[0].replace("'", "")
                        fullpath = get_clear_name(match[1])
                        name = re.search(r"(?<=\|)([^|]+)$", fullpath).group(1) \
                            if re.search(r"(?<=\|)([^|]+)$", fullpath) else fullpath
                        sidc = match[2].strip().replace("'", "") if match[2] else None
                        has_svg = bool(match[3])

                        if has_svg:
                            add_csv_row([hierarchy, sidc, fullpath, name, hierarchy + '.png', hierarchy + '.svg'])


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
            tree_string += build_tree_string(value, previous_key+'.'+key, indent + "│   ", is_name)

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
    header = ['hierarchy', 'SIDC', 'fullpath', 'name', 'icon-png', 'icon-svg']
    add_csv_row(header, False)


def add_csv_row(row, append=True):
    global csv_data_output_name
    mode = 'a' if append else 'w'

    with open(csv_data_output_name, mode, newline='') as csv_file:
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
    # Remove all html tags
    clean_string = re.sub('<.*?>', '', modified_string)
    return clean_string
