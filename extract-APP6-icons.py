import csv
import os
import re
import shutil

from app6.data_generation.data_generation import generate_data_from_milsymbol_app6
from app6.utils.file_manager import copy_and_rename_file, clear_files_in_directory
from app6.utils.image_manager import create_png_equivalents

mapping_file = os.path.join("APP6-icons", "icon-files-mapping.csv")
symbol_code = {}


def main():
    distinct_previous_svg = generate_data_from_milsymbol_app6(mapping_file)
    manage_svg_files(distinct_previous_svg)
    # Creating png images from svg files
    create_png_equivalents("APP6-icons/svg", "APP6-icons/png")

    # update data for symbol selector (csv and png)
    shutil.copy(os.path.join("APP6-icons", "icon-files-mapping.csv"),
                os.path.join("symbol-selector", "src", "resources", "icon-files-mapping.csv"))
    clear_files_in_directory(os.path.join("symbol-selector", "src", "images", "icons"), [".png", ".svg"])
    put_all_png_in_directory(os.path.join("APP6-icons", "png"),
                             os.path.join("symbol-selector", "src", "images", "icons"))


def manage_svg_files(distinct_svg):
    global symbol_code
    global mapping_file
    svg_directory = os.path.join("rawdata", "symbols")
    svg_app6b_directory = os.path.join("rawdata", "app6-svg")
    codes = {}

    # Read the mapping file
    with open(mapping_file, "r") as f:
        csv_reader = csv.reader(f, delimiter=";")
        next(csv_reader)  # Ignore the CSV header
        for line in csv_reader:
            if line:
                code = line[0].lower().replace("-", "")
                symbol_code = line[1].lower().replace("-", "")
                codes[symbol_code] = code

    clear_files_in_directory(os.path.join("APP6-icons"), [".png", ".svg"])

    # Iterate over the SVG files
    for file_name in os.listdir(svg_directory):
        file_path = os.path.join(svg_directory, file_name)
        if not (os.path.isfile(file_path) and file_name.lower().endswith(".svg")):
            continue

        if os.path.isfile(file_path) and file_name.lower().endswith(".svg"):
            symbol_code = os.path.splitext(file_name)[0]
            symbol_code = symbol_code[0] + "*" + symbol_code[2] + "*" + symbol_code[4:]
            symbol_code = symbol_code.replace("-", "")

        while True:
            if symbol_code in codes:
                switch_case = {
                    'f': ['friend', 'assumed-friend'],
                    'h': ['hostile', 'suspect'],
                    'n': ['neutral', 'pending'],
                    'u': ['unknown']
                }
                case = switch_case.get(file_name[1], ['unknown'])

                subfolder = case[0]
                code = codes[symbol_code]
                print(f"File Name: {file_name}, Corresponding Code: {code}")
                copy_and_rename_file(file_path, subfolder, code)

                # create behaviour with dash (cf. other_behaviours)
                if len(case) > 1:
                    create_other_svg_behaviour(file_path_in=file_path, folder_out=case[1], filename_out=code)

                break

            if "p" in symbol_code:
                symbol_code = replace_last_character_found(symbol_code, "p")
            else:
                copy_and_rename_file(file_path, "rejected", file_name)
                print(f"File Name: {file_name}, No corresponding code found")
                break

    # Iterate over the app6-SVG files
    idx_file = 0
    svg_values = list(distinct_svg.values())
    for file_name in os.listdir(svg_app6b_directory):
        file_path = os.path.join(svg_app6b_directory, file_name)
        if not (os.path.isfile(file_path) and file_name.lower().endswith(".svg")):
            continue

        # focus only on tactical data
        file_number = re.search(r'(\d+)', file_name).group(0)
        # 147 svg de tactical, de 806 à 952 inclus
        if int(file_number) < 806 or int(file_number) >= 953:
            continue

        for code in svg_values[idx_file]:
            print(f"File Name: {file_name}, Corresponding Code: {code}")
            copy_and_rename_file(file_path, "tactical", code)

        idx_file += 1


def create_other_svg_behaviour(file_path_in, folder_out, filename_out):
    with open(file_path_in, 'r') as f:
        svg_content = f.read()

    # Utilise une expression régulière pour rechercher la première balise <path>
    match = re.search(r'<path\s[^>]*?stroke="black"', svg_content)

    if match:
        start, end = match.span()

        # Insère stroke-dasharray="5,5" après stroke="black"
        modified_svg = svg_content[:end] + ' stroke-dasharray="5,5"' + svg_content[end:]

        # # Vérifie si le dossier de sortie existe, sinon le crée
        if not os.path.exists(os.path.join("APP6-icons", "svg", folder_out)):
            os.makedirs(os.path.join("APP6-icons", "svg", folder_out))

        # Obtient le nom de fichier sans le chemin
        file_name = os.path.basename(file_path_in)

        # Crée le chemin complet pour le fichier de sortie
        file_path_out = os.path.join("APP6-icons", "svg", folder_out, filename_out + ".svg")

        # Écrit le contenu modifié dans un nouveau fichier ou dans le même fichier
        with open(file_path_out, 'w') as f:
            f.write(modified_svg)


def replace_last_character_found(string, character):
    last_p_index = string.rfind(character)  # Find the index of the last "character" in the string
    if last_p_index != -1:  # Check if "character" is present in the string
        modified_string = string[:last_p_index] + "*" + string[last_p_index + 1:]
        return modified_string
    else:
        return string


def put_all_png_in_directory(path_in, path_out):
    for root, dirs, files in os.walk(path_in):
        for file in files:
            if file.endswith(".png") and file[0].isdigit():
                original_path = os.path.join(root, file)
                new_directory_name = os.path.basename(root)
                new_file_name = file.replace("x", new_directory_name[0].lower())
                new_path = os.path.join(path_out, new_file_name)

                shutil.copyfile(original_path, new_path)


# Call the main method
main()
