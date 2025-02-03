FROM python:3.10-slim

WORKDIR /app

ENV TZ=Europe/Paris
# Install system dependencies for psycopg2, git, and cron
RUN apt-get update && apt-get install -y dos2unix tzdata libpq-dev gcc git cron && rm -rf /var/lib/apt/lists/*

# Clone SpiderFoot
RUN git clone https://github.com/smicallef/spiderfoot.git /opt/spiderfoot

# Install SpiderFoot dependencies
WORKDIR /opt/spiderfoot
RUN pip install --no-cache-dir -r requirements.txt

# Return to app directory
WORKDIR /app

# Copy requirements and install FastAPI app dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy the app code
COPY . .

# Copy cronjob files
COPY app/weekly_scan.py /app/weekly_scan.py
COPY cronjob/weekly_scan_cron /etc/cron.d/weekly_scan_cron
RUN chmod 0644 /etc/cron.d/weekly_scan_cron && crontab /etc/cron.d/weekly_scan_cron
RUN dos2unix /etc/cron.d/weekly_scan_cron
RUN touch /var/log/cron.log

EXPOSE 8001

# Start cron and uvicorn
CMD ["sh", "-c", "cron -f & uvicorn app.main:app --host 0.0.0.0 --port 8000"]
