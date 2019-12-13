import socket

UDP_IP = "127.0.0.1"
UDP_PORT = 5005
sock = socket.socket(socket.AF_INET, 
socket.SOCK_DGRAM) 


def sendPacket(message,port=5005):
    sock.sendto(message.encode(),(UDP_IP,port))