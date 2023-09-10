import os


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
            svg2png(open(src_file, 'rb').read(), write_to=open(dest_file, 'wb'), output_width=128, output_height=128)
