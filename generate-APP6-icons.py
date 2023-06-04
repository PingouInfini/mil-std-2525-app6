import csv
import os
import re
import shutil
import uuid

filename = "rawdata/Milsymbol APP6-B.html"
distinct_previous_svg = {}


def copy_files_init_app6_icon_folder():
    clear_files_in_directory('APP6-icons/svg')
    clear_files_in_directory('APP6-icons/png')
    copy_files('rawdata/svg', 'APP6-icons/svg/friend')


# Delete all files in the target directory
def clear_files_in_directory(target_dir):
    # Check if the directory exists
    if not os.path.isdir(target_dir):
        print(f"The directory '{target_dir}' does not exist.")
        return

    # Traverse the directory recursively
    for root, dirs, files in os.walk(target_dir):
        for curr_file in files:
            if curr_file.lower().endswith(".svg") or curr_file.lower().endswith(".png"):
                file_path = os.path.join(root, curr_file)
                os.remove(file_path)


# Copy all files from the source directory to the target directory
def copy_files(source_dir, target_dir):
    if not os.path.exists(target_dir):
        os.makedirs(target_dir)

    file_list = os.listdir(source_dir)
    for file_name in file_list:
        source_file = os.path.join(source_dir, file_name)
        target_file = os.path.join(target_dir, file_name)
        shutil.copyfile(source_file, target_file)


def init_csv_mapping():
    # Add the CSV file header
    header = ['hierarchy', 'SIDC', 'icon-png', 'icon-svg']
    add_csv_row(header, False)


def extract_svg(string):
    regex = r'(<svg[\w\d=":/.><,\-# ()	]+)'
    found = re.search(regex, string)

    if found:
        svg_value = found.group(1)
        return svg_value
    else:
        return None


def rename_file(directory, number, new_filename):
    prefix = 'svgexport-'
    if 'svg' in directory:
        extension = '.svg'
    else:
        raise Exception("Invalid directory: Could not determine the extension.")

    # Create the filename to be searched
    old_filename = prefix + str(number) + extension

    # Check if the file exists in the directory
    file_path = os.path.join(directory, old_filename)
    if os.path.isfile(file_path):
        # Rename file
        new_file_path = os.path.join(directory, new_filename + extension)
        os.rename(file_path, new_file_path)
    else:
        # An error occured...
        print(f'The {old_filename} file does not exist in the {directory} directory.')


def add_csv_row(row, append=True):
    mode = 'a' if append else 'w'

    file_name = 'APP6-icons/icon-files-mapping.csv'
    with open(file_name, mode, newline='') as csv_file:
        writer = csv.writer(csv_file, delimiter=';')
        writer.writerow(row)


def replace_fill_color(directory, color):
    # Check if the directory exists
    if not os.path.isdir(directory):
        print(f"The directory '{directory}' does not exist.")
        return

    # Iterate over the files in the directory
    for file_name in os.listdir(directory):
        file_path = os.path.join(directory, file_name)

        # Check if the path corresponds to a file
        if os.path.isfile(file_path):
            # Read the file content
            with open(file_path, 'r') as curr_file:
                content = curr_file.read()

            # Replace the string, "#80E0FF" is the initial value (for friend)
            content = content.replace('fill="#80E0FF"', f'fill="{color}"')

            # Write the modified content back to the file
            with open(file_path, 'w') as curr_file:
                curr_file.write(content)


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


with open(filename, "r") as file:
    idx_file = 0
    file_begin = True

    # init files from "rawdata" folder to "APP6-icons" folder
    copy_files_init_app6_icon_folder()
    init_csv_mapping()

    for line in file:
        # count previous svg before the of hierarchy, stored in <count_svg>
        if "<svg" in line and file_begin:
            distinct_previous_svg[extract_svg(line)] = uuid.uuid4()

        # Ignore lines up to ">SPACE</h2>"
        if ">SPACE</h2>" in line:
            file_begin = False
            idx_file = len(distinct_previous_svg)

        if not file_begin:

            # pattern = r'(\d.X.[\d\.]+)[ ]?(?:(?:<[\/]?[brem]+>)+([\w:(),*\/\- &amp;]+)?)+(<\/td><td><svg)?'
            pattern = r'(\d.X.[\d\.]+)[ ]?(?:(?:<[\/]?[brem]+>)+([\w:(),*\/\- \'"&amp;]+)?)+(<\/td><td>)(<svg[' \
                      r'\w\d=":/.><,\-# ()	+$&amp;]+<\/svg>)?'
            matches = re.findall(pattern, line)

            if matches:
                for match in matches:
                    hierarchy = match[0].replace("'", "")
                    sidc = match[1].strip().replace("'", "") if match[1] else None
                    has_svg = bool(match[3])

                    if has_svg:
                        # a hierarchy has the same symbol
                        if match[3] in distinct_previous_svg:
                            file_hierarchy = distinct_previous_svg[match[3]]
                            add_csv_row([hierarchy, sidc, file_hierarchy + '.png', file_hierarchy + '.svg'])
                            continue
                        else:
                            distinct_previous_svg[match[3]] = hierarchy

                        rename_file('APP6-icons/svg/friend', idx_file, hierarchy)
                        add_csv_row([hierarchy, sidc, hierarchy + '.png', hierarchy + '.svg'])

                        idx_file += 1

    # Finally, we adapt the background color to the affiliation
    copy_files('APP6-icons/svg/friend', 'APP6-icons/svg/hostile')
    replace_fill_color('APP6-icons/svg/hostile', '#FF8080')
    copy_files('APP6-icons/svg/friend', 'APP6-icons/svg/neutral')
    replace_fill_color('APP6-icons/svg/neutral', '#AAFFAA')
    copy_files('APP6-icons/svg/friend', 'APP6-icons/svg/unknown')
    replace_fill_color('APP6-icons/svg/unknown', '#FFFF80')

    # Creating png images from svg files
    create_png_equivalents("APP6-icons/svg", "APP6-icons/png")
