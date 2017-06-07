# -*- coding: utf-8 -*-

from __future__ import print_function

import asyncore
import collections
import logging
import socket
import os
import time

MAX_MESSAGE_LENGTH = 1024

class RemoteClient(asyncore.dispatcher):

    def __init__(self, host, socket, address, client_nb):
        asyncore.dispatcher.__init__(self, socket)
        self.host = host
        self.outbox = collections.deque()
        self.say(str(client_nb) + "\n")

    def say(self, message):
        self.outbox.append(message)

    def handle_read(self):
        client_message = self.recv(MAX_MESSAGE_LENGTH)
        self.host.broadcast(client_message)

    def handle_write(self):
        if not self.outbox:
            return
        message = self.outbox.popleft()
        if len(message) > MAX_MESSAGE_LENGTH:
            raise ValueError('Message too long')
        self.send(message)


class Host(asyncore.dispatcher):

    log = logging.getLogger('Host')

    def __init__(self, address=('127.0.0.1', 9876)):
        asyncore.dispatcher.__init__(self)
        self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
        self.bind(address)
        self.listen(1)
        self.remote_clients = []
        self.nb_client = 0

    def handle_accept(self):
        socket, addr = self.accept()
        self.log.info('[+] New server socket thread started for %s', addr)
        self.nb_client += 1
        self.remote_clients.append(RemoteClient(self, socket, addr, self.nb_client))
        self.log.info('Player %d joined the game', len(self.remote_clients))
        time.sleep(2)
        


    def handle_read(self):
        self.log.info('Received message: %s', self.read())

    def broadcast(self, message):
        self.log.info('Broadcasting message: %s', message)
        for remote_client in self.remote_clients:
            remote_client.say(message)


def init_service():
    print (" ██████╗██╗  ██╗███████╗ ██████╗██╗  ██╗███████╗██████╗ ███████╗")
    print ("██╔════╝██║  ██║██╔════╝██╔════╝██║ ██╔╝██╔════╝██╔══██╗██╔════╝")
    print ("██║     ███████║█████╗  ██║     █████╔╝ █████╗  ██████╔╝███████╗")
    print ("██║     ██╔══██║██╔══╝  ██║     ██╔═██╗ ██╔══╝  ██╔══██╗╚════██║")
    print ("╚██████╗██║  ██║███████╗╚██████╗██║  ██╗███████╗██║  ██║███████║")
    print (" ╚═════╝╚═╝  ╚═╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚══════╝")
    print ("")

if __name__ == '__main__':
    os.system("clear")
    init_service()
    logging.basicConfig(level=logging.INFO)
    logging.info('Creating host game')
    host = Host()
    asyncore.loop()