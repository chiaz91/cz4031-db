from PyQt5 import uic
from PyQt5.QtWidgets import *
#from qt_material import apply_stylesheet

class UI(QMainWindow):
    def __init__(self):
        super(UI, self).__init__()
        # load UI from file (generated by Qt Designer
        uic.loadUi("main.ui", self)
        # link to UI widgets
        self.input_sql = self.findChild(QTextEdit, "input_query")
        self.label_qep = self.findChild(QLabel, "text_plan")
        self.btn_analyse = self.findChild(QPushButton, "btn_analyse")
        self.btn_clear = self.findChild(QPushButton, "btn_clear")
        self.list_database = self.findChild(QComboBox, "combo_databases")
        self.tree_attrs = self.findChild(QTreeWidget, "tree_attrs")
        # init widgets
        self.tree_attrs.setHeaderLabels(["Schema"])
        self.btn_clear.clicked.connect(self.clear)
        self.list_database.currentIndexChanged.connect(self._onDatabaseChanged)
        self.tree_attrs.itemDoubleClicked.connect(self._onSchemaItemDoubleClicked)
    
    def showError(self, errMessage, execption=None):
        dialog = QMessageBox()
        dialog.setStyleSheet("QLabel{min-width: 300px;}");
        dialog.setWindowTitle("Error")
        #dialog.setIcon(QMessageBox.Warning)
        dialog.setText(errMessage)
        if execption is not None:
            dialog.setDetailedText(str(execption))
        dialog.setStandardButtons(QMessageBox.Ok)
        #dialog.buttonClicked.connect(cb)
        dialog.exec_()

    def clear(self):
        self.input_sql.setPlainText("")
        self.label_qep.setText("")
        
    def readInput(self):
        return self.input_sql.toPlainText()
    
    def setInput(self, text):
        self.input_sql.setPlainText(text)
    
    def setResult(self, text):
        self.label_qep.setText(text)
            
    def setSchema(self, schema=None):
        self.tree_attrs.clear()
        if schema is None:
            return
        for table in schema:
            table_item = QTreeWidgetItem([table])
            for attr in schema[table]:
                attr_item =  QTreeWidgetItem([attr])
                table_item.addChild(attr_item)  
            self.tree_attrs.addTopLevelItem(table_item)
            
    # callback setter
    def setOnAnalyseClicked(self, callback):
        if callback:
            self.btn_analyse.clicked.connect(callback)
        
    def setOnDatabaseChanged(self, callback):
        self.cb_db_changed = callback
        
    def setListDatabase(self, list_db=["TPC-H"]):
        self.list_database.clear()
        self.list_database.addItems(list_db)

            
    # private events handling 
    def _onDatabaseChanged(self, cur_index):
        if hasattr(self, "cb_db_changed"):
            self.cb_db_changed()
        
    def _onSchemaItemDoubleClicked(self, item, col):
        # append item text to input text area
        self.setInput( f"{self.readInput()} {item.text(col)} ") 
        


'''
# maybe this part move to main script
if __name__ == "__main__":
    app = QApplication(sys.argv)
    apply_stylesheet(app, theme="light_cyan_500.xml")
    window = UI()
    
    # fake schema
    schema = {
        "tabel1":["item_1", "item_2", "item_3"],
        "tabel2":["item_4", "item_5", "item_6"],
        "tabel3":["item_7", "item_8", "item_9"],
        "tabel4":["item_10", "item_11", "item_12"]
    }
    window.setSchema(schema)
    
    # assigning callback
    window.setOnClicked(
        lambda: window.setResult( window.readInput() )
    )
    window.show()
    sys.exit(app.exec_())
'''