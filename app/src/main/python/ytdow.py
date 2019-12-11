from pytube import YouTube
import send
import json



def checkUrl(url):
   global yt
   global urlvalid
   try:
      yt = YouTube(url,on_progress_callback=progress_function)
      urlvalid = True
   except:
      print("Error")
      urlvalid = False  
      send.sendPacket('error:Incorrect youtube url link!')
   

def getVideoTitleAndSize(kind):
    if not urlvalid: 
        print("url incorrect")
        return 

    global video
    if kind == "mp3":
        video = yt.streams.filter(only_audio=True).first()
    elif kind == "mp4":
        video = yt.streams.first()    
    
    global file_size
    global title
    title = video.title
    file_size = video.filesize
    file_sizemb = file_size / 1000000
    send.sendPacket(json.dumps({"title":title,"file_size":file_sizemb}))


def doDownload(saveIn='/storage/emulated/0/download'):
    if not urlvalid:
        print("url incorrect")
        return 

    video.download(output_path = saveIn)



def progress_function(stream,chunk,file_handle,reamining):
    percent = (100*(file_size-reamining))/file_size
    value = "{:00.0f}% downloaded".format(percent)
    send.sendPacket(value)



def percent(bytes,total):
   perc = (float(bytes)/float(total)) * float(100)
   return perc