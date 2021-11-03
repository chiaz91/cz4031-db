# cz4031-db

## Step 1: generating data with TPC-H tool

- download [TPC-H ](http://www.tpc.org/tpc_documents_current_versions/current_specifications5.asp) data
- run `dbgen` to generate .tbl data
  <img src="pictures/1-data generation.png"/>

## Step2: Converting .tbl file to .csv file

- a simple program is written for file conversion<img src="pictures/2-convert csv file.png"/>

## Step3: Download PostgreSQL and pgAdmin4

- download and install [PostgreSQL](https://www.postgresql.org/), it should have [pgAdmin4](https://www.pgadmin.org/) included

## Step4: Create first database - 'db4031'

- Open pgAdmin4
- On the side bar, right click on database, and select Create Database<img src="pictures/3-create db menu.png"/>
- Name the database as `db4031` and click on 'Save'<img src="pictures/4-create db.png"/>

## Step5: Create schema with Query Tool

- first right click on database 4031, and click on Query Tool
  <img src="pictures/5-query tool.png"/>
- In the Query Tool panel, open the predefined schema creation query file (`db_init.sql`)
  <img src="pictures/6-open query file.png"/>
- clicked on run button to execute queries
  <img src="pictures/7-run query.png"/>

## Step6: Import the data with .csv file

- right click on respective table for example `customer` table and choose import
  <img src="pictures/8-import csv.png"/>
- change to import, select the data file and clicked on save
  <img src="pictures/9-select importing file.png"/>
- after everything done, clicked on statistic it should show the live touples count
  <img src="pictures/10-done.png"/>

## Step7: Create second database - db4031-imdb

- IMDb dataset can be downloaded from: https://datasets.imdbws.com/
- title.ratings.tsv.gz and title.basics.tsv.gz will be used in this section.
- Following steps 5-6, create a second database with the name db4031-imdb
  <img src="pictures/IMDb1-create imdb db"/>
- In the Query Tool panel, open the predefined schema creation query file (`db_imdb_init.sql`)
  <img src="pictures/IMDb2-create table using query tool.png"/>
- clicked on run button to execute queries
- Import the data using .tsv file for both the title and ratings tables by right clicking on respective table then select "import".
- Import options: format: "text", delimiter: "[tab]".
  <img src="pictures/IMDb3-import tsv file (title).png"/>
  <img src="pictures/IMDb4-import tsv file (ratings).png"/>
