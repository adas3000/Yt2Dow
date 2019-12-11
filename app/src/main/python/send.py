import socket

UDP_IP = "127.0.0.1"
UDP_PORT = 5005
MESSAGE = "Hello, World!"
print ("UDP target IP:"+ UDP_IP)
print ("UDP target port:"+ str(UDP_PORT))

sock = socket.socket(socket.AF_INET, 
socket.SOCK_DGRAM) 
#sock.sendto(MESSAGE.encode(), (UDP_IP, UDP_PORT))


def sendPacket(message):
    sock.sendto(message.encode(),(UDP_IP,UDP_PORT))