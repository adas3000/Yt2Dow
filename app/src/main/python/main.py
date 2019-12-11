from pytube import YouTube
# if mp4ormp4 true download mp3 else download mp4

def doDownload(url,kind,saveIn) :
   kind = kind.lower()   
   if not kind=="mp3" or not kind=="mp4":
      pass
      #send error msg

   YouTube(url).streams.filter(file_extension="mp3").first().download(output_path='/storage/emulated/0/download')
   #send title etc. thorught socket 


def getList(*args):
        return list(args)

