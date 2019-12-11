from pytube import YouTube
import send


def doDownload(url,kind,saveIn='/storage/emulated/0/download') :
   kind = kind.lower()   
   if not kind=="mp3" or not kind=="mp4":
      pass
      #send error msg
   yt = YouTube(url,on_progress_callback=progress_function)
   
   video = yt.streams.filter(progressive=True,file_extension=kind).order_by('resolution').first()
   global file_size
   global title
   file_size = video.filesize
   title = video.title
   video.download(output_path=saveIn)
   

def progress_function(stream,chunk,file_handle,reamining):
    percent = (100*(file_size-reamining))/file_size
    value = "{:00.0f}% downloaded".format(percent)
    #value = "{:00.0f}".format(percent)
    send.sendPacket(value)



def percent(bytes,total):
   perc = (float(bytes)/float(total)) * float(100)
   return perc


def getList(*args):
        return list(args)



#doDownload("https://www.youtube.com/watch?v=pXdY1B-KVJg","mp4") 