import json

title = "Codiac"
file_size = "10000"

msg = json.dumps({"title":title,"file_size":file_size})

print(msg)