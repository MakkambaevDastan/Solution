import os

def list_files_recursively(directory):
    with open("metadata.csv", "w") as a:
        a.write('file,title,author,subject,keywords,year,pages,edition,volume,publisher,city,serial,abstract,isbn,bbk,udk,doi\n')
        for root, dirs, files in os.walk(directory):
            for file in files:
                a.write(str('"' + os.path.join(root, file)+ '",\n'))

list_files_recursively(r"D:\library")
