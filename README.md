
# ✉️ Email Reply Generator (AI-powered)

This is a full-stack AI-powered application that generates professional email replies using Google's Gemini API. It's built with **React**, **Spring Boot**, and deployed using **Docker Compose**, **Nginx**, and monitored with **Prometheus + Grafana**.

---

## 🛠️ Tech Stack

| Layer       | Technology                        |
|------------|------------------------------------|
| Frontend   | React (email-writer-frontend)      |
| Backend    | Spring Boot (email-writer-sp)      |
| AI API     | Google Gemini API                  |
| Web Server | Nginx with reverse proxy + HTTPS   |
| Deployment | Docker + Docker Compose            |
| Monitoring | Prometheus, Grafana, Node Exporter, cAdvisor |
| Domain     | DuckDNS                            |
| SSL        | Let's Encrypt via acme.sh          |
| Host       | AWS EC2 (Ubuntu 22.04)             |

---


## 📁 Project Structure

```
Email-Reply-Generator/
├── email-writer-frontend/     # React frontend
├── email-writer-sp/           # Spring Boot backend
├── monitoring/                # Prometheus config
├── nginx/                     # Nginx reverse proxy + SSL certs
│   ├── default.conf
│   └── ssl/
│       ├── fullchain.cer
│       └── email-reply-generator.duckdns.org.key
├── docker-compose.yml         # Full-stack deployment
├── .env                       # GEMINI_API_KEY & other env vars
└── README.md
```

---

## 🚀 Local Development (Optional)

### Backend (Spring Boot)
```bash
cd email-writer-sp
./mvnw spring-boot:run
```

### Frontend (React)
```bash
cd email-writer-frontend
npm install
npm start
```

---

## 🐳 Dockerized Deployment

### 1. Clone & configure:
```bash
git clone https://github.com/yourusername/Email-Reply-Generator.git
cd Email-Reply-Generator
```

### 2. Add your environment variables
Create a `.env` file:

```env
GEMINI_API_KEY=your_gemini_api_key
GEMINI_API_URL=/v1beta/models/gemini-2.0-flash:generateContent
```

### 3. Run containers

```bash
docker-compose up -d --build
```

### 4. SSL Setup (Manual Step)

Make sure your DuckDNS domain points to your EC2 public IP. Then:

```bash
sudo apt install socat -y
curl https://get.acme.sh | sh
source ~/.bashrc

# Export your DuckDNS token
export DuckDNS_Token="your_duckdns_token"

~/.acme.sh/acme.sh --issue --dns dns_duckdns -d email-reply-generator.duckdns.org
```

### 5. Copy SSL Certs into Nginx mount:

```bash
mkdir -p nginx/ssl
cp ~/.acme.sh/email-reply-generator.duckdns.org_ecc/fullchain.cer nginx/ssl/
cp ~/.acme.sh/email-reply-generator.duckdns.org_ecc/email-reply-generator.duckdns.org.key nginx/ssl/
```

---

## 🔒 Nginx + HTTPS Configuration

Located in: `nginx/default.conf`

Ensure this snippet is used in `default.conf`:

```nginx
server {
  listen 443 ssl;
  server_name email-reply-generator.duckdns.org;

  ssl_certificate     /etc/nginx/ssl/fullchain.cer;
  ssl_certificate_key /etc/nginx/ssl/email-reply-generator.duckdns.org.key;

  location / {
    root /usr/share/nginx/html;
    index index.html;
    try_files $uri /index.html;
  }

  location /api/ {
    proxy_pass http://email-backend:8000/api/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  }
}
```

---

## 📊 Monitoring Setup

- **Prometheus** runs on: `http://<EC2-IP>:9090`
- **Grafana** runs on: `http://<EC2-IP>:3001`
- **cAdvisor** runs on: `http://<EC2-IP>:8080`
- Dashboards can be imported into Grafana from Docker + Node Exporter metrics.

---

## 🔁 Auto-Renew SSL (Cron)

```bash
crontab -e
# Add this line
0 0 * * * ~/.acme.sh/acme.sh --cron --home ~/.acme.sh > /dev/null
```

---

## 🔧 Useful Docker Commands

```bash
docker-compose up -d         # Start all services
docker-compose down          # Stop all services
docker logs <container>      # View logs
docker exec -it <container> bash  # Access container shell
```

---

## ✅ Health Check

```bash
curl http://localhost:8000/actuator/health
```

---

## 📮 Contact

Built with 💻 by **Anshu Waghmare**  
GitHub: [anshuw1](https://github.com/anshuw1)  
Project: [Prometheus-Grafana-Automation-Setup](https://github.com/anshuw1/Prometheus-Grafana-Automation-Setup)
