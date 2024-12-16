import os


def list_files_recursively(directory):
    with open("metadata.csv", "w") as a:
        # i = 0
        a.write('file,title,author,subject,keywords,year,pages,edition,volume,publisher,city,serial,abstract,isbn,bbk,udk,doi\n')
        for root, dirs, files in os.walk(directory):
            for file in files:
                # i = i + 1
                a.write(str('"' + os.path.join(root, file)+ '",\n'))
        # print(i)


list_files_recursively(r"D:\library")


# import os

# root = r'D:/library'
# for filename in os.listdir( root ):
#     relative_path = os.path.join( root, filename )
#     absolute_path = os.path.abspath( relative_path )
#     print( absolute_path )


# from pathlib import Path
# i=0
# for p in Path(r'D:/library').rglob( '*' ):
#     i=i+1
#     print( p )
# print(i)


# from pathlib import Path
# root_directory = Path("library")
# with open("folder.txt", "w") as a:
#     for path_object in root_directory.glob("**"):
#         a.write(str(path_object)+'\n')


# for path in Path('D:/library').rglob('*'):
#     print(path)

# import os

# def list_files_recursive(path='.'):
#     with open("folder.txt", "w") as a:
#         for entry in os.listdir(path):
#             full_path = os.path.join(path, entry)
#             if os.path.isdir(full_path):
#                 list_files_recursive(full_path)
#             else:
#                 # print(full_path)
#                 a.write(str(full_path)+'\n')

# # Specify the directory path you want to start from
# directory_path = (r'folder')
# list_files_recursive(directory_path)


# import pathlib

# # folder path
# dir_path = r'folder'

# # to store file names
# res = []

# # construct path object
# d = pathlib.Path(dir_path)

# # iterate directory
# for entry in d.iterdir():
#     # check if it a file
#     if entry.is_file():
#         res.append(entry)
# print(res)


# import os

# with open("library.txt", "w") as a:
#     for path, subdirs, files in os.walk(r'D:\library'):
#        for filename in files:
#          f = os.path.join(path, filename)
#          a.write(str(f) + os.linesep)

# import os

# a = open("output2.txt", "w")
# for path, subdirs, files in os.walk(r'D:\projects\python\folder'):
#    for filename in files:
#       a.write(filename + os.linesep)
