import json
from bootstrap import *

def set_lights(data):
    json_obj = json.dumps(data)
    lights = json_obj['lights']
    propagate = json_obj['propagate']
    led.fill(Color(0, 0, 0, 0), start=0, end=31)
    for light in lights:
        light_id = light['lightID'] - 1
        if propagate:
            led.fill(Color(light['red'], light['green'], light['blue'], light['intensity']), start=light_id, end=31)
        else:
            led.fill(Color(light['red'], light['green'], light['blue'], light['intensity']), start=light_id, end=light_id)