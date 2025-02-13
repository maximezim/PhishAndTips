#!/bin/bash
set -e

# Create required directory
mkdir -p /var/lib/apt/lists/partial

# Install sqlite3 if not present
if ! command -v sqlite3 &> /dev/null; then
    apt-get update
    apt-get install -y sqlite3
fi

# Start Gophish in the background and capture its PID
/opt/gophish/gophish &
GOPHISH_PID=$!

# Wait for the database to be initialized (max 30 seconds)
max_attempts=30
attempt=0
while [ $attempt -lt $max_attempts ]; do
    if sqlite3 /opt/gophish/gophish.db "SELECT name FROM sqlite_master WHERE type='table' AND name='smtp';" | grep -q "smtp"; then
        # Initialize SMTP settings if not already done
        if [ ! -f /opt/gophish/.initialized ]; then
            sqlite3 /opt/gophish/gophish.db "INSERT INTO smtp (
                id,
                user_id,
                interface_type,
                name,
                host,
                username,
                password,
                from_address,
                modified_date,
                ignore_cert_errors
            ) VALUES (
                1,
                1,
                'SMTP',
                'smtp',
                '${SMTP_HOST}:${SMTP_PORT}',
                '${SMTP_MAIL_USERNAME}',
                '${SMTP_MAIL_PASSWORD}',
                '${SMTP_MAIL_USERNAME}',
                datetime('now'),
                '${IGNORE_TLS_ERRORS}'
            );"
            touch /opt/gophish/.initialized
        fi
        break
    fi
    attempt=$((attempt + 1))
    sleep 1
done

if [ $attempt -eq $max_attempts ]; then
    echo "Timeout waiting for smtp table to be created"
    exit 1
fi

# Wait for Gophish process
wait $GOPHISH_PID