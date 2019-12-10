from pytube import YouTube
# if mp4ormp4 true download mp3 else download mp4

def doDownload(url,mp3ormp4) :
    if mp3ormp4 == True:
        YouTube(url).streams.filter(only_audio=True).first().download()
    else:
        YouTube(url).streams.first().download()

def getList(*args):
        return list(args)
