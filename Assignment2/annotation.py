class Annotator:

    def __init__(self):
        self.iCount = 0

    # wrapper function to do preprocessing on the qep and to only return finished string
    def wrapper(self, qep):
        # just to make it a bit nicer
        final = self.annotate(qep[0][0][0]['Plan'], True)[1]
        final = final[:-3]
        final += " to get the final result."
        return final 

    #TODO filters
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

        
        # python 3.8 has no switch function :(

        if query["Node Type"] == 'Seq Scan':
            table = query["Relation Name"]
            name = query["Alias"]
            annie = "perform sequential scan on table {} as {}. \n".format(table, name)
            return table, result + annie

        elif query["Node Type"] == 'Index Scan':
            table = query["Relation Name"]
            name = query["Alias"]
            annie = "perform index scan on table {} as {} using index {} where {}. \n".format(table, name, query["Index Name"], query["Index Cond"])
            return table, result + annie

        elif query["Node Type"] == 'Index-Only Scan':
            table = query["Relation Name"]
            name = query["Alias"]
            annie = "perform index scan on table {} as {} using index {} where {}. \n".format(table, name, query["Index Name"], query["Index Cond"])
            return table, result + annie

        # elif query["Node Type"] == 'Bitmap Heap Scan':


        # elif query["Node Type"] == 'Bitmap Index Scan':


        # elif query["Node Type"] == 'Custom Scan':


        # elif query["Node Type"] == 'CTE Scan':


        # elif query["Node Type"] == 'Foreign Scan':

        
        # elif query["Node Type"] == 'Function Scan':

        
        # elif query["Node Type"] == 'Subquery Scan':

        
        # elif query["Node Type"] == 'TID Scan':

        
        # elif query["Node Type"] == 'Values Scan':

        
        # elif query["Node Type"] == 'Worktable Scan':

        
        elif query["Node Type"] == 'Nested Loop':
            self.iCount += 1
            annie = "perform a nested loop join on tables {} and {}".format(joinTables[0], joinTables[1])
            if "Join Filter" in query:
                annie += " under the condition {}".format(joinTables[0], joinTables[1], query["Join Filter"])
            if not first:
                annie += " to get intermediate table T{}. \n".format(self.iCount)
            else: annie += ". \n"
            return "T" + str(self.iCount), result + annie
        
        elif query["Node Type"] == 'Hash Join':
            self.iCount += 1
            annie = "perform a hash join on tables {} and {} under the condition {}".format(joinTables[0], joinTables[1], query["Hash Cond"])
            if not first:
                annie += " to get intermediate table T{}. \n".format(self.iCount)
            else: annie += ". \n"
            return "T" + str(self.iCount), result + annie

        elif query["Node Type"] == 'Merge Join':
            self.iCount += 1
            annie = "perform a merge join on tables {} and {} under the condition {}".format(joinTables[0], joinTables[1], query["Merge Cond"])
            if not first:
                annie += " to get intermediate table T{}. \n".format(self.iCount)
            else: annie += ". \n"
            return "T" + str(self.iCount), result + annie
        
        elif query["Node Type"] == 'Aggregate':
            self.iCount += 1
            annie = "perform aggregate on table {}".format(joinTables[0])
            if not first:
                annie += " to get intermediate table T{}. \n".format(self.iCount)
            else: annie += ". \n"
            return "T" + str(self.iCount), result + annie
        
        # elif query["Node Type"] == 'Append':

        
        # elif query["Node Type"] == 'BitmapAnd':


        # elif query["Node Type"] == 'BitmapOr':

        
        elif query["Node Type"] == 'Gather':
            self.iCount += 1
            annie = ("perform gather on table {}".format(joinTables[0]))
            if not first:
                annie += " to get intermediate table T{}. \n".format(self.iCount)
            else: annie += ". \n"
            return "T" + str(self.iCount), result + annie

        
        # elif query["Node Type"] == 'Gather Merge':

        
        # elif query["Node Type"] == 'Group':

        
        # elif query["Node Type"] == 'GroupAggregate':

        
        elif query["Node Type"] == 'Hash':
            annie = "perform hashing on table {}. \n".format(joinTables[0])
            return joinTables[0], result + annie

        # elif query["Node Type"] == 'HashAggregate':


        # elif query["Node Type"] == 'HashSetOp':


        # elif query["Node Type"] == 'Incremental Sort':


        # elif query["Node Type"] == 'Limit':


        # elif query["Node Type"] == 'LockRows':


        elif query["Node Type"] == 'Materialize':
            annie = "materialize table {}. \n".format(joinTables[0])
            return joinTables[0], result + annie

        # elif query["Node Type"] == 'MergeAppend':


        # elif query["Node Type"] == 'MixedAggregate':


        # elif query["Node Type"] == 'ModifyTable':


        # elif query["Node Type"] == 'ProjectSet':


        # elif query["Node Type"] == 'Recursive Union':


        # elif query["Node Type"] == 'Result':


        # elif query["Node Type"] == 'SetOp':


        elif query["Node Type"] == 'Sort':
            annie = "perform a sort on table {} with sort key {}. \n".format(joinTables[0], query["Sort Key"])
            return joinTables[0], result + annie

        # elif query["Node Type"] == 'Unique':


        # elif query["Node Type"] == 'WindowAgg':

        else:
            annie = "performing {}. \n".format(query["Node Type"])
            return joinTables[0], result + annie





