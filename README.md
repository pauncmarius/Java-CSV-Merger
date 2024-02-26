# Repository Name: "Java-CSV-Merge"

## Summary:
Create a Java command-line tool to merge data from two CSV files. The tool should identify common information between the files and generate three new CSV files based on this information. The first file will contain data present in both input files, the second file will contain data exclusive to the first input file, and the third file will contain data exclusive to the second input file.

## Input:
The program should accept two paths to input CSV files as command-line arguments.

## Output:
The program will generate three CSV files in the current directory:

"common.csv": Contains rows found in both input CSV files.<br>
"diff1.csv": Contains rows found only in the first input CSV file.<br>
"diff2.csv": Contains rows found only in the second input CSV file.

## Specification:
The first row in each CSV file is treated as the header row.
Only values associated with common columns in both files are considered when comparing rows.
Two rows are considered equal if they have the same values on all common columns.
The "common.csv" file merges equal rows, potentially resulting in additional columns compared to the input files.
