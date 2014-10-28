import urllib2, socket, sys, time
ip = socket.gethostbyname(socket.getfqdn())
while True:
	try:
		urllib2.urlopen("http://cs4720.cs.virginia.edu/ipregistration/?pokemon=Natu&ip=" + ip)
		sys.exit(0)
	except urllib2.URLError:
		time.sleep(30)