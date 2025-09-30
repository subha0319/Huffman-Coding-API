# Huffman Coding API & Visualizer

A full-stack web application that provides a real-time, interactive tool to demonstrate Huffman coding, a fundamental lossless data compression algorithm. This project was built to showcase the practical application of Data Structures and Algorithms (DSA) concepts in a modern, deployed web environment.

**Live Application Link:** [**https://huffman-coding-api-sqe8.onrender.com**](https://huffman-coding-api-soe8.onrender.com/) 

*(Note: The free hosting instance may spin down due to inactivity, causing a slight delay of up to 50 seconds on the first load.)*

---

## Features

-   **Compress Text**: Accepts any string of text and returns the Huffman-encoded binary string and the corresponding character-to-code mapping table.
-   **Decompress Text**: Accepts an encoded binary string and its code table to perfectly reconstruct the original text.
-   **Live Stats**: Instantly calculates and displays the original size, compressed size, and the resulting compression ratio.
-   **RESTful Backend API**: A robust backend built with Spring Boot to handle all the complex compression/decompression logic.
-   **Responsive React UI**: A clean, simple, and responsive user interface built with React for a seamless user experience.

---

## Real-World Use Cases of Huffman Coding

While this project is a visualizer, Huffman coding is a foundational algorithm used in many real-world technologies:

-   **File Compression:** It is a key component in compression tools like **PKZIP** (the format used by `.zip` files) and **GZIP**.
-   **Media Formats:** Multimedia formats such as **JPEG** for images, **MP3** for audio, and **MPEG** for video use Huffman coding as part of their compression process to reduce file sizes.
-   **Network Communication:** It's used to reduce the amount of data that needs to be transmitted over networks, improving speed and efficiency.

---

## Tech Stack

| Category      | Technology                                    |
| ------------- | --------------------------------------------- |
| **Backend** | Java 21, Spring Boot, Maven                   |
| **Frontend** | React, JavaScript (ES6+), CSS3                |
| **Deployment**| Render (Backend via Docker, Frontend as Static Site) |

---

## Project Structure

The project is a monorepo containing two main parts: the backend API and the frontend UI.
```bash
/
├── backend/
│   ├── src/main/java/com/github/subha0319/huffman_api/
│   │   ├── HuffmanApiApplication.java  # Main Spring Boot application
│   │   ├── HuffmanController.java      # REST API endpoints
│   │   ├── HuffmanService.java         # Core compression/decompression logic
│   │   └── HuffmanNode.java            # Node for the Huffman Tree
│   ├── src/test/java/                    # JUnit tests
│   ├── pom.xml                           # Maven configuration
│   └── Dockerfile                        # Docker configuration for deployment
│
└── frontend/
│  ├── public/                           # Static assets and index.html
│  ├── src/
│  │   ├── App.js                        # Main React component and UI logic
│  │   ├── App.css                       # Styles for the App component
│  │   └── index.css                     # Global styles
│  └── package.json                      # NPM dependencies and scripts
└── .gitignore                        # Files to ignore for Node.js
```
---

## Getting Started

To run this project on your local machine, follow these steps.

### Prerequisites

-   Java JDK 21 or later
-   Maven 3.8+
-   Node.js v14+ and npm

### 1. Configure for Local Environment

For local testing, the frontend needs to connect to the local backend server.

-   In `frontend/src/App.js`, ensure the `API_URL` constant is set to the local address:
    ```javascript
    const API_URL = 'http://localhost:8080/api';
    ```
-   In `backend/.../HuffmanController.java`, ensure the `@CrossOrigin` annotation points to the local frontend:
    ```java
    @CrossOrigin(origins = "http://localhost:3000")
    ```

### 2. Run the Backend Server

```bash
# Navigate to the backend directory
cd backend

# Run the Spring Boot application (use mvnw.cmd on Windows)
./mvnw spring-boot:run
```
The backend API will now be running on http://localhost:8080.


### 3\. Run the Frontend Application

```bash
# In a new terminal, navigate to the frontend directory
cd frontend

# Install the necessary dependencies
npm install

# Start the React development server
npm start
```

The frontend application will automatically open in your browser at `http://localhost:3000`.

-----

## How to Test the Application Locally

The backend includes a suite of JUnit tests to ensure the core algorithm works correctly across various edge cases.

To run these tests:

```bash
# Navigate to the backend directory
cd backend

# Run the Maven test command (use mvnw.cmd on Windows)
./mvnw test
```

The tests will execute, and you will see a `BUILD SUCCESS` message if all tests pass.

---

## API Documentation

The backend exposes a simple REST API with two main endpoints.

### Compress Endpoint

  - **URL**: `/api/compress`

  - **Method**: `POST`

  - **Description**: Takes a string of text and returns its compressed binary representation and the corresponding code table.

  - **Request Body**:

    ```json
    {
      "text": "hello world"
    }
    ```

  - **Success Response (200 OK)**:

    ```json
    {
      "encodedText": "011011000100001111101010111011000",
      "codeTable": {
        " ": "00",
        "r": "010",
        "d": "011",
        "e": "100",
        "h": "1010",
        "w": "1011",
        "l": "110",
        "o": "111"
      }
    }
    ```

### Decompress Endpoint

  - **URL**: `/api/decompress`

  - **Method**: `POST`

  - **Description**: Takes a Huffman-encoded binary string and a code table to reconstruct the original text.

  - **Request Body**:

    ```json
    {
      "encodedText": "011011000100001111101010111011000",
      "codeTable": {
        " ": "00",
        "r": "010",
        "d": "011",
        "e": "100",
        "h": "1010",
        "w": "1011",
        "l": "110",
        "o": "111"
      }
    }
    ```

  - **Success Response (200 OK)**:

    ```json
    {
      "text": "hello world"
    }
    ```

-----
## Potential Improvements

  - **File Uploads**: Allow users to upload `.txt` files for compression and download the compressed output.
  - **Visual Tree**: Render a visual representation of the Huffman Tree that is generated during the compression process.
  - **API Documentation**: Add Swagger/OpenAPI documentation for the backend REST API.
  - **Enhanced Error Handling**: Provide more specific feedback to the user on the frontend for invalid inputs during decompression.
