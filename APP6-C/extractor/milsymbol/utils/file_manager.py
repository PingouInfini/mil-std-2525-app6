import os
import shutil

APP6_ICONS_PATH = os.path.join("..", "APP6-icons")


# Delete all files in the target directory
def clear_files_in_directory(target_dir, extensions):
    # Check if the directory exists
    if not os.path.isdir(target_dir):
        print(f"The directory '{target_dir}' does not exist.")
        return

    # Traverse the directory recursively
    for root, dirs, files in os.walk(target_dir):
        for curr_file in files:
            if os.path.splitext(curr_file)[1] in extensions:
                file_path = os.path.join(root, curr_file)
                os.remove(file_path)

    # Remove empty directories
    for root, dirs, files in os.walk(target_dir, topdown=False):
        for dir_name in dirs:
            dir_path = os.path.join(root, dir_name)
            if not os.listdir(dir_path):
                os.rmdir(dir_path)


# Copy all files from the source directory to the target directory
def copy_files(source_dir, target_dir):
    if not os.path.exists(target_dir):
        os.makedirs(target_dir)

    file_list = os.listdir(source_dir)
    for file_name in file_list:
        source_file = os.path.join(source_dir, file_name)
        target_file = os.path.join(target_dir, file_name)
        shutil.copyfile(source_file, target_file)


def copy_and_rename_file(file_in, subfolder, file_out):
    # Check if the subfolder exists, create it if it doesn't
    if not os.path.exists(os.path.join(APP6_ICONS_PATH, "svg", subfolder)):
        os.makedirs(os.path.join(APP6_ICONS_PATH, "svg", subfolder))

    # Build the full path of the input file
    file_in_path = os.path.abspath(file_in)

    # Get the file extension
    file_extension = os.path.splitext(file_in)[1]
    if os.path.splitext(file_out)[1] == file_extension:
        file_extension = ""

    # Build the full path of the output file in the subfolder
    file_out_path = os.path.join(APP6_ICONS_PATH, "svg", subfolder, file_out + file_extension)

    # Copy the input file to the subfolder with the specified output filename
    shutil.copy2(file_in_path, file_out_path)
