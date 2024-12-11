FROM python:3.10-slim

WORKDIR /app

# Install system dependencies for psycopg2 and git
RUN apt-get update && apt-get install -y libpq-dev gcc git && rm -rf /var/lib/apt/lists/*

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

EXPOSE 8000
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]
