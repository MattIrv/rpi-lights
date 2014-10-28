import web
from lights_ms2 import set_lights
        
urls = (
    '/rpi', 'rpi'
)
app = web.application(urls, globals())

class rpi:        
    def POST(self):
        data = web.data()
        return set_lights(data)

if __name__ == "__main__":
    app.run()