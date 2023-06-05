import csv
import os
import shutil


def read_svg_files():
    global symbol_code
    svg_directory = os.path.join("rawdata", "symbols")
    mapping_file = os.path.join("rawdata", "APP6_code_mapping.csv")
    codes = {}

    # Read the mapping file
    with open(mapping_file, "r") as f:
        csv_reader = csv.reader(f, delimiter=";")
        next(csv_reader)  # Ignore the CSV header
        for line in csv_reader:
            if line:
                symbol_code = line[1].lower().replace("-", "")
                code = line[0].lower().replace("-", "")
                codes[symbol_code] = code

    # Iterate over the SVG files
    for file_name in os.listdir(svg_directory):
        file_path = os.path.join(svg_directory, file_name)
        if not (os.path.isfile(file_path) and file_name.lower().endswith(".svg")):
            continue

        if os.path.isfile(file_path) and file_name.lower().endswith(".svg"):
            symbol_code = os.path.splitext(file_name)[0]
            symbol_code = symbol_code[0] + "*" + symbol_code[2:]
            symbol_code = symbol_code.replace("-", "")

        while True:
            if symbol_code in codes:
                switch_case = {
                    'f': 'friend',
                    'h': 'hostile',
                    'n': 'neutral',
                    'u': 'unknown'
                }
                subfolder = switch_case.get(file_name[1], 'unknown')
                code = codes[symbol_code]
                print(f"File Name: {file_name}, Corresponding Code: {code}")
                copy_and_rename_file(file_path, subfolder, code)
                break

            if "p" in symbol_code:
                symbol_code = replace_last_character_found(symbol_code, "p")
            else:
                copy_and_rename_file(file_path, "rejected", file_name)
                print(f"File Name: {file_name}, No corresponding code found")
                break


def replace_last_character_found(string, character):
    last_p_index = string.rfind(character)  # Find the index of the last "p" in the string
    if last_p_index != -1:  # Check if "p" is present in the string
        modified_string = string[:last_p_index] + "*" + string[last_p_index + 1:]
        return modified_string
    else:
        return string


def copy_and_rename_file(file_in, subfolder, file_out):
    file_directory = os.path.dirname(file_in)

    # Check if the subfolder exists, create it if it doesn't
    if not os.path.exists(os.path.join("APP6-icons", "svg", subfolder)):
        os.makedirs(os.path.join("APP6-icons", "svg", subfolder))

    # Build the full path of the input file
    file_in_path = os.path.abspath(file_in)

    # Get the file extension
    file_extension = os.path.splitext(file_in)[1]
    if os.path.splitext(file_out)[1] == file_extension:
        file_extension = ""

    # Build the full path of the output file in the subfolder
    file_out_path = os.path.join("APP6-icons", "svg", subfolder, file_out + file_extension)

    # Copy the input file to the subfolder with the specified output filename
    shutil.copy2(file_in_path, file_out_path)


def create_png_equivalents(src_directory, dest_directory):
    # Check if the source directory exists
    if not os.path.isdir(src_directory):
        print(f"The source directory '{src_directory}' does not exist.")
        return

    # Create the destination directory if it doesn't exist
    if not os.path.exists(dest_directory):
        os.makedirs(dest_directory)

    # Recursively traverse the source directory
    for root, dirs, files in os.walk(src_directory):
        # Create the corresponding subdirectories in the destination directory
        rel_path = os.path.relpath(root, src_directory)
        dest_subdirectory = os.path.join(dest_directory, rel_path)
        if not os.path.exists(dest_subdirectory):
            os.makedirs(dest_subdirectory)

        # Process each file in the current directory
        for curr_file in files:
            src_file = os.path.join(root, curr_file)
            dest_file = os.path.join(dest_subdirectory, os.path.splitext(curr_file)[0] + ".png")

            # Convert SVG to PNG
            from cairosvg import svg2png
            svg2png(open(src_file, 'rb').read(), write_to=open(dest_file, 'wb'))


# Call the method
read_svg_files()
# Creating png images from svg files
create_png_equivalents("APP6-icons/svg", "APP6-icons/png")
