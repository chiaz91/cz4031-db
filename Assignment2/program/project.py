import sys 
import time
import json
import psycopg2 # need install
from PyQt5.QtWidgets import QApplication # need install
from qt_material import apply_stylesheet # need install
from interface import UI


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
        with open("config.json", "r") as file:
            self.config = json.load(file)
        # init ui components
        self.app = QApplication(sys.argv)
        apply_stylesheet(self.app, theme="light_cyan_500.xml")
        self.window = UI()
        self.window.setOnDatabaseChanged( lambda: self.onDatabaseChanged())
        self.window.setOnAnalyseClicked( lambda: self.analyseQuery() )
        self.window.setListDatabase(["TPC-H", "Test"])

    def run(self):
        self.window.show()
        sys.exit(self.app.exec_())
        
    def onDatabaseChanged(self):
        # check cur database, update schema?
        cur_db = self.window.list_database.currentText()
        print(f"curr db is {cur_db}")
        if (cur_db == "TPC-H"):
            self.db_config = self.config["db"]
        else:
            self.db_config = None
        self.updateSchema()

    def hasDbConfig(self):
        if not hasattr(self, "db_config"): 
            return False
        if self.db_config == None:
            return False
        return True

    def analyseQuery(self):
        if not self.hasDbConfig():
            # show error?
            return
        try:
            query = self.window.readInput()
            if not query:
                print("query is empty")
                return
            print("query: %s"%query)
            with DatabaseCursor(self.db_config) as cursor:
                cursor.execute("EXPLAIN (FORMAT JSON) " + query)
                plan = cursor.fetchall()
                # maybe do anntation here
                plan_str = str(plan)
                self.window.setResult( plan_str )
        except Exception as e:
            print(str(e)) 

    def updateSchema(self):
        if not self.hasDbConfig(): 
            self.window.setSchema(None)
            return
        
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
            
    
    
if __name__ == "__main__":
    Program().run()

