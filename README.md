# ⚡ Java HTTP Server

A lightweight, high-performance HTTP/1.1 server written from scratch in **pure Java** — no frameworks, no libraries.

This project demonstrates how real-world HTTP servers like Tomcat, Netty, and Express work under the hood, with features like routing, gzip compression, and persistent connections.

---

## 🚀 Features Implemented

### ✅ Core HTTP Features
- [x] `GET` and `POST` method support
- [x] Persistent connections (HTTP/1.1 keep-alive)
- [x] Manual request parsing (request line, headers, body)
- [x] Lazy parsing for headers and body
- [x] Content-Length based body parsing
- [x] Connection: close/keep-alive handling

### 📂 File Handling
- [x] `GET /files/{fileName}`: Read file content
- [x] `POST /files/{fileName}`: Create new file with request body
- [x] Canonical path validation (prevents directory traversal)

### 🧠 Routing Engine
- [x] Trie-based router for fast static & dynamic path resolution
- [x] Path parameters support (`/files/{fileName}`, `/users/{id}`)
- [x] Method-based routing (`GET` vs `POST` on same path)

### 💨 Gzip Compression
- [x] Checks for `Accept-Encoding: gzip`
- [x] Compresses response with GZIP if supported
- [x] Adds `Content-Encoding: gzip` and correct `Content-Length`
- [x] Compatible with persistent connections

---

## 🔧 Endpoints

| Method | Path                    | Description                                  |
|--------|-------------------------|----------------------------------------------|
| GET    | `/hello`                | Returns static hello message                 |
| GET    | `/user-agent`           | Echoes client's `User-Agent` header          |
| GET    | `/files/{fileName}`     | Returns content of specified file            |
| POST   | `/files/{fileName}`     | Creates file with given content              |
| POST   | `/submit`               | Echoes the request body                      |

---

## 🧪 Testing the Server

### 🔹 Start the Server

```bash
javac Main.java
java Main
```

### 🔹 Test Hello Endpoint

```bash
curl http://localhost:8080/hello
```

### 🔹 Test GZIP Support

```bash
curl -H "Accept-Encoding: gzip" --compressed http://localhost:8080/hello
```

### 🔹 Test File Creation

```bash
curl -X POST -d "My test file" http://localhost:8080/files/test.txt
```

### 🔹 Test File Retrieval

```bash
curl http://localhost:8080/files/test.txt
```

---

## 🔒 Security Features

- ✅ Canonical path validation
- ✅ Safe socket handling (no over-reading)
- ✅ Handles malformed requests gracefully
- ✅ Optional socket timeout for idle connections

---

## 🛠 Future Enhancements

- [ ] Query parameter parsing
- [ ] Static file serving with correct MIME types
- [ ] Multipart/form-data support (file upload)
- [ ] JSON deserialization and response builder
- [ ] ETag and cache control headers
- [ ] Rate limiting per IP
- [ ] Middleware support (logging, auth, etc.)
- [ ] WebSocket upgrade handling

---

## 👨‍💻 Author

Built with 💙 by **Deva Prakash**  
Feel free to fork, improve, and make it your own.
