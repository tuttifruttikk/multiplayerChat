# multiplayerChat / многопользовательский чат
Клиент не знает порт сервера. Сервер в отдельном потоке шлет на широковещательный UDP адрес пакет с номером порта для TCP.  Клиент при старте принимает широковещательное UDP сообщение, берет оттуда TCP порт и соединяется.
-
The client does not know the server port. The server in a separate stream sends a packet with the port number for TCP to the broadcast UDP address. The client at startup receives a broadcast UDP message, takes a TCP port from there and connects.
-
