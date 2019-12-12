from pytube import YouTube
import send
import json


def doDownload(url,kind,saveIn='/storage/emulated/0/download') :

   yt = None
   try:
      yt = YouTube(url,on_progress_callback=progress_function)
      send.sendPacket('ok:Good url link')
   except:
      print("Error")
      send.sendPacket('error:Non valid youtube url link!')
      return 

   video = None
   if kind == 'mp3':
      video = yt.streams.filter(only_audio=True).first()
   else:
      video = yt.streams.first()

   global file_size
   global title
   global filepath
   file_size = video.filesize
   title = video.title
  

   video.download(output_path=saveIn)

def getVideoInfo(url,kind,saveIn='/storage/emulated/0/download'):
   yt = None
   try:
      yt = YouTube(url,on_progress_callback=progress_function)
      send.sendPacket('ok:Good url link')
   except:
      print("Error")
      send.sendPacket('error:Non valid youtube url link!')
      return 

   video = None
   if kind == 'mp3':
      video = yt.streams.filter(only_audio=True).first()
   else:
      video = yt.streams.first()

   mb_size = video.filesize/1000000
   mb_size = "%.1f" % mb_size
   filepath = saveIn+"/"+title
   print("file path: "+filepath)

   send.sendPacket(json.dumps({"title":video.title,"file_size":mb_size,"file_path":filepath}))
   


def progress_function(stream,chunk,file_handle,reamining):
    percent = (100*(file_size-reamining))/file_size
    value = "{:00.0f}% downloaded".format(percent)
    send.sendPacket(value)



def percent(bytes,total):
   perc = (float(bytes)/float(total)) * float(100)
   return perc