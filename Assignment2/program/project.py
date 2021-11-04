import sys 
import time
import json
import psycopg2 # need install
from PyQt5.QtWidgets import QApplication # need install
from qt_material import apply_stylesheet #, list_themes # need install
from interface import *
from annotation import *

# extract hard coded values
FILE_CONFIG = "config.json"
FILE_APP_THEME = "light_cyan_500.xml" #list_themes()[12]


# allow use of with syntax
class DatabaseCursor(object):
    def __init__(self, config):
        self.config = config

    def __enter__(self):
        self.conn = psycopg2.connect(
            host=self.config["host"],
            dbname=self.config["dbname"],
            user=self.config["user"],
            password=self.config["pwd"]
            # add port?
        )   
        self.cur = self.conn.cursor()
        #self.cur.execute("SET search_path TO " + self.config['schema'])
        return self.cur

    def __exit__(self, exc_type, exc_val, exc_tb):
        # some logic to commit/rollback
        self.conn.close()

class Program():
    def __init__(self):
        with open(FILE_CONFIG, "r") as file:
            self.config = json.load(file)
        # init ui components
        self.app = QApplication(sys.argv)
        apply_stylesheet(self.app, theme=FILE_APP_THEME)
        self.window = UI()
        self.window.setOnDatabaseChanged( lambda: self.onDatabaseChanged())
        self.window.setOnAnalyseClicked( lambda: self.analyseQuery() )

    def run(self):
        self.window.show()
        list_db = list(self.config.keys() )
        print(f"List of database configs from json file: {list_db}")
        self.window.setListDatabase(list_db)
        sys.exit(self.app.exec_())
        
    def onDatabaseChanged(self):
        # check cur database, update schema?
        cur_db = self.window.list_database.currentText()
        print(f"Current selected database is {cur_db}")
        self.db_config = self.config[cur_db]
        self.updateSchema()

    def hasDbConfig(self):
        if not hasattr(self, "db_config"): 
            return False
        if self.db_config == None:
            return False
        return True

    def analyseQuery(self):
        if not self.hasDbConfig():
            self.window.showError("Database configuration is not found")
            return
        try:
            query = self.window.readInput()
            if not query:
                print("query is empty")
                return
            print("query: \n%s"%query)
            with DatabaseCursor(self.db_config) as cursor:
                cursor.execute("EXPLAIN (FORMAT JSON) " + query)
                plan = cursor.fetchall()
                print("qep: \n%s"%plan)
                plan_annotated = Annotator().wrapper(plan)
                print("annotated qep: \n%s"%plan_annotated)
                self.window.setResult( plan_annotated )
                
        except Exception as e:
            print(str(e))
            self.window.showError("Unable to analyse query!", e)

    def updateSchema(self):
        if not self.hasDbConfig(): 
            self.window.setSchema(None)
            self.window.showError("Database configuration is not found")
            return
        try:
            with DatabaseCursor(self.db_config) as cursor:
                query = "SELECT table_name, column_name, data_type, character_maximum_length as length FROM information_schema.columns WHERE table_schema='public' ORDER BY table_name, ordinal_position"
                cursor.execute(query)
                response = cursor.fetchall()

                # parse response as dictionary 
                schema = {}
                for item in response:
                    # cols are table_name, column_name, data_type, length (nullable)
                    attrs = schema.get(item[0], [])
                    attrs.append(item[1])
                    schema[item[0]] = attrs
                self.window.setSchema(schema)
        except Exception as e:
            print(str(e))
            self.window.showError("Unable to retrieve schema information!", e)
    
    
if __name__ == "__main__":
    Program().run()

