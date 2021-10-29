import sys 
import time
from PyQt5.QtWidgets import QApplication # need install
from qt_material import apply_stylesheet # need install
from interface import UI


def doSomething(ui):
    query = ui.readInput()
    if not query:
        print("query is empty")
        return
    print("query: %s"%query)
    # simulate doing some long duration task
    time.sleep(2)
    ui.setResult( "task completed" )
    
if __name__ == "__main__":
    # load config file
    # connect to database
    # show app ui
    app = QApplication(sys.argv)
    apply_stylesheet(app, theme="light_cyan_500.xml")
    window = UI()
    # assigning callback
    window.setOnClicked( lambda: doSomething(window) )
    
    window.show()
    sys.exit(app.exec_())
