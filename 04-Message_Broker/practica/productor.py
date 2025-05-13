import requests

BROKER_URL = "http://localhost:5000"  # Change to broker's IP

def declare_queue(queue_name):
    response = requests.post(f"{BROKER_URL}/declare_queue/{queue_name}")
    return response.json()

def publish(queue_name, message):
    response = requests.post(
        f"{BROKER_URL}/publish/{queue_name}",
        json={"message": message}
    )
    return response.json()

if __name__ == '__main__':
    declare_queue("test_queue")
    for i in range(5):
        publish("test_queue", f"Message {i}")
        print(f"Published message {i}")