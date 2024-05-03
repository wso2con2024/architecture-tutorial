import os
from confluent_kafka import Consumer, KafkaError
from azure.communication.email import EmailClient
import json

# Fetch configuration from environment variables
bootstrap_servers = os.getenv('BOOTSTRAP_SERVERS')
security_protocol = os.getenv('SECURITY_PROTOCOL')
sasl_mechanisms = os.getenv('SASL_MECHANISMS')
sasl_username = os.getenv('KAFKA_USERNAME')
sasl_password = os.getenv('KAFKA_PASSWORD')
session_timeout_ms = os.getenv('SESSION_TIMEOUT_MS')
topic_name = os.getenv('TOPIC_NAME')
preferred_broker = os.getenv('PREFERRED_BROKER')

# Configuration values from environment or configuration file
connection_string = os.environ.get('AZURE_COMM_SERVICES_CONNECTION_STRING')
sender_address = os.environ.get('AZURE_COMM_SERVICES_SENDER_ADDRESS')

email_client = EmailClient.from_connection_string(connection_string)

# Configuration for the Confluent Kafka Consumer

if preferred_broker == 'local-broker':
    conf = {
            'bootstrap.servers': bootstrap_servers,
            'security.protocol': 'PLAINTEXT',
            'group.id': '1',
            'auto.offset.reset': 'earliest',
            'enable.auto.commit': True,
    }
else:
    conf = {
            'bootstrap.servers': bootstrap_servers,
            'security.protocol': security_protocol,
            'sasl.mechanisms': sasl_mechanisms,
            'sasl.username': sasl_username,
            'sasl.password': sasl_password,
            'session.timeout.ms': session_timeout_ms,
            'group.id': 'your_group_id',  # Specify your consumer group
            'auto.offset.reset': 'earliest',  # Start reading at the earliest message
            'enable.auto.commit': True,  # Automatically commit offsets
    }


def send_email(receipient, subject, body):
    recipient = receipient
    subject = subject
    body = body

    message = {
        "senderAddress": sender_address,
        "recipients": {
            "to": [{"address": recipient}],
        },
        "content": {
            "subject": subject,
            "plainText": body,
            "html": "<html><body><p>" + body + "</p></body></html>",
        }
    }

    print("Received !!!!! request for /notifications/send-email")
    
    POLLER_WAIT_TIME = 5
    poller = email_client.begin_send(message)

    time_elapsed = 0
    while not poller.done():
        print("Email send poller status: " + poller.status())

        poller.wait(POLLER_WAIT_TIME)
        time_elapsed += POLLER_WAIT_TIME

        if time_elapsed > 18 * POLLER_WAIT_TIME:
            raise RuntimeError("Polling timed out.")

    if poller.result()["status"] == "Succeeded":
        #return jsonify({"message": "Email sent successfully"}), 200
         print("Successfull for /notifications/send-email-event")
    else:
        #return jsonify({"message": "Failed to send email"}), 500
        print("Failed for /notifications/send-email-event")

def on_reservation_created(recipient, subject, body):
    # Implement the logic for when a reservation is created
    print(f"NotificationService: Sending reservation created notification to user", recipient, subject, body)
    # reservation is json and need to obtain email and the reservation id from the json 
    # then send the email to the user
    send_email(recipient, subject, body)
   

def on_reservation_updated(reservation):
    # Implement the logic for when a reservation is updated
    print(f"NotificationService: Sending reservation updated notification to user", recipient, subject, body)
    send_email(recipient, subject, body)

def on_reservation_cancelled(reservation):
    # Implement the logic for when a reservation is cancelled
    print(f"NotificationService: Sending reservation cancelled notification to user", recipient, subject, body)
    send_email(recipient, subject, body)


# Create a Confluent Kafka Consumer
consumer = Consumer(conf)

# Subscribe to the topic
consumer.subscribe([topic_name])  # Specify the topic name here

try:
    while True:
        msg = consumer.poll(timeout=1.0)  # Poll for messages
        if msg is None:
            continue
        if msg.error():
            if msg.error().code() == KafkaError._PARTITION_EOF:
                # End of partition event
                continue
            elif msg.error():
                print(f"Error: {msg.error()}")
                break
        else:
            # Deserialize the message from JSON
            #message_value = json.loads(msg.value().decode('utf-8'))
            reservation = json.loads(msg.value().decode('utf-8'))
            message_key = msg.key().decode('utf-8') if msg.key() else None
            print(f"Key: {message_key}, Value: {reservation}")

            if message_key == 'CREATED':
                body = (
                    f"Hello {reservation['user']['name']},<br><br>"
                    f"Your reservation for room {reservation['room']['number']} "
                    f"({reservation['room']['type']['name']}) from "
                     f"{'-'.join(map(str, reservation['checkinDate']))} "
                    f"to {'-'.join(map(str, reservation['checkoutDate']))} has been confirmed.")
                recipient = reservation['user']['email']
                subject = "You have a booking at Lux hotels - Reservation Confirmation"
                on_reservation_created(recipient, subject, body)
            elif message_key == 'UPDATED':
                body = (
                    f"Hello {reservation['user']['name']},<br><br>"
                    f"Your reservation for room {reservation['room']['number']} "
                    f"({reservation['room']['type']['name']}) from "
                     f"{'-'.join(map(str, reservation['checkinDate']))} "
                    f"to {'-'.join(map(str, reservation['checkoutDate']))} MODIFICATION has been confirmed.")
                recipient = reservation['user']['email']
                subject = "You have a booking at Lux hotels - Reservation MODIFICATION Confirmation"
                on_reservation_updated(reservation)
            elif message_key == 'DELETED':
                body = (
                    f"Hello {reservation['user']['name']},<br><br>"
                    f"Your reservation for room {reservation['room']['number']} "
                    f"({reservation['room']['type']['name']}) from "
                     f"{'-'.join(map(str, reservation['checkinDate']))} "
                    f"to {'-'.join(map(str, reservation['checkoutDate']))} CANCELLATION has been confirmed.")
                recipient = reservation['user']['email']
                subject = "You have a booking at Lux hotels - Reservation CANCELLATION Confirmation"
                on_reservation_cancelled(reservation)
            else:  
                print("Unknown event type")
finally:
    consumer.close()  # Close the consumer