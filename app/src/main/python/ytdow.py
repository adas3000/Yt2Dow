from pytube import YouTube
import send
import json


yt = YouTube('https://www.youtube.com/watch?v=LCcIx6bCcr8').streams.filter(mime_type='audio/mp4').first().download()


print("Finished")