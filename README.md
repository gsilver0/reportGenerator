# reportGenerator
A simple cell phone usage report generator

There are discrepancies in the data model between the readme and the csv files. I will assume that the project will be to read the CSVs.

While not present in the CSV files, the instructions suggest that employees may replace their cell phones. The test cases should verify that the reports properly update before and after a changeover occurs. Also, I will assume that the different phones will be represented on separate lines, as a changeover could occur any time during a month.
Because there is no data to tie a particular call to a particular phone, I will assume that employees will have a single phone active at any time, and that phones are activated immediately upon purchase.

The UI will need to take an input of what year to process. For simplicities sake, I will assume that the most recent 5 calendar years are applicable. Alternatively, the dropdown could present a list of years based on the data.
