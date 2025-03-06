import csv
import os

from milsymbol.enum.affiliation import Affiliation
from milsymbol.enum.battle_dimension import BattleDimension
from milsymbol.svg_shape.hostile_shapes import hostile_color

APP6_ICONS_PATH = os.path.join("..", "APP6-icons")


def extract_battle_dimension(hierarchy):
    if len(hierarchy) >= 3:
        third_char = hierarchy[2]
        if third_char == 'G':
            if len(hierarchy) >= 5 and hierarchy[4] != "-":
                fifth_char = hierarchy[4]
                dimension_value = third_char + fifth_char
            else:
                dimension_value = third_char
        else:
            dimension_value = third_char.upper()

        print(dimension_value)
        for dimension in BattleDimension:
            if dimension.name == dimension_value:
                return dimension
    return None


def get_SIDC_from_hierarchy(hierarchy):
    try:
        with open(os.path.join(APP6_ICONS_PATH, "icon-files-mapping.csv"), 'r') as file:
            csv_reader = csv.DictReader(file, delimiter=';')
            for row in csv_reader:
                if row['hierarchy'] == hierarchy:
                    return row['SIDC']

        print(f"No SIDC found for the hierarchy '{hierarchy}' in the 'icon-files-mapping.csv' file.")
    except FileNotFoundError:
        print("The 'icon-files-mapping.csv' file was not found.")
    except Exception as e:
        print(f"An error occurred: {str(e)}")


def manage_file(directory, file_name, affiliations):
    filename = os.path.basename(file_name)
    hierarchy = os.path.splitext(filename)[0]
    file_path = os.path.join(directory, file_name)

    # À partir de la hierarchie, on retrouve le SIDC.
    SIDC = get_SIDC_from_hierarchy(hierarchy)
    # Le 3e digit indique la battle dimensions (space, air, ground...)
    battle_dimension = extract_battle_dimension(SIDC)

    # Check if the path corresponds to a file
    if os.path.isfile(file_path):
        replace_string_in_file(file_path, Affiliation.F.get_shape(battle_dimension),
                               affiliations.get_shape(battle_dimension))


def replace_string_in_file(file_path, old_string, new_string):
    try:
        # Open the file in read mode
        with open(file_path, 'r') as file:
            file_content = file.read()

        # Replace the OLD string with the NEW string in the file content
        new_content = file_content.replace(old_string, new_string)
        # Replace the string, "#80E0FF" is the initial value (for friend)
        new_content = new_content.replace('fill="#80E0FF"', f'fill="{hostile_color}"')

        # Open the file in write mode to update it
        with open(file_path, 'w') as file:
            file.write(new_content)

        print(f"The string '{old_string}' has been replaced with '{new_string}' in the file '{file_path}'.")
    except FileNotFoundError:
        print(f"The file '{file_path}' was not found.")
    except Exception as e:
        print(f"An error occurred: {str(e)}")
