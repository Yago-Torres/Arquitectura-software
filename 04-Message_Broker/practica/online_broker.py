from flask import Flask, request, jsonify
import threading
import time
import json
from queue import Queue
from werkzeug.serving import run_simple
from werkzeug.middleware.dispatcher import DispatcherMiddleware
from geventwebsocket import WebSocketServer, WebSocketApplication, Resource
from collections import OrderedDict  # Add to imports


app = Flask(__name__)
message_queue = Queue()
connections = set()
lock = threading.Lock()

class WebSocketApp(WebSocketApplication):
    def on_open(self):
        with lock:
            connections.add(self.ws)
    
    def on_close(self, reason):
        with lock:
            connections.discard(self.ws)
    
    def on_message(self, message):
        pass  # Not needed for basic pub/sub

@app.route('/queues/<name>', methods=['PUT'])
def crear_cola(name):
    return jsonify({"status": "created"}), 201

@app.route('/queues/<name>/messages', methods=['POST'])
def publicar_mensaje(name):
    try:
        content = request.json['content']
        ttl = request.json.get('ttl', 300)
    except KeyError:
        return jsonify({"error": "Missing 'content' field"}), 400

    message = json.dumps({"content": content, "ttl": ttl})
    message_queue.put(message)
    
    # Broadcast to all connected clients
    with lock:
        for ws in list(connections):
            try:
                ws.send(message)
            except:
                connections.discard(ws)
    
    return jsonify({"status": "published"}), 202

def run_broker():
    # WebSocket server
    WebSocketServer(
        ('0.0.0.0', 5000),
        Resource(OrderedDict([('/queues', WebSocketApp)])),  # Use OrderedDict
        debug=False
    ).serve_forever()

if __name__ == '__main__':
    # Run Flask and WebSocket in separate threads
    flask_thread = threading.Thread(
        target=lambda: run_simple(
            'localhost', 5001, app,
            use_reloader=False, 
            threaded=True
        )
    )
    flask_thread.daemon = True
    flask_thread.start()
    
    # Run WebSocket server in main thread
    run_broker()