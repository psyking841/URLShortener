from django.shortcuts import render
from django.template import loader
from django.http import HttpResponse
from subprocess import Popen
from subprocess import PIPE
import urllib.parse
from django.shortcuts import redirect


def index(request):
    #template = loader.get_template('search/index.html')
    #return HttpResponse(template.render(None, request))
    #some_var = "abc"
    #context = {'term':some_var}
    #context = None
    return render(request, 'shorturls/index.html')
    #return HttpResponse("Hello, world. You're at the polls index.")

def conversion_results(request):
	long_url = request.POST['long_url']
	short_url = request.POST['short_url']
	template = loader.get_template('shorturls/results.html')
	if long_url:
		res = jar_wrapper('-s', long_url)
		context = {
			'long_res': '',
			'short_res': res
		}
		return HttpResponse(template.render(context, request))

	if short_url:
		res = jar_wrapper('-l', short_url)
		context = {
			'long_res': res,
			'short_res': ''
		}
		return HttpResponse(template.render(context, request))
    
# Create your views here.

def url_redirect(request, short_url):
	res = jar_wrapper('-l', short_url)
	long_url = urllib.parse.urljoin("http:/", res.decode('utf-8'))
	return redirect(long_url)


def jar_wrapper(*args):
    process = Popen(['scala', '/home/public/URLShortener-assembly-0.1.jar'] + list(args), stdout=PIPE, stderr=PIPE)
    # while process.poll() is None:
    #     line = process.stdout.readline()
    #     if line != '' and line.endswith('\n'):
    #         ret.append(line[:-1])
    stdout, stderr = process.communicate()
    if stdout == '':
    	return 'ERROR!'
    return stdout
