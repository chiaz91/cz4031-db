# Assignment 2

## objective
* annotate "SQL query" using its "QEP view" (operations/algorithms used during querying process)

## dataset and tools

* [TPC-H ][1]
* [PostgreSQL][2]
* [pgAdmin][3]

## submission files

* `interface.py`: code for the GUI
* `annotation.py`: code for generating the annotations
* `preprocessing.py`: code for reading inputs and any preprocessing necessary to make your algorithm work
* `project.py`: main file that invokes all the necessary procedures from these three files (Must executable with python IDE or command prompt)

## Setting up project with Anaconda

* create a `config.json` file in project root directory like below

  ```json
  {
  	"db": {
  		"host": "localhost",
  		"dbname": "db4031",
  		"user" : "",
  		"pwd" : ""
  	}
  }
  ```


* Create new environment

  ```shell
  conda create --name cz4031a2 python=3.9.6
  ```

* Activate environment

  ```shell
  conda activate cz4031a2
  ```

* Install required frameworks and libraries

  ```shell
  pip install -r requirements.txt
  ```

  



[1]:http://www.tpc.org/tpc_documents_current_versions/current_specifications5.asp
[2]:https://www.postgresql.org/
[3]:https://www.pgadmin.org/
