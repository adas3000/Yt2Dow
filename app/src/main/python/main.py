from pytube import YouTube
# if mp4ormp4 true download mp3 else download mp4

def doDownload(url,saveIn) :
   YouTube(url).streams.filter(only_audio=True).first().download(output_path='/storage/emulated/0/download')#output_path ="/data/user/0/com.yt.app/files")


def getList(*args):
        return list(args)

