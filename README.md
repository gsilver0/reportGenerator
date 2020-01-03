# reportGenerator
A simple cell phone usage report generator

There are discrepancies in the data model between the readme and the csv files. I will assume that the project will be to read the CSVs.
There is a misspelling in one of the CSV files. The originally supplied CellPhoneUsageByMonth csv will be rejected. After correcting the misspelling, it will be accepted.
The CSV file checked into this repository will have this corrected, but it can be tested with the original file.

While not present in the CSV files, the instructions suggest that employees may replace their cell phones. The test cases should verify that the reports properly update before and after a changeover occurs. Also, I will assume that the different phones will be represented on separate lines, as a changeover could occur any time during a month.
Because there is no data to tie a particular call to a particular phone, I will assume that employees will have a single phone active at any time, and that phones are activated immediately upon purchase.

The UI will need to take an input of what year to process. For simplicities sake, I will assume that the most recent 5 calendar years are applicable. Alternatively, the dropdown could present a list of years based on the data.

Report:

The year the report is ran for is not requested in the spec document, but lacking this information would make reading the report difficult. This will be added to the header section.

The number of users is also not requested in the spec document, but this information is relevant for the average data and minutes columns, and is also included. 

The printed report will use the same format (mm/dd/yyyy) as the CellPhoneUsageByMonth csv

The Number of Phones header column will return the number of phones active in the specified year. Phones that were deactivated prior to the report year, or activated afterward are not included.

The Average Minutes and Average Data columns will be the average usage for the year per employee, regardless of the number of phones used.
No units are specified for data usage. The report will leave this ambiguous.

Required test cases:
Base case (supplied test case)
Phone replaced in prior year (phone replaced in the previous year should not be counted)
Phone replaced in current year (both phones should be counted)
Phone replaced in later year (only phones active in the specified calendar year should be counted)

