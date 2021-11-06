class Annotator:

    def __init__(self):
        self.iCount = 0
        self.stepCount = 0

    # wrapper function to do preprocessing on the qep and to only return finished string
    def wrapper(self, qep):
        # just to make it a bit nicer
        final = self.annotate(qep[0][0][0]['Plan'], True)[1]
        final = final[:-3]
        final += " to get the final result."
        return final 

    def annotate(self, query, first = False):
        
        # for storing previous tables since they are not included in the qep
        joinTables = []

        # result string to be combined with current iter's output and returned
        result = ""

        if "Plans" in query:
            for plan in query["Plans"]:
                temp = self.annotate(plan)
                joinTables.append(temp[0])
                result += temp[1]


        self.stepCount += 1
        result += "Step {}: ".format(self.stepCount)

        # python 3.8 has no switch function :(

        if query["Node Type"] == 'Seq Scan':
            table = query["Relation Name"]
            name = query["Alias"]
            annie = "Perform sequential scan on table {} as {}".format(table, name)
            if "Filter" in query:
                annie += " with filter {}".format(query["Filter"])
            annie += ". \n"
            return table, result + annie


        elif query["Node Type"] == 'Index Scan':
            table = query["Relation Name"]
            name = query["Alias"]
            annie = "Perform index scan on table {} as {} using index on {}".format(table, name, query["Index Name"])
            if "Index Cond" in query:
                annie += " where {}".format(query["Index Cond"])
            if "Filter" in query:
                annie += " with filter {}".format(query["Filter"])
            annie += ". \n"
            return table, result + annie


        elif query["Node Type"] == 'Index-Only Scan':
            table = query["Relation Name"]
            name = query["Alias"]
            annie = "Perform index scan on table {} as {} using index on {}".format(table, name, query["Index Name"])
            if "Index Cond" in query:
                annie += " where {}".format(query["Index Cond"])
            if "Filter" in query:
                annie += " with filter {}".format(query["Filter"])
            annie += ". \n"
            return table, result + annie


        elif query["Node Type"] == 'CTE Scan':
            table = query["CTE Name"]
            name = query["Alias"]
            annie = "Perform CTE scan on table {} as {}".format(table, name)
            if "Filter" in query:
                annie += " with filter {}".format(query["Filter"])
            annie += ". \n"
            return table, result + annie


        elif query["Node Type"] == 'Foreign Scan':
            table = query["Relation Name"]
            name = query["Alias"]
            annie = "Perform foreign scan on table {} from schema {} as {}. \n".format(table, query["Schema"], name)
            return table, result + annie

        
        elif query["Node Type"] == 'Function Scan':
            table = query["Schema"]
            name = query["alias"]
            annie = "Perform function {} on schema {} and return the results as {}".format(query["Function Name"], table, name)
            if "Filter" in query:
                annie += " with filter {}".format(query["Filter"])
            annie += ". \n"
            return table, result + annie

        
        elif query["Node Type"] == 'Subquery Scan':
            annie = "The subquery results from the previous operation is read"
            if "Filter" in query:
                annie += " with filter {}".format(query["Filter"])
            annie += ". \n"
            return joinTables[0], result + annie

        
        elif query["Node Type"] == 'TID Scan':
            table = query["Relation"]
            name = query["Alias"]
            annie = "Perform a Tuple ID scan on table {} as {}. \n".format(table ,name)
            return table, result + annie

        
        elif query["Node Type"] == 'Nested Loop':
            self.iCount += 1
            annie = "Perform a nested loop join on tables {} and {}".format(joinTables[0], joinTables[1])
            if "Join Filter" in query:
                annie += " under the condition {}".format(joinTables[0], joinTables[1], query["Join Filter"])
            if "Filter" in query:
                annie += " with filter {}".format(query["Filter"])
            if not first:
                annie += " to get intermediate table T{}. \n".format(self.iCount)
            else: annie += ". \n"
            return "T" + str(self.iCount), result + annie

        
        elif query["Node Type"] == 'Hash Join':
            self.iCount += 1
            annie = "Perform a hash join on tables {} and {}".format(joinTables[0], joinTables[1])
            if "Hash Cond" in query:
                annie += " under the condition {}".format(query["Hash Cond"])
            if "Filter" in query:
                annie += " with filter {}".format(query["Filter"])
            if not first:
                annie += " to get intermediate table T{}. \n".format(self.iCount)
            else: annie += ". \n"
            return "T" + str(self.iCount), result + annie


        elif query["Node Type"] == 'Merge Join':
            self.iCount += 1
            annie = "Perform a merge join on tables {} and {}".format(joinTables[0], joinTables[1])
            if "Merge Cond" in query:
                annie += " under the condition {}".format(query["Merge Cond"])
            if "Filter" in query:
                annie += " with filter {}".format(query["Filter"])
            annie += ". \n"
            if not first:
                annie += " to get intermediate table T{}. \n".format(self.iCount)
            else: annie += ". \n"
            return "T" + str(self.iCount), result + annie

        
        elif query["Node Type"] == 'Aggregate':
            self.iCount += 1
            annie = "Perform aggregate on table {}".format(joinTables[0])
            if not first:
                annie += " to get intermediate table T{}. \n".format(self.iCount)
            else: annie += ". \n"
            return "T" + str(self.iCount), result + annie

        
        elif query["Node Type"] == 'Append':
            self.iCount += 1
            annie = "Append the results from table {} to table {}".format(joinTables[0], joinTables[1]) 
            if not first:
                annie += " to get intermediate table T{}. \n".format(self.iCount)
            else: annie += ". \n"
            return "T" + str(self.iCount), result + annie

        
        elif query["Node Type"] == 'Gather':
            self.iCount += 1
            annie = ("Perform gather on table {}".format(joinTables[0]))
            if not first:
                annie += " to get intermediate table T{}. \n".format(self.iCount)
            else: annie += ". \n"
            return "T" + str(self.iCount), result + annie

        
        elif query["Node Type"] == 'Gather Merge':
            annie = "The results of the previous operation are gathered and merged. \n"
            return joinTables[0], result + annie

        
        elif query["Node Type"] == 'GroupAggregate':
            self.iCount += 1
            annie = "Perform a group aggregate on table {}".format(joinTables[0])
            if not first:
                annie += " to get intermediate table T{}. \n".format(self.iCount)
            else: annie += ". \n"
            return "T" + str(self.iCount), result + annie

        
        elif query["Node Type"] == 'Hash':
            annie = "Perform hashing on table {}. \n".format(joinTables[0])
            return joinTables[0], result + annie


        elif query["Node Type"] == 'HashAggregate':
            self.iCount += 1
            annie = "Perform a hash aggregate on table {}".format(joinTables[0])
            if not first:
                annie += " to get intermediate table T{}. \n".format(self.iCount)
            else: annie += ". \n"
            return "T" + str(self.iCount), result + annie


        elif query["Node Type"] == 'Incremental Sort':
            annie = "An incremetal sort is performed on table {} with sort key {}. \n".format(joinTables[0], query["Sort Key"])
            return joinTables[0], result + annie


        elif query["Node Type"] == 'Limit':
            annie = "The specified number of rows is selected from table {}. \n".format(joinTables[0])
            return joinTables[0], result + annie


        elif query["Node Type"] == 'Materialize':
            annie = "Materialize table {}. \n".format(joinTables[0])
            return joinTables[0], result + annie


        elif query["Node Type"] == 'MergeAppend':
            self.iCount += 1
            annie = "Results from table {} are appended to table {}".format(joinTables[0], joinTables[1])
            if not first:
                annie += " to get intermediate table T{}. \n".format(self.iCount)
            else: annie += ". \n"
            return "T" + str(self.iCount), result + annie


        elif query["Node Type"] == 'ModifyTable':
            table = query["Relation Name"]
            annie = "Table {} is modified. \n ".format(table)
            return table, result + annie


        elif query["Node Type"] == 'SetOp':
            self.iCount += 1
            annie = "A set operation is performed on table {}".format(joinTables[0])
            if not first:
                annie += " to get intermediate table T{}. \n".format(self.iCount)
            else: annie += ". \n"
            return "T" + str(self.iCount), result + annie


        elif query["Node Type"] == 'Sort':
            annie = "Perform a sort on table {} with sort key {}. \n".format(joinTables[0], query["Sort Key"])
            return joinTables[0], result + annie


        elif query["Node Type"] == 'Unique':
            table = query["Subplan Name"] if "Subplan Name" in query else joinTables[0]
            annie = "Duplicates are removed from table {}. \n".format(table)
            return table, result + annie


        else:
            annie = "Perform {}. \n".format(query["Node Type"])
            return joinTables[0], result + annie





