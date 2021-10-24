# cz4031-db

## Step 1: generating data with TPC-H tool

<img src="pictures/1-data generation.png"/>

## Step2: Converting .tbl file to .csv file

<img src="pictures/2-convert csv file.png"/>

## Step3: Download PostgreSQL and pgAdmin4

## Step4: Create database as 'db4031'

* Open pgAdmin4
* On the side bar, right click on database, and select Create Database<img src="pictures/3-create db menu.png"/>
* Name the database as 'db4031' and click on 'Save'<img src="pictures/4-create db.png"/>

## Step5: Create schema with Query Tool

* first right click on database 4031, and click on Query Tool
  <img src="pictures/5-query tool.png"/>
* In the Query Tool panel, select the predefined create table .sql file
  <img src="pictures/6-open query file.png"/>
* clicked on run button to execute queries
  <img src="pictures/7-run query.png"/>

## Step6: Import the data with .csv file

* right click on respective table for exmaple `customer` table and choose import
  <img src="pictures/8-import csv.png"/>

* change to import, select the data file and clicked on save 
  <img src="pictures/9-select importing file.png"/>

* after everything done, clicked on statistic it should show the live touples count
  <img src="pictures/10-done.png"/>

  

