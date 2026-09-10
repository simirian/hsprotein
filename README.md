To run the Maven code:
`mvn clean package` cleans out any old target folders / build artifacts and then re-packages them
``mvn compile exec:java "-Dexec.args=`"pathToInputFileDirectory inputFileName residueID pathToOutputFileDirectory dipeptideOnlyTag`""`` to run the project in PowerShell (with escaped strings and tags)
- `pathToInputFileDirectory` ex: `../../../Downloads`
- `inputFileName` ex: `DummyCarbons.pdb` or `fourCarbon.cif`
- `ResidueID` ex: `123`
- `pathToOutputFileDirectory` ex: `protein_output`
- `dipeptideOnlyTag` ex: `1` (True) or `0` (False)