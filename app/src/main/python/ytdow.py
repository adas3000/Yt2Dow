from pytube import YouTube
import send
import json



def checkUrl(url):
   try:
      yt = YouTube(url,on_progress_callback=progress_function)
      send.sendPacket('Ok')
   except:
      print("Error")
      send.sendPacket('error:Incorrect youtube url link!')
   

def getVideoTitleAndSize(url,kind):
    yt = YouTube(url,on_progress_callback=progress_function)

    if kind == "mp3":
        video = yt.streams.filter(only_audio=True).first()
    elif kind == "mp4":
        video = yt.streams.first()    
    
    global file_size
    file_size = video.filesize
    send.sendPacket(json.dumps({"title":video.title,"file_size":file_size/1000000}))
    return video


def doDownload(url,kind,saveIn='/storage/emulated/0/download'):

    yt = YouTube(url,on_progress_callback=progress_function)
    
    if kind == "mp3":
        video = yt.streams.filter(only_audio=True).first()
    elif kind == "mp4":
        video = yt.streams.first()    
    
    global file_size
    file_size = video.filesize
    video.download(output_path = saveIn)



def progress_function(stream,chunk,file_handle,reamining):
    percent = (100*(file_size-reamining))/file_size
    value = "{:00.0f}% downloaded".format(percent)
    send.sendPacket(value)



def percent(bytes,total):
   perc = (float(bytes)/float(total)) * float(100)
   return perc