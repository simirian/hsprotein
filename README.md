# Running the Code

To run the Maven code: `mvn clean package` cleans out any old target folders /
build artifacts and then re-packages them

```
mvn compile exec:java '-Dexec.args="FILE OUTDIR"'
```

- `FILE` the structure to read, can be PDB or mmCIF
- `OUTDIR` a directory to place outputs in

# Javadocs

After building, navigate to the `target/apidocs` folder and open `index.html` in
a browser.

# CSV Files

All CSV files use the following columns:

```
chain,resnum,time,energy,angles
```
