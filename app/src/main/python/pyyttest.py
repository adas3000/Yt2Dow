from pytube import YouTube


yt = YouTube('https://www.youtube.com/watch?v=9B5OXpt53DY').streams.filter(only_audio=True).first().download()
print("Finished")