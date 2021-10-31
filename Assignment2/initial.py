import psycopg2 # need install
import json
import configparser
from flask import Flask, render_template
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__, template_folder="layout")
app.config['SQLALCHEMY_DATABASE_URI']="postgresql://scott:tiger@localhost/db4031"
db = SQLAlchemy(app)

class customer(db.Model):
  __tablename__ = 'customer'
  id = db.Column('c_custkey', db.Integer, primary_key=True)
  name = db.Column('c_name', db.Unicode)


@app.route("/")
def home():
  return render_template("index.html")

if __name__ == "__main__":
  app.run(debug=True)