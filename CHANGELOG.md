# Changelog

All notable changes to this project will be documented in this file.

## [1.6] - 2025-07-29

> Notable Changes are applied mostly to Book Panel from Admin's Dashboard.

### Added

- Added sort button (ASC and DESC), able to sort book data by column name.
- Added Edit and Deleted Functions (revamped).

### Changed

- Edit & Delete Function no longer use buttons, instead it checks for user's **double click** on a data row.
- Add Button modified from "Add" to "Add Books"


## [1.5] - 2025-07-22

### Added

- Login button is added in **Register page**.
- Added buttons to switch between _Admin_ and _Member_ in the Member's panel (Admin Dashboard).
- Book Panel revamped - User is able to add and delete books. [Edit function is not present yet]
- `CHANGELOG.md` is added to track and elaborate the progress of the project.

### Changed

- Register function is fixed (due to a bug where database is not registered).
- Added and modified `README.md` for catching up. (due to README.md is not prioritised from the start).

## [1.4] - 2025-07-13

### Added

- Register & Sign Out function is implemented.
- Database successfully connected through _XAMPP_.

## [1.3.1] - 2025-07-10 

### Added

- `schema.sql` for user-testing purposes.

### Changed

- `README.md` is modified, instructions to import `schema.sql` is added.

## [1.3] - 2025-07-10

### Added

- Admin dashboard and student dashboard is created.
- Admin panels on book, members and transactions are implemented in the dashboard.
- `adminicon.png` and `studenticon.png` are added as icons for the GUI window.

### Changed
- UI modified and designed to accompany different colours for better look.
- Refined `LoginMain` and `RegisterMain` code by arranging sentences in the code.
- `Person` class is modified to `User` class and inherited by `Admin` and `Student`.