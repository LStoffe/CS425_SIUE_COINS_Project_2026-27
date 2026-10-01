# Duplication Report for `big/` Project

This document summarizes the confirmed duplicate text/code files found in the `big/` directory. It focuses on files that share both identical names and identical content based on direct examination.


## 1. Duplicated JSP Files: `handbooks/safetypolicies/pgN.jsp`
For each of these, the file in the build output matches the one in the source exactly:

- `big/src/web/handbooks/safetypolicies/pgN.jsp`
- `big/build/dist/handbooks/safetypolicies/pgN.jsp`

Where `N` is a page number, e.g., 1, 2, 3, ..., 18 (all observed to match).


## 2. Duplicated JavaScript Library Files
- `big/src/web/js/library.js`
- `big/build/dist/js/library.js`

These files are confirmed to be identical in content.


## 3. Other Likely-Identical/Patterned Duplicates

### `employeehandbook/pgN.jsp`
- `big/src/web/handbooks/employeehandbook/pgN.jsp`
- `big/build/dist/handbooks/employeehandbook/pgN.jsp`

Where `N` is a handbook page number (pattern and folder structure indicate matching content). Full verification recommended if absolute accuracy needed.

### Configuration/Resource Files
- Many `.properties` and `.xml` files in `conf/` are copied to `build/dist/WEB-INF/classes/`.
- Example:
  - `big/conf/BIGLog.properties`, `big/build/dist/WEB-INF/classes/BIGLog.properties`
  - `big/conf/iBATIS/companyPersonnel-sqlMap.xml`, `big/build/dist/WEB-INF/classes/companyPersonnel-sqlMap.xml`


## Table: Example Duplicates (Sample)

| File Name              | Source Location                                       | Deployment Location                                    | Confirmed Duplicate? |
|------------------------|------------------------------------------------------|--------------------------------------------------------|----------------------|
| pg14.jsp               | src/web/handbooks/safetypolicies/pg14.jsp            | build/dist/handbooks/safetypolicies/pg14.jsp           | Yes                  |
| pg5.jsp                | src/web/handbooks/safetypolicies/pg5.jsp             | build/dist/handbooks/safetypolicies/pg5.jsp            | Yes                  |
| pg2.jsp                | src/web/handbooks/safetypolicies/pg2.jsp             | build/dist/handbooks/safetypolicies/pg2.jsp            | Yes                  |
| pg4.jsp                | src/web/handbooks/safetypolicies/pg4.jsp             | build/dist/handbooks/safetypolicies/pg4.jsp            | Yes                  |
| library.js             | src/web/js/library.js                                | build/dist/js/library.js                               | Yes                  |
| ...                    | ...                                                  | ...                                                    | ...                  |
| BIGLog.properties      | conf/BIGLog.properties                               | build/dist/WEB-INF/classes/BIGLog.properties           | Likely               |


## Notes on Why These Duplicates Exist
- **Development:** Source files (`src/`, `conf/`) are maintained by developers.
- **Deployment:** Files are copied to `build/dist/` by the build process for use by the server/application at runtime.
- **Best Practice:** Do **not** delete one set; both are needed for development and deployment.



## If You Need More Detail
- This is a summary of the duplicate files by name and content.
- If you wish to verify or enumerate all config/resource file duplicates, or need a file-by-file CSV or additional hashes, please request further analysis or automation.

## Can You Remove One Copy of These Files and Still Run the Application?

In almost all well-structured Java web applications, the answer is:

> ❌ **No, you cannot freely delete one copy everywhere.**

You can sometimes remove **source/versioned copies**, but not always — and only if you fully understand the project’s **build and deployment process**.


## 🧩 Why?

### Purpose of the Duplicates

- Files in `src/web/handbooks/safetypolicies/pgN.jsp` are typically the **source** or **developer-editable** files.  
- Files in `build/dist/handbooks/safetypolicies/pgN.jsp` are **deployed output** — the build process (e.g., **Ant** via `build.xml`) copies source files to the deployment target directory for the application server (like **Tomcat**).  
- The same applies for **JS**, **XML**, and **properties** files:
  - Sources live in `src/` or `conf/`
  - Deploy/output copies are in `build/dist/...` or similar


## ⚙️ The Application Server / Runtime Only Sees the Deploy Folder

- The **running server** reads from the **deployment output** (`build/dist/...`), not from the developer sources.  
- If you delete from **output** (e.g., `build/dist`), the app will **break** or be **missing functionality**.  
- If you delete from **source** (e.g., `src/web`), you won’t be able to **rebuild**, **maintain**, or **modify** your app properly.


## ✅ Best Practices

- **Do not** manually delete these duplicate files.  
- Let your **build tool** (Ant, Maven, etc.) handle copying and updating.  

If you want to reduce disk usage or confusion:

- Add build/output directories (like `build/dist/`) to your `.gitignore`.  
- Only track “**source of truth**” files located in `src/`, `conf/`, etc.


### Summary

| Location | Purpose | Safe to Delete? |
|-----------|----------|----------------|
| `src/` | Source files (developer editable) | ❌ No |
| `conf/` | Configuration files | ❌ No |
| `build/dist/` | Deployment output (runtime) | ❌ No, app won’t run |
| `.gitignore` | Exclude build outputs from version control | ✅ Yes, for cleanup |


> 💡 **Tip:** To safely manage duplicates, always inspect the `build.xml` (Ant) or `pom.xml` (Maven) to understand the copy/deploy rules before making any deletions.

