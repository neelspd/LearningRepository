def file_lengthy(fname):
        with open(fname) as f:
                for i, l in enumerate(f):
                        pass
        return i + 1
print("Number of lines in the file: ",file_lengthy("test.txt"))

def main():
    Websites = open("randomsites.txt","r")
    for link in Websites:
        c = Crawler()
        c.crawl_page(link)
    Websites.close()
    


if __name__ == '__main__':
main()


fname = input("Enter file name: ")
num_lines = 0
with open(fname, 'r') as f:
    for line in f:
        num_lines += 1
print("Number of lines:")
print(num_lines)


lines_seen = set()  # holds lines already seen
outfile = open('foo.txt', "w")
infile = open('bar.txt', "r")
print "The file bar.txt is as follows"
for line in infile:
    print line
    if line not in lines_seen:  # not a duplicate
        outfile.write(line)
        lines_seen.add(line)
outfile.close()
print "The file foo.txt is as follows"
for line in open('foo.txt', "r"):
    print line
    
    
    from collections import Counter 
  
data_set = "Welcome to the world of Geeks " \ 
"This portal has been created to provide well written well" \ 
"thought and well explained solutions for selected questions " \ 
"If you like Geeks for Geeks and would like to contribute " \ 
"here is your chance You can write article and mail your article " \ 
" to contribute at geeksforgeeks org See your article appearing on " \ 
"the Geeks for Geeks main page and help thousands of other Geeks. " \ 
  
# split() returns list of all the words in the string 
split_it = data_set.split() 
  
# Pass the split_it list to instance of Counter class. 
Counter = Counter(split_it) 
  
# most_common() produces k frequently encountered 
# input values and their respective counts. 
most_occur = Counter.most_common(4) 
  
print(most_occur) 

with open('file') as f:
    cnts = Counter(l.strip() for l in f)

# Display 3 most common lines
cnts.most_common(3)


    import re
    import datetime
    filepath = 'new 1.txt'
    datepattern = '%d-%m-%Y'
    with open(filepath, 'r') as f:
    	file = f.read()
    x = re.findall('\d\d-\d\d-\d\d\d\d', file)
    y = []
    for item in x:
    	try:
    		date = datetime.datetime.strptime(item, datepattern)
    		y.append(date)
    	except:
    		pass
    res = []
    for item in y:
    	a, b = str(item).split(' ')
    	res.append(a)
    	
    for item in res: print(item)
