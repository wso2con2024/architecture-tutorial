# Building Cloud Native Applications: Hotel Reservation System

## Overview

This tutorial will walk you through the creation of a cloud-native hotel reservation system, which includes:

1. A Hotel Reservation Service developed using Java Spring Boot, Python, and Ballerina.
2. A Hotel Reservation Web Application developed using ReactJS.

## Prerequisites

Before you start, make sure you have:

1. **GitHub Account**
   - Ensure you have a GitHub account. Sign up or log in here: [GitHub](https://github.com/)
2. **Microsoft Visual Studio Code**
   - Download and install Visual Studio Code if not already installed: [VSCode](https://code.visualstudio.com/)
   - Install the WSO2 Ballerina plugin from the VSCode extensions marketplace.
3. **Git**
   - Install Git for version control. Follow the installation guide here: [Git](https://git-scm.com/downloads)
4. **Web Browser**
   - A recent version of Google Chrome or Mozilla Firefox is required.
5. **HTTP Client**
   - Install Postman and curl for testing HTTP requests. Download Postman here: [Postman](https://www.postman.com/downloads/)
   - Curl is usually pre-installed on Unix-based systems. For Windows, you can download it here: [Curl](https://curl.se/windows/)
6. **Ballerina**
   - Install the latest version of Ballerina. Follow the installation instructions here: [Ballerina Swan Lake](https://ballerina.io/)
   - Ensure the Ballerina VSCode extension is installed.
7. **Python**
   - Install Python 3.x from the official website: [Python Downloads](https://www.python.org/downloads/)
8. **Kafka Broker**
   - Set up a Kafka broker. You can use the Confluent Cloud SaaS broker with a free trial: [Confluent Cloud](https://confluent.cloud/)
9. **Azure Communication Services**
   - Create an account and set up Azure Communication Services. Guidance on key generation will be provided during setup. Start with a trial account here: [Azure Communication Services](https://azure.microsoft.com/en-us/products/communication-services)
10. **Choreo Account**
    - Sign up for a Choreo account to integrate and deploy services efficiently.

# Project Setup and Prerequisites

This document outlines the necessary tools and services required to set up and run the project. Please ensure you have the following installed and configured on your workstation.


## Business Scenario

The goal is to build a reservation system for a luxury hotel that allows users to search for rooms, make reservations, and manage their bookings.

## High-Level Steps

1. Develop the HTTP service using Spring Boot, Python services for email communication, and Ballerina for BFF services implementation using GraphQL.
2. Push the code to your GitHub account.
3. Deploy the cloud-native application on Choreo, including both services and the web application.

## Detailed Steps

### 1. Develop the GraphQL Service for Rooms Search Catalog (Experience API)

- Implement Ballerina GraphQL for the room search catalog.

### 2. Develop Java Spring Boot HTTP Service (Domain APIs)

Develop an HTTP service with Java Spring Boot that includes endpoints for:

- **Making reservations**: Handle POST requests with user data and return a confirmation with a unique reference number.
- **Listing reservations**: Allow users to retrieve their booking details.
- **Updating reservations**: Enable modifications to existing bookings.
- **Canceling reservations**: Provide a way for users to cancel their bookings.

### 3.Develop Python HTTP for Email Notification  (Domain APIs)
- The Python service will be designed to send email notifications related to user reservation activities. This service will interact with Azure communication services to manage the dispatch of emails.


## Project Objective

Develop a reservation platform for a premium hotel establishment.

## Proposed Solution

Create a web application that enables hotel guests to book rooms. The application will offer:

![Architecture Diagram](/images/architecture-v1.jpeg)

### Room Search Feature

- Guests can search for rooms by check-in and check-out dates, with a filter for the number of guests.
- Search results will display a list of room types, each with a "Reserve" button.

### Room Reservation Process

- Guests must provide personal information to book a room.
- The "Reserve" button is enabled after all fields are filled in correctly, and a unique reference number is provided upon reservation.

### Reservation Management

- Guests can view and manage their reservations after logging in.
- Options to update or cancel the reservation are provided for each booking.

### Reservation Modification

- Guests can modify any part of their reservation.

### Reservation Cancellation

- Guests can cancel their reservations easily through the booking system.

### Project Setup Guidance 
[Set up guidance](/docs/Building_Cloud_Native_Application_Project_Setup.pdf)



