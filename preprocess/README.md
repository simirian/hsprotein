This python script is used to fetch protein models from PDB and preprocess them.
Preprocessing will remove alternate locations of atoms by selecting the first
location of each, remove all atoms which are not part of a polypeptide chain,
and then populate the chain with hydrogen if there is none in the model.

# Usage

The command takes two arguments. The first is the name of a file which contains
a list of PDB IDs to fetch from PDB. Internet is required to fetch the files.
The second argument is the output directory in which the PDB files and all
processed files will be placed. An optional `-o` flag can be used to force files
to be downloaded and processed again.

    python fetch.py -o idlist.txt ouputs/
