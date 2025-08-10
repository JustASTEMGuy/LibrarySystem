# Library System with GUI
## Setup / Requirements
### Database Setup
1. Run `db/schema.sql` on your **MySQL** server using **phpMyAdmin** or **MySQL** before starting the application.
2. Ensure the database is **online**.
3. Insert sample data to the ``transactions`` table and ``users`` table, set the role as ``Admin`` or ``Student`` for testing purposes.

### Running the Code
1. Using the **powershell** terminal, enter the following command to compile the `.ps1` file:
```bash
.\buildcompile.ps1
```

2. Ensure the following output is shown:
```bash
Compiling Java Source Files...
Build complete!
```

3. Run the following code to run `Main.java`:
```bash
java -cp "bin;lib/*" main.Main
```

## Folder Structure

The workspace contain multiple folders, **where**:

- `config`: the folder to maintain database connection

- `dao`: the folder to maintain access classes to database
- `gui`: the folder to maintain Graphical User Interface for users
    - `panel`: the subfolder to maintain panels from dashboards
- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies
- `bin`: the folder to store compiled output files
- `main`: the folder to store `Main` class, used to run the program
- `obj`: the folder to incorporate objects for OOP
- `resources`: the folder to store resources such as images
- `test`: the folder to store test files
- `user`: the folder to store user's class

## Dependencies
- [MySQL](https://www.mysql.com/) (Database)

## Credits
- [Woon Wei Jian](https://github.com/JustASTEMGuy)
- [Mah Yau Mun](https://github.com/MahYM0605)