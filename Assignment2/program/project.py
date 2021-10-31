import sys 
import time
from PyQt5.QtWidgets import QApplication # need install
from qt_material import apply_stylesheet # need install
from interface import UI

import json
import psycopg2 # need install


def analyseQuery(ui, conn):
    query = ui.readInput()
    if not query:
        print("query is empty")
        return
    print("query: %s"%query)
    # simuate query
    if conn:
        cursor = conn.cursor()
        cursor.execute("EXPLAIN (FORMAT JSON) " + query)
        plan = cursor.fetchall()
        # maybe do anntation here
        plan_str = str(plan)
        ui.setResult( plan_str )
    
if __name__ == "__main__":
    # load config file
    with open("config.json", "r") as file:
        config = json.load(file)

    # connect to database
    db_config = config["db"]
    conn = psycopg2.connect(
        host=db_config["host"],
        dbname=db_config["dbname"],
        user=db_config["user"],
        password=db_config["pwd"]
    )
    if conn:
        print("Connected to database %s sucessfully!" % db_config["dbname"])
    else:
        print("Connection failed.")
        
    # show app ui
    app = QApplication(sys.argv)
    apply_stylesheet(app, theme="light_cyan_500.xml")
    window = UI()
    # assigning callback
    window.setOnClicked( lambda: analyseQuery(window, conn) )
    
    window.show()
    sys.exit(app.exec_())
