import sys
import re 
import time

def parse_nw(nw):
	if nw[0]=='(':
		nw=nw[1:-1]
		count=0
		for i in range(0,len(nw)):
			letter=nw[i]
			if letter=="(":
				count+=1
			elif letter==")":
				count-=1
			elif (letter==",") and (count==0):
				break_pos=i
				return([nw[:break_pos],nw[break_pos+1:]])
	else:
		return(nw)

def disruptree(input):
	nw=input.rstrip().rstrip(';')
	re_leaf=re.compile('([^,\(\)]+)')
	leafs=re_leaf.findall(nw)
	nodes=[nw]
	divisions=dict()
	while len(nodes)!=0:
		node=nodes.pop()
		divisions[node]=parse_nw(node)
		for child in divisions[node]:
			if not child in leafs:
				nodes.append(child)
	tree=list()
	nodes_index=dict()
	index=0
	chosen=list()
	isTerminal=dict()
	for leaf in leafs:
		tree.append('')
		chosen.append('-')
		nodes_index[leaf]=index
		isTerminal[leaf]=True
		index+=1
	midlines=dict()
	while len(divisions)!=0:
		for index in range(0,len(tree)):
			toWrite=''
			for parent in divisions.keys():
				child1,child2=divisions[parent]
				if (child1 in nodes_index.keys()) and (child2 in nodes_index.keys()):
					index1=nodes_index[child1]
					index2=nodes_index[child2]
					if index1-index2==-1:
						if (index==index1) and (child1 in leafs):
							toWrite=','
							chosen[index]='_'
						elif index==index2:
							if child2 in leafs:
								toWrite='\\'
								if child2 in leafs:
									if isTerminal[child2]:
										toWrite="'"
										isTerminal[child2]=False
								chosen[index]=' '
							else:
								toWrite='\\'
								chosen[index]=' '
							del divisions[parent]
						elif (index==index1+1) and (child1 not in leafs):
							toWrite='/'
							chosen[index]='_'
						if toWrite!='':
							nodes_index[parent]=nodes_index[child1]
							break
					else:
						if index==index2:
							toWrite='\\'
							if child2 in leafs:
								if isTerminal[child2]:
									toWrite="'"
									isTerminal[child2]=False
							chosen[index]=' '
							chosen[index-1]='_'
						if toWrite!='':
							nodes_index[child2]=nodes_index[child2]-1
							break	
			if toWrite!='':
				tree[index]=toWrite+tree[index]
			else :
				tree[index]=chosen[index]+tree[index]
	for index in range(0,len(tree)):
		tree[index]=tree[index]+leafs[index]
	final_tree="<html>"+"<br>".join(tree).replace(" ",'<span style="color:white">|</span>')+"</html>"
	return({"html_tree":final_tree,"leafs":','.join(leafs),"size":str(max([len(x) for x in tree]))})

