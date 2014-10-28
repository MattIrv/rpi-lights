import urllib2, socket, sys, time
while True:
	try:
		s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
		s.connect(("gmail.com", 80))
		ip = s.getsockname()[0]
		s.close()
		urllib2.urlopen("http://cs4720.cs.virginia.edu/ipregistration/?pokemon=Natu&ip=" + ip)
		sys.exit(0)
	except urllib2.URLError:
		time.sleep(30)