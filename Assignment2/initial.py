import psycopg2 # need install
import json
import configparser
from flask import Flask, render_template
#from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__, template_folder="layout")

'''
app.config['SQLALCHEMY_DATABASE_URI']="postgresql://scott:tiger@localhost/db4031"
db = SQLAlchemy(app)

class customer(db.Model):
  __tablename__ = 'customer'
  id = db.Column('c_custkey', db.Integer, primary_key=True)
  name = db.Column('c_name', db.Unicode)
'''


# loading from config file
with open("config.json", "r") as file:
    config = json.load(file)

db_config = config["db"]
conn = psycopg2.connect(
            host=db_config["host"],
            dbname=db_config["dbname"],
            user=db_config["user"],
            password=db_config["pwd"]
    )


@app.route("/")
def home():
    # use example query here
    cursor = conn.cursor()
    query = "SELECT COUNT(c.c_custkey) FROM customer as c, nation as n  WHERE c.c_nationkey = n.n_nationkey"
    cursor.execute("EXPLAIN (FORMAT JSON)" + query)
    plans = cursor.fetchall()
    plans_str = str(plans)
    
    return render_template("index.html", query=query, qep=plans_str)

if __name__ == "__main__":
  app.run(debug=True)
