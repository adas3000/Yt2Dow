from pytube import YouTube


yt = YouTube('https://www.youtube.com/watch?v=pXdY1B-KVJg').streams.filter(adaptive=True).first().download()
