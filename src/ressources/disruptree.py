import sys
import re 
import time



def parse_nw(nw):
	re_clade=re.compile('^(#\d+)(.*)$')
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
				m=re_clade.match(nw[break_pos+1:])
				if m:
					break_pos+=len(m.group(1))
				return([nw[:break_pos],nw[break_pos+1:]])
	else:
		return(nw)

def disruptree(input):
	clades=dict()
	nw=input.rstrip().rstrip(';')
	re_leaf=re.compile('([^#,\(\)]{1}[^,\(\)]+)')
	re_clade=re.compile('^(.*)(#\d+)$')
	leafs=re_leaf.findall(nw)
	nodes=[nw]
	divisions=dict()
	while len(nodes)!=0:
		node=nodes.pop()
		divisions[node]=parse_nw(node)
		for i in range(0,len(divisions[node])):
			child=divisions[node][i]
			m=re_clade.match(child)
			if m:
				child=m.group(1)
				divisions[node][i]=child
				clades[child]=m.group(2)
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
	count=0
	while len(divisions)!=0:
		count+=1
		for index in range(0,len(tree)):
			toWrite=''
			for parent in divisions.keys():
				if parent in clades.keys():
					parent_clade=True
				else:
					parent_clade=False
				child1,child2=divisions[parent]
				if (child1 in nodes_index.keys()) and (child2 in nodes_index.keys()):
					index1=nodes_index[child1]
					index2=nodes_index[child2]
					if index1-index2==-1:
						if (index==index1) and (child1 in leafs):
							toWrite=','
							if child1 in clades.keys():
								toWrite='<span style="color:red"><b>'+toWrite+'</b></span>'
							chosen[index]='_'
						elif index==index2:
							if child2 in leafs:
								if isTerminal[child2]:
									toWrite="'"
								else:
									toWrite="\\"
								if child2 in clades.keys():
									toWrite='<span style="color:red"><b>'+toWrite+'</b></span>'
								isTerminal[child2]=False
								chosen[index]='.'
							else:
								toWrite='\\'
								if child2 in clades.keys():
									toWrite='<span style="color:red"><b>'+toWrite+'</b></span>'
								chosen[index]='.'
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
							if child2 in clades.keys():
								toWrite='<span style="color:red"><b>'+toWrite+'</b></span>'
							if child2 in leafs:
								if isTerminal[child2]:
									toWrite="'"
									isTerminal[child2]=False
							chosen[index]='.'
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
	final_tree="<html>"+"<br>".join(tree).replace(".",'<span style="color:white">|</span>')+"</html>"
	return({"html_tree":final_tree,"leafs":','.join(leafs),"size":str(count+max([len(x) for x in leafs]))})

